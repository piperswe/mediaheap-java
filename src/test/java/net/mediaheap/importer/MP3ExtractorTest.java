package net.mediaheap.importer;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static net.mediaheap.importer.TestHelpers.assertHasTag;
import static net.mediaheap.importer.TestHelpers.tagsFromTestFile;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MP3ExtractorTest {
    @Test
    void missingId3() throws IOException {
        var tags = tagsFromTestFile(new MP3Extractor(), "mp3/missing_id3.mp3");
        assertTrue(tags.isEmpty());
    }

    @Test
    void id3v2() throws IOException {
        var tags = tagsFromTestFile(new MP3Extractor(), "mp3/id3v2.mp3");
        assertHasTag(MP3Extractor.V2NS, tags, "TALB", "Album Title");
        assertHasTag(MP3Extractor.V2NS, tags, "TCON", "Pop");
        assertHasTag(MP3Extractor.V2NS, tags, "TIT2", "Track Title");
        assertHasTag(MP3Extractor.V2NS, tags, "TPE1", "Artist Name");
        assertHasTag(MP3Extractor.V2NS, tags, "TYER", "2023");
    }

    @Test
    void both() throws IOException {
        var tags = tagsFromTestFile(new MP3Extractor(), "mp3/both.mp3");

        assertHasTag(MP3Extractor.V1NS, tags, "TALB", "Album");
        assertHasTag(MP3Extractor.V1NS, tags, "TCON", "Pop");
        assertHasTag(MP3Extractor.V1NS, tags, "TIT2", "Title");
        assertHasTag(MP3Extractor.V1NS, tags, "TPE1", "Artist");
        assertHasTag(MP3Extractor.V1NS, tags, "TYER", "2023");

        assertHasTag(MP3Extractor.V2NS, tags, "TALB", "Album");
        assertHasTag(MP3Extractor.V2NS, tags, "TCON", "Pop");
        assertHasTag(MP3Extractor.V2NS, tags, "TIT2", "Title");
        assertHasTag(MP3Extractor.V2NS, tags, "TPE1", "Artist");
        assertHasTag(MP3Extractor.V2NS, tags, "TYER", "2023");
    }

    @Test
    void real() throws IOException {
        var tags = tagsFromTestFile(new MP3Extractor(), "mp3/Study and Relax.mp3");

        assertHasTag(MP3Extractor.V1NS, tags, "TAL", "Royalty Free");
        assertHasTag(MP3Extractor.V1NS, tags, "TP1", "Kevin MacLeod");
        assertHasTag(MP3Extractor.V1NS, tags, "TYE", "2019");
        assertHasTag(MP3Extractor.V1NS, tags, "TSS", "Logic Pro X 10.4.7");

        assertHasTag(MP3Extractor.V2NS, tags, "TAL", "Royalty Free");
        assertHasTag(MP3Extractor.V2NS, tags, "TP1", "Kevin MacLeod");
        assertHasTag(MP3Extractor.V2NS, tags, "TYE", "2019");
        assertHasTag(MP3Extractor.V2NS, tags, "TSS", "Logic Pro X 10.4.7");
    }
}