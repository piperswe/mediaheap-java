package net.mediaheap.model;

import java.util.Objects;

public class MediaHeapTag {
    private int id;
    private int fileId;
    private String namespace;
    private String key;
    private String value;

    public MediaHeapTag() {
    }

    public MediaHeapTag(int fileId, String namespace, String key, String value) {
        this.fileId = fileId;
        this.namespace = Objects.requireNonNull(namespace);
        this.key = Objects.requireNonNull(key);
        this.value = Objects.requireNonNull(value);
    }

    public MediaHeapTag(MediaHeapFile file, String namespace, String key, String value) {
        this(file.getId(), namespace, key, value);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
