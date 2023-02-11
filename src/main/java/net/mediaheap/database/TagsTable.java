package net.mediaheap.database;

import lombok.Cleanup;
import lombok.NonNull;
import net.mediaheap.model.MediaHeapTag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagsTable {
    @NonNull
    private final DatabaseConnection db;

    TagsTable(@NonNull DatabaseConnection db) {
        this.db = db;
    }

    public int[] insertTags(List<MediaHeapTag> tags) throws SQLException {
        var conn = db.getConnection();
        conn.setAutoCommit(false);
        int[] result;
        try (var stmt = db.getConnection().prepareStatement("INSERT INTO Tag(fileId, namespace, key, value) VALUES (?, ?, ?, ?);")) {
            for (var tag : tags) {
                stmt.setInt(1, tag.getFileId());
                stmt.setString(2, tag.getNamespace());
                stmt.setString(3, tag.getKey());
                stmt.setString(4, tag.getValue());
                stmt.addBatch();
            }
            result = stmt.executeBatch();
        }
        conn.commit();
        return result;
    }

    public List<MediaHeapTag> getTagsForFile(int fileId) throws SQLException {
        @Cleanup var stmt = db.getConnection().prepareStatement("SELECT * FROM Tag WHERE fileId = ?;");
        stmt.setInt(1, fileId);
        @Cleanup var results = stmt.executeQuery();
        var tags = new ArrayList<MediaHeapTag>();
        while (results.next()) {
            tags.add(MediaHeapTag.of(
                    results.getInt("id"),
                    results.getInt("fileId"),
                    results.getString("namespace"),
                    results.getString("key"),
                    results.getString("value")
            ));
        }
        return tags;
    }
}
