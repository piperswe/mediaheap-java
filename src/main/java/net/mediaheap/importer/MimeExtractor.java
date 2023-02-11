package net.mediaheap.importer;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MimeExtractor implements Extractor {
    private static final MimeExtractor global = new MimeExtractor();
    private final Map<String, Extractor> extractors = new HashMap<>();

    public static MimeExtractor getGlobal() {
        return global;
    }

    public void registerExtractor(String mimeType, Extractor extractor) {
        extractors.put(mimeType, extractor);
    }

    public void registerBuiltinExtractors() {
        var mp3 = new MP3Extractor();
        registerExtractor("audio/mpeg", mp3);
        registerExtractor("audio/mp3", mp3);
        var flac = new AudioTaggerExtractor("https://schemas.mediaheap.net/flac");
        registerExtractor("audio/flac", flac);
        registerExtractor("audio/x-flac", flac);
        var m4a = new AudioTaggerExtractor("https://schemas.mediaheap.net/m4a");
        registerExtractor("audio/mp4", m4a);
        registerExtractor("audio/m4a", m4a);
        var ogg = new AudioTaggerExtractor("https://schemas.mediaheap.net/ogg");
        registerExtractor("audio/ogg", ogg);
        var wav = new AudioTaggerExtractor("https://schemas.mediaheap.net/wav");
        registerExtractor("audio/x-wav", wav);
    }

    public Extractor getExtractor(String mimeType) {
        return extractors.get(mimeType);
    }

    @Override
    public List<MediaHeapTag> extractTagsFrom(MediaHeapFile file) throws IOException {
        var extractor = getExtractor(file.getFileType());
        if (extractor == null) {
            return Collections.emptyList();
        } else {
            return extractor.extractTagsFrom(file);
        }
    }
}
