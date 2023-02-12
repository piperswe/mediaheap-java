package net.mediaheap.importer;

import com.google.common.io.BaseEncoding;
import lombok.Cleanup;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.mediaheap.database.DatabaseConnection;
import net.mediaheap.model.MediaHeapFile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

public class Importer {
    @NonNull
    private final DatabaseConnection db;
    @Getter
    @Setter
    @NonNull
    private Extractor extractor = MimeExtractor.getGlobal();

    public Importer(@NonNull DatabaseConnection db) {
        this.db = db;
    }

    public static Optional<String> getPathMimeType(@NonNull String path) throws IOException {
        return Optional.ofNullable(Files.probeContentType(FileSystems.getDefault().getPath(path)));
    }

    private @NonNull Hashes hashFile(@NonNull String path) throws IOException {
        try {
            var buffer = new byte[8192];
            int count;
            var sha256 = MessageDigest.getInstance("SHA-256");
            var sha512 = MessageDigest.getInstance("SHA-512/256");
            var md5 = MessageDigest.getInstance("MD5");
            @Cleanup var fis = new FileInputStream(path);
            @Cleanup var bis = new BufferedInputStream(fis);
            while ((count = bis.read(buffer)) > 0) {
                sha256.update(buffer, 0, count);
                sha512.update(buffer, 0, count);
                md5.update(buffer, 0, count);
            }
            var hashes = new Hashes();
            hashes.sha256 = BaseEncoding.base16().lowerCase().encode(sha256.digest());
            hashes.sha512 = BaseEncoding.base16().lowerCase().encode(sha512.digest());
            hashes.md5 = BaseEncoding.base16().lowerCase().encode(md5.digest());
            return hashes;
        } catch (NoSuchAlgorithmException e) {
            // This shouldn't happen - every Java version we deploy to should have all three digests available.
            throw new RuntimeException(e);
        }
    }

    public @NonNull MediaHeapFile importFromWithMimeType(@NonNull String path, @NonNull Optional<String> mimeType) throws IOException, SQLException {
        var hashes = hashFile(path);
        var fileType = mimeType.orElse("application/octet-stream");
        var file = MediaHeapFile.of(path, hashes.sha256, hashes.sha512, hashes.md5, fileType);
        file = db.getFiles().insertFile(file);
        var tags = getExtractor().extractTagsFrom(file, Collections.emptyList());
        db.getTags().insertTags(tags);
        return file;
    }

    public @NonNull MediaHeapFile importFromWithMimeType(@NonNull String path, String mimeType) throws IOException, SQLException {
        return importFromWithMimeType(path, Optional.ofNullable(mimeType));
    }

    public @NonNull MediaHeapFile importFrom(@NonNull String path) throws IOException, SQLException {
        var contentType = getPathMimeType(path);
        return importFromWithMimeType(path, contentType);
    }

    private static class Hashes {
        String sha256;
        String sha512;
        String md5;
    }
}
