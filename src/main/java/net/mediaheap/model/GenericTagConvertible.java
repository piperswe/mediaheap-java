package net.mediaheap.model;

import java.util.List;

public interface GenericTagConvertible {
    List<MediaHeapTag> getTags(MediaHeapFile file, String namespace);
}
