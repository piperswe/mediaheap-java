package net.mediaheap.model;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MediaHeapTagListFactory {
    private final int fileId;
    @NonNull
    private final String namespace;
    @NonNull
    private final List<MediaHeapTag> tags;

    private MediaHeapTagListFactory(int fileId, @NonNull String namespace, @NonNull List<MediaHeapTag> tags) {
        this.fileId = fileId;
        this.namespace = namespace;
        this.tags = tags;
    }

    public static @NonNull MediaHeapTagListFactory start(@NonNull MediaHeapFile file, @NonNull String namespace) {
        return start(file.getId(), namespace);
    }

    public static @NonNull MediaHeapTagListFactory start(int fileId, @NonNull String namespace) {
        return start(fileId, namespace, new ArrayList<>());
    }

    public static @NonNull MediaHeapTagListFactory start(@NonNull MediaHeapFile file, @NonNull String namespace, @NonNull List<MediaHeapTag> tags) {
        return start(file.getId(), namespace, tags);
    }

    public static @NonNull MediaHeapTagListFactory start(int fileId, @NonNull String namespace, @NonNull List<MediaHeapTag> tags) {
        return new MediaHeapTagListFactory(fileId, namespace, tags);
    }

    public @NonNull MediaHeapTagListFactory add(@NonNull String key, @NonNull Optional<String> value) {
        value.ifPresent(s -> tags.add(MediaHeapTag.of(-1, fileId, namespace, key, s)));
        return this;
    }

    public @NonNull MediaHeapTagListFactory add(@NonNull String key, String value) {
        return add(key, Optional.ofNullable("".equals(value) ? null : value));
    }

    public @NonNull List<MediaHeapTag> build() {
        return tags;
    }
}
