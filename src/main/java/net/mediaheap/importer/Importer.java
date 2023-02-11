package net.mediaheap.importer;

import com.google.common.io.BaseEncoding;
import net.mediaheap.model.MediaHeapFile;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Importer {
    private Extractor extractor = MimeExtractor.getGlobal();

    public static String getPathMimeType(String path) throws IOException {
        return Files.probeContentType(FileSystems.getDefault().getPath(path));
    }

    public Extractor getExtractor() {
        return extractor;
    }

    public void setExtractor(Extractor extractor) {
        this.extractor = extractor;
    }

    private Hashes hashFile(String path) throws IOException {
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

    public MediaHeapFile importFromWithMimeType(String path, String mimeType) throws IOException {
        MediaHeapFile file = new MediaHeapFile();
        file.setPath(path);
        var hashes = hashFile(path);
        file.setSha256Hash(hashes.sha256);
        file.setSha512Hash(hashes.sha512);
        file.setMd5Hash(hashes.md5);
        file.setFileType(mimeType);
        getExtractor().extractTagsFrom(file);
        return file;
    }

    public MediaHeapFile importFrom(String path) throws IOException {
        var contentType = getPathMimeType(path);
        return importFromWithMimeType(path, contentType);
    }

    private static class Hashes {
        String sha256;
        String sha512;
        String md5;
    }
}
