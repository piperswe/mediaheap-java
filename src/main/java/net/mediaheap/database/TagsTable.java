package net.mediaheap.database;

import lombok.Cleanup;
import lombok.NonNull;
import net.mediaheap.model.MediaHeapFile;
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

    public void insertTags(@NonNull List<@NonNull MediaHeapTag> tags) throws SQLException {
        var conn = db.getConnection();
        var autocommit = conn.getAutoCommit();
        var rolledBack = false;
        conn.setAutoCommit(false);
        try (var stmt = db.getConnection().prepareStatement("INSERT INTO Tag(fileId, namespace, key, value) VALUES (?, ?, ?, ?);")) {
            for (var tag : tags) {
                stmt.setInt(1, tag.getFileId());
                stmt.setString(2, tag.getNamespace());
                stmt.setString(3, tag.getKey());
                stmt.setString(4, tag.getValue());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (Exception e) {
            conn.rollback();
            rolledBack = true;
            throw e;
        } finally {
            if (!rolledBack) {
                conn.commit();
            }
            conn.setAutoCommit(autocommit);
        }
    }

    public @NonNull List<@NonNull MediaHeapTag> getTagsForFile(int fileId) throws SQLException {
        @Cleanup var stmt = db.getConnection().prepareStatement("SELECT * FROM Tag WHERE fileId = ?;");
        stmt.setInt(1, fileId);
        @Cleanup var results = stmt.executeQuery();
        var tags = new ArrayList<MediaHeapTag>();
        while (results.next()) {
            tags.add(MediaHeapTag.fromResultSet(results));
        }
        return tags;
    }

    public @NonNull List<@NonNull MediaHeapTag> getTagsForFile(@NonNull MediaHeapFile file) throws SQLException {
        return getTagsForFile(file.getId());
    }
}
