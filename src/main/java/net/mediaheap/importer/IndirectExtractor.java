package net.mediaheap.importer;

import lombok.NonNull;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

public class IndirectExtractor implements Extractor {
    @NonNull
    private final Supplier<Extractor> supplier;

    public IndirectExtractor(@NonNull Supplier<Extractor> supplier) {
        this.supplier = supplier;
    }

    @Override
    public @NonNull List<MediaHeapTag> extractTagsFrom(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws IOException {
        var extractor = supplier.get();
        return extractor.extractTagsFrom(file, existingTags);
    }
}
