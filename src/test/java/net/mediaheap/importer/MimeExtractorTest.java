package net.mediaheap.importer;

import net.mediaheap.musicbrainz.MockMusicbrainzClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static net.mediaheap.importer.TestHelpers.assertHasTag;
import static net.mediaheap.importer.TestHelpers.tagsFromTestFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MimeExtractorTest {
    private MimeExtractor extractor;

    @BeforeEach
    public void setUp() {
        extractor = new MimeExtractor();
        extractor.setMusicbrainzExtractor(new MusicbrainzExtractor(new MockMusicbrainzClient()));
        extractor.registerBuiltinExtractors();
    }

    @Test
    void id3v2() throws IOException {
        var tags = tagsFromTestFile(extractor, "mp3/id3v2.mp3");
        assertHasTag(MP3Extractor.V2NS, tags, "TALB", "Album Title");
        assertHasTag(MP3Extractor.V2NS, tags, "TCON", "Pop");
        assertHasTag(MP3Extractor.V2NS, tags, "TIT2", "Track Title");
        assertHasTag(MP3Extractor.V2NS, tags, "TPE1", "Artist Name");
        assertHasTag(MP3Extractor.V2NS, tags, "TYER", "2023");
    }

    @Test
    void flac() throws IOException {
        var tags = tagsFromTestFile(extractor, "flac/filled_tags.flac");
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
    void ogg() throws IOException {
        var tags = tagsFromTestFile(extractor, "ogg/filled_tags.ogg");
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
    void wav() throws IOException {
        var tags = tagsFromTestFile(extractor, "wav/filled_tags.wav");
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
    void m4a() throws IOException {
        var tags = tagsFromTestFile(extractor, "m4a/filled_tags.m4a");
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

    @Test
    void musicbrainz() throws IOException {
        var tags = tagsFromTestFile(extractor, "call_me_what_you_like.flac");
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "MUSICBRAINZ_ARTISTID", "deefd67b-c917-4b0d-ab48-c5af7f92cd82");
        assertHasTag("https://schemas.mediaheap.net/musicbrainz/track/album", tags, "title", "Call Me What You Like");
        assertHasTag("https://schemas.mediaheap.net/musicbrainz/track/album-artist", tags, "name", "Lovejoy");
        assertHasTag("https://schemas.mediaheap.net/musicbrainz/track/artist", tags, "name", "Lovejoy");
        assertHasTag("https://schemas.mediaheap.net/musicbrainz/track/recording", tags, "title", "Call Me What You Like");
        assertHasTag("https://schemas.mediaheap.net/musicbrainz/track/release-group", tags, "title", "Call Me What You Like");
        assertHasTag("https://schemas.mediaheap.net/musicbrainz/track/work", tags, "title", "Call Me What You Like");
        assertHasTag("https://schemas.mediaheap.net/musicbrainz/track", tags, "number", "1");
    }

    @Test
    void noExtractor() throws IOException {
        var tags = tagsFromTestFile(extractor, "example.aup3");
        assertEquals(0, tags.size());
    }
}
