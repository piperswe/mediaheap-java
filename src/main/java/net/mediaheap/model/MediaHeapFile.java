package net.mediaheap.model;

import lombok.NonNull;
import lombok.Value;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@Value(staticConstructor = "of")
public class MediaHeapFile {
    int id = -1;
    @NonNull String path;
    @NonNull String sha256Hash;
    @NonNull String sha512Hash;
    @NonNull String md5Hash;
    String fileType;

    public @NonNull Path getNioPath() {
        return FileSystems.getDefault().getPath(getPath());
    }

    public @NonNull File getFile() {
        return new File(path);
    }
}
