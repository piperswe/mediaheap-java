package net.mediaheap.model;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class MediaHeapFile {
    private int id;
    private String path;
    private String sha256Hash;
    private String sha512Hash;
    private String md5Hash;
    private String fileType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSha256Hash() {
        return sha256Hash;
    }

    public void setSha256Hash(String sha256Hash) {
        this.sha256Hash = sha256Hash;
    }

    public String getSha512Hash() {
        return sha512Hash;
    }

    public void setSha512Hash(String sha512Hash) {
        this.sha512Hash = sha512Hash;
    }

    public String getMd5Hash() {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash) {
        this.md5Hash = md5Hash;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Path getNioPath() {
        return FileSystems.getDefault().getPath(getPath());
    }

    public File getFile() {
        return new File(path);
    }
}
