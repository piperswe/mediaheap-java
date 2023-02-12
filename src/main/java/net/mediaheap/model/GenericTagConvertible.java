package net.mediaheap.model;

import lombok.NonNull;

import java.util.List;

public interface GenericTagConvertible {
    @NonNull List<MediaHeapTag> getTags(@NonNull MediaHeapFile file, @NonNull String namespace);
}
