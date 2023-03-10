package net.mediaheap.importer;

import lombok.NonNull;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import org.overviewproject.mime_types.GetBytesException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestHelpers {
    public static void assertIsOne(String x, String... ys) {
        for (var y : ys) {
            if (y.equals(x)) return;
        }
        fail(String.format("%s is not one of %s", x, String.join(", ", ys)));
    }

    public static @NonNull String testFilePath(@NonNull String filename) {
        return "./src/test/resources/net/mediaheap/importer/" + filename;
    }

    public static @NonNull List<@NonNull MediaHeapTag> tagsFromTestFile(@NonNull Extractor extractor, @NonNull String filename) throws IOException, GetBytesException {
        var path = testFilePath(filename);
        var file = MediaHeapFile.of(path, "", "", "", Importer.getPathMimeType(path).orElse(null));
        return extractor.extractTagsFrom(file, Collections.emptyList());
    }

    public static void assertHasTag(@NonNull String namespace, @NonNull List<@NonNull MediaHeapTag> tags, @NonNull String key, String value) {
        for (var tag : tags) {
            if (tag.namespace().equals(namespace) && tag.key().equals(key)) {
                assertEquals(value, tag.value());
                return;
            }
        }
        fail(String.format("Missing %s#%s", namespace, key));
    }
}
