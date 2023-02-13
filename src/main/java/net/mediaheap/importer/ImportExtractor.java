package net.mediaheap.importer;

import lombok.NonNull;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;

public class ImportExtractor implements Extractor {
    public static final @NonNull String NS = "https://schemas.mediaheap.net/file/import";

    @Override
    public @NonNull List<MediaHeapTag> extractTagsFrom(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws IOException {
        return MediaHeapTagListFactory.start(file, NS)
                .add("originalPath", file.path())
                .add("size", String.valueOf(Files.size(file.getNioPath())))
                .add("sha256", file.sha256Hash())
                .add("sha512", file.sha512Hash())
                .add("md5", file.md5Hash())
                .add("timestamp", String.valueOf(Instant.now().getEpochSecond()))
                .build();
    }
}
