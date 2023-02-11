package net.mediaheap.importer;

import com.google.common.io.BaseEncoding;
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
import java.util.Collections;

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

    public static String getPathMimeType(@NonNull String path) throws IOException {
        return Files.probeContentType(FileSystems.getDefault().getPath(path));
    }

    private @NonNull Hashes hashFile(@NonNull String path) throws IOException {
        try {
            var buffer = new byte[8192];
            int count;
            var sha256 = MessageDigest.getInstance("SHA-256");
            var sha512 = MessageDigest.getInstance("SHA-512/256");
            var md5 = MessageDigest.getInstance("MD5");
            var bis = new BufferedInputStream(new FileInputStream(path));
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

    public @NonNull MediaHeapFile importFromWithMimeType(@NonNull String path, String mimeType) throws IOException {
        var hashes = hashFile(path);
        var fileType = "application/octet-stream";
        if (mimeType != null && !"".equals(mimeType)) {
            fileType = mimeType;
        }
        var file = MediaHeapFile.of(path, hashes.sha256, hashes.sha512, hashes.md5, fileType);
        getExtractor().extractTagsFrom(file, Collections.emptyList());
        return file;
    }

    public @NonNull MediaHeapFile importFrom(@NonNull String path) throws IOException {
        var contentType = getPathMimeType(path);
        return importFromWithMimeType(path, contentType);
    }

    private static class Hashes {
        String sha256;
        String sha512;
        String md5;
    }
}
