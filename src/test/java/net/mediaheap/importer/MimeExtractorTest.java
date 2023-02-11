package net.mediaheap.importer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static net.mediaheap.importer.TestHelpers.assertHasTag;
import static net.mediaheap.importer.TestHelpers.tagsFromTestFile;

public class MimeExtractorTest {
    private MimeExtractor extractor;

    @BeforeEach
    public void init() {
        extractor = new MimeExtractor();
        extractor.registerBuiltinExtractors();
    }

    @Test
    void testId3v2() throws IOException {
        var tags = tagsFromTestFile(extractor, "mp3/id3v2.mp3");
        assertHasTag(MP3Extractor.V2NS, tags, "TALB", "Album Title");
        assertHasTag(MP3Extractor.V2NS, tags, "TCON", "Pop");
        assertHasTag(MP3Extractor.V2NS, tags, "TIT2", "Track Title");
        assertHasTag(MP3Extractor.V2NS, tags, "TPE1", "Artist Name");
        assertHasTag(MP3Extractor.V2NS, tags, "TYER", "2023");
    }

    @Test
    void testFlac() throws IOException {
        var tags = tagsFromTestFile(AudioTaggerExtractorTest.FLAC_EXTRACTOR, "flac/filled_tags.flac");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "TITLE", "Title");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "ARTIST", "Artist");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "ALBUM", "Album");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "GENRE", "Pop");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "COMMENT", "Comment");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "ALBUMARTIST", "Album Artist");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "COMPOSER", "Composer");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "DISCNUMBER", "Discnumber");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "DATE", "2023");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "TRACKNUMBER", "1");
    }

    @Test
    void testFilledOgg() throws IOException {
        var tags = tagsFromTestFile(AudioTaggerExtractorTest.OGG_EXTRACTOR, "ogg/filled_tags.ogg");
        assertHasTag(AudioTaggerExtractorTest.OGG_NS, tags, "TITLE", "Title");
        assertHasTag(AudioTaggerExtractorTest.OGG_NS, tags, "ARTIST", "Artist");
        assertHasTag(AudioTaggerExtractorTest.OGG_NS, tags, "ALBUM", "Album");
        assertHasTag(AudioTaggerExtractorTest.OGG_NS, tags, "GENRE", "Pop");
        assertHasTag(AudioTaggerExtractorTest.OGG_NS, tags, "COMMENT", "Comment");
        assertHasTag(AudioTaggerExtractorTest.OGG_NS, tags, "ALBUMARTIST", "Album Artist");
        assertHasTag(AudioTaggerExtractorTest.OGG_NS, tags, "COMPOSER", "Composer");
        assertHasTag(AudioTaggerExtractorTest.OGG_NS, tags, "DISCNUMBER", "Discnumber");
        assertHasTag(AudioTaggerExtractorTest.OGG_NS, tags, "DATE", "2023");
        assertHasTag(AudioTaggerExtractorTest.OGG_NS, tags, "TRACKNUMBER", "1");
    }

    @Test
    void testFilledWav() throws IOException {
        var tags = tagsFromTestFile(AudioTaggerExtractorTest.WAV_EXTRACTOR, "wav/filled_tags.wav");
        assertHasTag(AudioTaggerExtractorTest.WAV_NS, tags, "TIT2", "Title");
        assertHasTag(AudioTaggerExtractorTest.WAV_NS, tags, "TPE1", "Artist");
        assertHasTag(AudioTaggerExtractorTest.WAV_NS, tags, "TALB", "Album");
        assertHasTag(AudioTaggerExtractorTest.WAV_NS, tags, "TCON", "Pop");
        assertHasTag(AudioTaggerExtractorTest.WAV_NS, tags, "COMM", "Comment");
        assertHasTag(AudioTaggerExtractorTest.WAV_NS, tags, "TPOS", "5");
        assertHasTag(AudioTaggerExtractorTest.WAV_NS, tags, "TPE2", "Album Artist");
        assertHasTag(AudioTaggerExtractorTest.WAV_NS, tags, "TCOM", "Composer");
        assertHasTag(AudioTaggerExtractorTest.WAV_NS, tags, "TYER", "2023");
        assertHasTag(AudioTaggerExtractorTest.WAV_NS, tags, "TRCK", "1");
    }

    @Test
    void testFilledM4a() throws IOException {
        var tags = tagsFromTestFile(AudioTaggerExtractorTest.M4A_EXTRACTOR, "m4a/filled_tags.m4a");
        assertHasTag(AudioTaggerExtractorTest.M4A_NS, tags, "_cnam", "Title");
        assertHasTag(AudioTaggerExtractorTest.M4A_NS, tags, "_cART", "Artist");
        assertHasTag(AudioTaggerExtractorTest.M4A_NS, tags, "_calb", "Album");
        assertHasTag(AudioTaggerExtractorTest.M4A_NS, tags, "gnre", "Pop");
        assertHasTag(AudioTaggerExtractorTest.M4A_NS, tags, "_ccmt", "Comment");
        assertHasTag(AudioTaggerExtractorTest.M4A_NS, tags, "disk", "5");
        assertHasTag(AudioTaggerExtractorTest.M4A_NS, tags, "aART", "Album Artist");
        assertHasTag(AudioTaggerExtractorTest.M4A_NS, tags, "_cwrt", "Composer");
        assertHasTag(AudioTaggerExtractorTest.M4A_NS, tags, "_cday", "2023");
        assertHasTag(AudioTaggerExtractorTest.M4A_NS, tags, "trkn", "1");
    }
}
