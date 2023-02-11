package net.mediaheap.importer;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.musicbrainz.http.HTTPMusicbrainzClient;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MimeExtractor implements Extractor {
    private static final Logger logger = Logger.getLogger("MimeExtractor");

    private static final MimeExtractor global = new MimeExtractor();
    private Extractor musicbrainzExtractor = new MusicbrainzExtractor(new HTTPMusicbrainzClient());
    private final Map<String, List<Extractor>> extractors = new HashMap<>();

    public static MimeExtractor getGlobal() {
        return global;
    }

    public void registerExtractors(String mimeType, Extractor... newExtractors) {
        if (!extractors.containsKey(mimeType)) {
            extractors.put(mimeType, new ArrayList<>());
        }
        extractors.get(mimeType).addAll(Arrays.asList(newExtractors));
    }

    public void registerBuiltinExtractors() {
        var musicbrainz = new IndirectExtractor(() -> this.musicbrainzExtractor);
        var mp3 = new MP3Extractor();
        registerExtractors("audio/mpeg", mp3, musicbrainz);
        registerExtractors("audio/mp3", mp3, musicbrainz);
        var flac = new AudioTaggerExtractor("https://schemas.mediaheap.net/file/audio/tags/flac");
        registerExtractors("audio/flac", flac, musicbrainz);
        registerExtractors("audio/x-flac", flac, musicbrainz);
        var m4a = new AudioTaggerExtractor("https://schemas.mediaheap.net/file/audio/tags/m4a");
        registerExtractors("audio/mp4", m4a, musicbrainz);
        registerExtractors("audio/m4a", m4a, musicbrainz);
        var ogg = new AudioTaggerExtractor("https://schemas.mediaheap.net/file/audio/tags/ogg");
        registerExtractors("audio/ogg", ogg, musicbrainz);
        var wav = new AudioTaggerExtractor("https://schemas.mediaheap.net/file/audio/tags/wav");
        registerExtractors("audio/wav", wav, musicbrainz);
        registerExtractors("audio/x-wav", wav, musicbrainz);
    }

    public List<Extractor> getExtractors(String mimeType) {
        return extractors.getOrDefault(mimeType, Collections.emptyList());
    }

    @Override
    public List<MediaHeapTag> extractTagsFrom(MediaHeapFile file, List<MediaHeapTag> existingTags) throws IOException {
        var extractors = getExtractors(file.getFileType());
        if (extractors.isEmpty()) {
            logger.log(Level.WARNING, String.format("No extractors for file type %s!", file.getFileType()));
        }
        List<MediaHeapTag> tags = new ArrayList<>();
        for (var extractor : extractors) {
            tags.addAll(extractor.extractTagsFrom(file, tags));
        }
        return tags;
    }

    public void setMusicbrainzExtractor(Extractor musicbrainzExtractor) {
        this.musicbrainzExtractor = musicbrainzExtractor;
    }
}
