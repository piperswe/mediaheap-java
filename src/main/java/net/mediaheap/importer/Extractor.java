package net.mediaheap.importer;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;

import java.io.IOException;
import java.util.List;

public interface Extractor {
    List<MediaHeapTag> extractTagsFrom(MediaHeapFile file, List<MediaHeapTag> existingTags) throws IOException;
}
