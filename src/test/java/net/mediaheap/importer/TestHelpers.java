package net.mediaheap.importer;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestHelpers {
    public static List<MediaHeapTag> tagsFromTestFile(Extractor extractor, String filename) throws IOException {
        var file = new MediaHeapFile();
        var path = "./src/test/resources/net/mediaheap/importer/" + filename;
        file.setPath(path);
        file.setFileType(Importer.getPathMimeType(path));
        return extractor.extractTagsFrom(file);
    }

    public static void assertHasTag(String namespace, List<MediaHeapTag> tags, String key, String value) {
        for (var tag : tags) {
            if (tag.getNamespace().equals(namespace) && tag.getKey().equals(key)) {
                assertEquals(value, tag.getValue());
                return;
            }
        }
        fail(String.format("Missing %s#%s", namespace, key));
    }
}
