package net.mediaheap.importer;

import lombok.NonNull;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class NullExtractor implements Extractor {
    @Override
    public @NonNull List<MediaHeapTag> extractTagsFrom(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws IOException {
        return Collections.emptyList();
    }
}
