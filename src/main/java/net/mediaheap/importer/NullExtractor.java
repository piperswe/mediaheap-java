package net.mediaheap.importer;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class NullExtractor implements Extractor {
    @Override
    public List<MediaHeapTag> extractTagsFrom(MediaHeapFile file, List<MediaHeapTag> existingTags) throws IOException {
        return Collections.emptyList();
    }
}
