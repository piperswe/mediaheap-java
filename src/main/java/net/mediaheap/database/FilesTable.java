package net.mediaheap.database;

import lombok.Cleanup;
import lombok.NonNull;
import net.mediaheap.model.MediaHeapFile;

import java.sql.SQLException;

public class FilesTable {
    @NonNull
    private final DatabaseConnection db;

    FilesTable(@NonNull DatabaseConnection db) {
        this.db = db;
    }

    public @NonNull MediaHeapFile insertFile(@NonNull MediaHeapFile file) throws SQLException {
        var conn = db.getConnection();
        var autocommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        var rolledBack = false;
        try (var stmt = db.getConnection().prepareStatement("INSERT INTO File(path, sha256Hash, sha512Hash, md5Hash, fileType) VALUES (?, ?, ?, ?, ?) RETURNING *;")) {
            stmt.setString(1, file.getPath());
            stmt.setString(2, file.getSha256Hash());
            stmt.setString(3, file.getSha512Hash());
            stmt.setString(4, file.getMd5Hash());
            stmt.setString(5, file.getFileType());
            @Cleanup var results = stmt.executeQuery();
            if (results.next()) {
                return MediaHeapFile.fromResultSet(results);
            } else {
                throw new SQLException("Insert didn't return any rows");
            }
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

    public MediaHeapFile getFile(int id) throws SQLException {
        @Cleanup var stmt = db.getConnection().prepareStatement("SELECT * FROM File WHERE id = ?;");
        stmt.setInt(1, id);
        @Cleanup var results = stmt.executeQuery();
        if (results.next()) {
            return MediaHeapFile.fromResultSet(results);
        } else {
            return null;
        }
    }
}
