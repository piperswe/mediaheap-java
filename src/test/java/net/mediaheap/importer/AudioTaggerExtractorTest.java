package net.mediaheap.importer;

import org.junit.jupiter.api.Test;
import org.overviewproject.mime_types.GetBytesException;

import java.io.IOException;

import static net.mediaheap.importer.TestHelpers.assertHasTag;
import static net.mediaheap.importer.TestHelpers.tagsFromTestFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AudioTaggerExtractorTest {
    public static final String FLAC_NS = "https://schemas.mediaheap.net/file/audio/tags/flac";
    public static final Extractor FLAC_EXTRACTOR = new AudioTaggerExtractor(FLAC_NS);
    public static final String OGG_NS = "https://schemas.mediaheap.net/file/audio/tags/ogg";
    public static final Extractor OGG_EXTRACTOR = new AudioTaggerExtractor(OGG_NS);
    public static final String WAV_NS = "https://schemas.mediaheap.net/file/audio/tags/wav";
    public static final Extractor WAV_EXTRACTOR = new AudioTaggerExtractor(WAV_NS);
    public static final String M4A_NS = "https://schemas.mediaheap.net/file/audio/tags/m4a";
    public static final Extractor M4A_EXTRACTOR = new AudioTaggerExtractor(M4A_NS);

    @Test
    void missingFlac() throws IOException, GetBytesException {
        var tags = tagsFromTestFile(FLAC_EXTRACTOR, "flac/missing_tags.flac");
        assertEquals(1, tags.size(), "More than just the FLAC VENDOR tag were saved");
    }

    @Test
    void filledFlac() throws IOException, GetBytesException {
        var tags = tagsFromTestFile(FLAC_EXTRACTOR, "flac/filled_tags.flac");
        assertHasTag(FLAC_NS, tags, "TITLE", "Title");
        assertHasTag(FLAC_NS, tags, "ARTIST", "Artist");
        assertHasTag(FLAC_NS, tags, "ALBUM", "Album");
        assertHasTag(FLAC_NS, tags, "GENRE", "Pop");
        assertHasTag(FLAC_NS, tags, "COMMENT", "Comment");
        assertHasTag(FLAC_NS, tags, "ALBUMARTIST", "Album Artist");
        assertHasTag(FLAC_NS, tags, "COMPOSER", "Composer");
        assertHasTag(FLAC_NS, tags, "DISCNUMBER", "Discnumber");
        assertHasTag(FLAC_NS, tags, "DATE", "2023");
        assertHasTag(FLAC_NS, tags, "TRACKNUMBER", "1");
    }

    @Test
    void missingOgg() throws IOException, GetBytesException {
        var tags = tagsFromTestFile(OGG_EXTRACTOR, "ogg/missing_tags.ogg");
        assertEquals(1, tags.size(), "More than just the OGG VENDOR tag were saved");
    }

    @Test
    void filledOgg() throws IOException, GetBytesException {
        var tags = tagsFromTestFile(OGG_EXTRACTOR, "ogg/filled_tags.ogg");
        assertHasTag(OGG_NS, tags, "TITLE", "Title");
        assertHasTag(OGG_NS, tags, "ARTIST", "Artist");
        assertHasTag(OGG_NS, tags, "ALBUM", "Album");
        assertHasTag(OGG_NS, tags, "GENRE", "Pop");
        assertHasTag(OGG_NS, tags, "COMMENT", "Comment");
        assertHasTag(OGG_NS, tags, "ALBUMARTIST", "Album Artist");
        assertHasTag(OGG_NS, tags, "COMPOSER", "Composer");
        assertHasTag(OGG_NS, tags, "DISCNUMBER", "Discnumber");
        assertHasTag(OGG_NS, tags, "DATE", "2023");
        assertHasTag(OGG_NS, tags, "TRACKNUMBER", "1");
    }

    @Test
    void missingWav() throws IOException, GetBytesException {
        var tags = tagsFromTestFile(WAV_EXTRACTOR, "wav/missing_tags.wav");
        assertEquals(0, tags.size());
    }

    @Test
    void filledWav() throws IOException, GetBytesException {
        var tags = tagsFromTestFile(WAV_EXTRACTOR, "wav/filled_tags.wav");
        assertHasTag(WAV_NS, tags, "TIT2", "Title");
        assertHasTag(WAV_NS, tags, "TPE1", "Artist");
        assertHasTag(WAV_NS, tags, "TALB", "Album");
        assertHasTag(WAV_NS, tags, "TCON", "Pop");
        assertHasTag(WAV_NS, tags, "COMM", "Comment");
        assertHasTag(WAV_NS, tags, "TPOS", "5");
        assertHasTag(WAV_NS, tags, "TPE2", "Album Artist");
        assertHasTag(WAV_NS, tags, "TCOM", "Composer");
        assertHasTag(WAV_NS, tags, "TYER", "2023");
        assertHasTag(WAV_NS, tags, "TRCK", "1");
    }

    @Test
    void missingM4a() throws IOException, GetBytesException {
        var tags = tagsFromTestFile(M4A_EXTRACTOR, "m4a/missing_tags.m4a");
        assertEquals(1, tags.size(), "More than just the M4A tool tag were saved");
    }

    @Test
    void filledM4a() throws IOException, GetBytesException {
        var tags = tagsFromTestFile(M4A_EXTRACTOR, "m4a/filled_tags.m4a");
        assertHasTag(M4A_NS, tags, "_cnam", "Title");
        assertHasTag(M4A_NS, tags, "_cART", "Artist");
        assertHasTag(M4A_NS, tags, "_calb", "Album");
        assertHasTag(M4A_NS, tags, "gnre", "Pop");
        assertHasTag(M4A_NS, tags, "_ccmt", "Comment");
        assertHasTag(M4A_NS, tags, "disk", "5");
        assertHasTag(M4A_NS, tags, "aART", "Album Artist");
        assertHasTag(M4A_NS, tags, "_cwrt", "Composer");
        assertHasTag(M4A_NS, tags, "_cday", "2023");
        assertHasTag(M4A_NS, tags, "trkn", "1");
    }
}
