package net.mediaheap.model;

import lombok.NonNull;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;

public record MediaHeapFile(int id, @NonNull String path, @NonNull String sha256Hash, @NonNull String sha512Hash,
                            @NonNull String md5Hash, String fileType) {
    public static @NonNull MediaHeapFile of(@NonNull String path, @NonNull String sha256Hash, @NonNull String sha512Hash, @NonNull String md5Hash, String fileType) {
        return new MediaHeapFile(-1, path, sha256Hash, sha512Hash, md5Hash, fileType);
    }

    public static @NonNull MediaHeapFile fromResultSet(@NonNull ResultSet results) throws SQLException {
        return new MediaHeapFile(
                results.getInt("id"),
                results.getString("path"),
                results.getString("sha256Hash"),
                results.getString("sha512Hash"),
                results.getString("md5Hash"),
                results.getString("fileType")
        );
    }

    public @NonNull Path getNioPath() {
        return FileSystems.getDefault().getPath(path());
    }

    public @NonNull File getFile() {
        return new File(path);
    }
}
