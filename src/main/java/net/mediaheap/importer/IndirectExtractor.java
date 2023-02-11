package net.mediaheap.importer;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

public class IndirectExtractor implements Extractor {
    private Supplier<Extractor> supplier;

    public IndirectExtractor(Supplier<Extractor> supplier) {
        this.supplier = supplier;
    }

    @Override
    public List<MediaHeapTag> extractTagsFrom(MediaHeapFile file, List<MediaHeapTag> existingTags) throws IOException {
        var extractor = supplier.get();
        return extractor.extractTagsFrom(file, existingTags);
    }
}
