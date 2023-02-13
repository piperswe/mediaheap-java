package net.mediaheap.importer;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.flogger.Flogger;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.musicbrainz.http.HTTPMusicbrainzClient;

import java.io.IOException;
import java.util.*;

@Flogger
public class MimeExtractor implements Extractor {
    private static final MimeExtractor global = new MimeExtractor();
    private final Map<String, List<Extractor>> extractors = new HashMap<>();
    @Getter
    @Setter
    @NonNull
    private Extractor musicbrainzExtractor = new MusicbrainzExtractor(new HTTPMusicbrainzClient());

    public static @NonNull MimeExtractor getGlobal() {
        return global;
    }

    public void registerExtractors(@NonNull String mimeType, @NonNull Extractor... newExtractors) {
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
        registerExtractors("audio/x-m4a", m4a, musicbrainz);
        var ogg = new AudioTaggerExtractor("https://schemas.mediaheap.net/file/audio/tags/ogg");
        registerExtractors("audio/ogg", ogg, musicbrainz);
        registerExtractors("audio/x-vorbis+ogg", ogg, musicbrainz);
        var wav = new AudioTaggerExtractor("https://schemas.mediaheap.net/file/audio/tags/wav");
        registerExtractors("audio/wav", wav, musicbrainz);
        registerExtractors("audio/x-wav", wav, musicbrainz);
        registerExtractors("audio/vnd.wave", wav, musicbrainz);
    }

    public @NonNull List<Extractor> getExtractors(@NonNull Optional<String> mimeType) {
        return mimeType.flatMap((t) -> Optional.ofNullable(extractors.get(t))).orElse(Collections.emptyList());
    }

    public @NonNull List<Extractor> getExtractors(String mimeType) {
        return getExtractors(Optional.ofNullable(mimeType));
    }

    @Override
    public @NonNull List<MediaHeapTag> extractTagsFrom(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws IOException {
        var extractors = getExtractors(file.getFileType());
        if (extractors.isEmpty()) {
            log.atWarning().log("No extractors for file type %s!", file.getFileType());
        }
        List<MediaHeapTag> tags = new ArrayList<>();
        for (var extractor : extractors) {
            tags.addAll(extractor.extractTagsFrom(file, tags));
        }
        return tags;
    }
}
