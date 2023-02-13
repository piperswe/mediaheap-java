package net.mediaheap.importer;

import net.mediaheap.database.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.overviewproject.mime_types.GetBytesException;

import java.io.IOException;
import java.sql.SQLException;

import static net.mediaheap.importer.TestHelpers.*;
import static org.junit.jupiter.api.Assertions.*;

class ImporterTest {
    private DatabaseConnection db;
    private MimeExtractor extractor;
    private Importer importer;

    @BeforeEach
    void setUp() throws SQLException {
        db = DatabaseConnection.testConnection();
        extractor = new MimeExtractor();
        extractor.setMusicbrainzExtractor(new NullExtractor());
        extractor.registerBuiltinExtractors();
        importer = new Importer(db);
        importer.setExtractor(new IndirectExtractor(() -> this.extractor));
    }

    @AfterEach
    void tearDown() throws SQLException {
        db.close();
        db = null;
    }

    @Test
    void getPathMimeType() throws GetBytesException {
        assertIsOne(Importer.getPathMimeType(testFilePath("flac/filled_tags.flac")).orElse(null), "audio/flac", "audio/x-flac");
        assertIsOne(Importer.getPathMimeType(testFilePath("flac/missing_tags.flac")).orElse(null), "audio/flac", "audio/x-flac");
        assertIsOne(Importer.getPathMimeType(testFilePath("m4a/filled_tags.m4a")).orElse(null), "audio/mp4", "audio/m4a", "audio/x-m4a");
        assertIsOne(Importer.getPathMimeType(testFilePath("m4a/missing_tags.m4a")).orElse(null), "audio/mp4", "audio/m4a", "audio/x-m4a");
        assertIsOne(Importer.getPathMimeType(testFilePath("mp3/both.mp3")).orElse(null), "audio/mpeg", "audio/mp3");
        assertIsOne(Importer.getPathMimeType(testFilePath("mp3/id3v2.mp3")).orElse(null), "audio/mpeg", "audio/mp3");
        assertIsOne(Importer.getPathMimeType(testFilePath("mp3/missing_id3.mp3")).orElse(null), "audio/mpeg", "audio/mp3");
        assertIsOne(Importer.getPathMimeType(testFilePath("mp3/Study and Relax.mp3")).orElse(null), "audio/mpeg", "audio/mp3");
        assertIsOne(Importer.getPathMimeType(testFilePath("ogg/filled_tags.ogg")).orElse(null), "audio/ogg", "audio/x-vorbis+ogg");
        assertIsOne(Importer.getPathMimeType(testFilePath("ogg/missing_tags.ogg")).orElse(null), "audio/ogg", "audio/x-vorbis+ogg");
        assertIsOne(Importer.getPathMimeType(testFilePath("wav/missing_tags.wav")).orElse(null), "audio/wav", "audio/x-wav", "audio/vnd.wave");
        assertIsOne(Importer.getPathMimeType(testFilePath("wav/filled_tags.wav")).orElse(null), "audio/wav", "audio/x-wav", "audio/vnd.wave");
        assertIsOne(Importer.getPathMimeType(testFilePath("call_me_what_you_like.flac")).orElse(null), "audio/flac", "audio/x-flac");
    }

    @Test
    void importFromWithMimeType() throws SQLException, IOException {
        var file = importer.importFromWithMimeType(testFilePath("flac/filled_tags.flac"), "audio/flac");
        assertNotNull(file);
        assertTrue(file.path().endsWith("filled_tags.flac"));
        assertEquals("audio/flac", file.fileType());
        assertEquals("3f7a61ec2b8d8953605fc578625a483713252832530ecf3c3de34d75a0c762c0", file.sha512Hash());
        var tags = db.getTags().getTagsForFile(file);
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "TITLE", "Title");
        assertHasTag(ImportExtractor.NS, tags, "size", "59194");

        file = importer.importFromWithMimeType(testFilePath("flac/filled_tags.flac"), "audio/not-flac");
        assertNotNull(file);
        assertTrue(file.path().endsWith("filled_tags.flac"));
        assertEquals("audio/not-flac", file.fileType());
    }

    @Test
    void importFrom() throws SQLException, IOException, GetBytesException {
        var file = importer.importFrom(testFilePath("flac/filled_tags.flac"));
        assertNotNull(file);
        assertTrue(file.path().endsWith("filled_tags.flac"));
        assertTrue(file.fileType().equals("audio/x-flac") || file.fileType().equals("audio/flac"), String.format("MIME type %s doesn't seem to be FLAC", file.fileType()));
        assertEquals("b6af98087869f0ea1f011eb308cbe15eec596263c9b714cd4d0e1b10b04c547d", file.sha256Hash());
        assertEquals("3f7a61ec2b8d8953605fc578625a483713252832530ecf3c3de34d75a0c762c0", file.sha512Hash());
        assertEquals("69f54b1f49aef9dc92c8eb0337fba497", file.md5Hash());
        var tags = db.getTags().getTagsForFile(file);
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "TITLE", "Title");
        assertHasTag(ImportExtractor.NS, tags, "size", "59194");
        assertHasTag(ImportExtractor.NS, tags, "sha256", "b6af98087869f0ea1f011eb308cbe15eec596263c9b714cd4d0e1b10b04c547d");
        assertHasTag(ImportExtractor.NS, tags, "sha512", "3f7a61ec2b8d8953605fc578625a483713252832530ecf3c3de34d75a0c762c0");
        assertHasTag(ImportExtractor.NS, tags, "md5", "69f54b1f49aef9dc92c8eb0337fba497");

        file = importer.importFrom(testFilePath("mp3/Study and Relax.mp3"));
        assertNotNull(file);
        assertTrue(file.path().endsWith("Study and Relax.mp3"));
        assertEquals("audio/mpeg", file.fileType());
        assertEquals("ea519c62546258f788e2e2f1b795d29cdc6b68aebb0bf2132a1c3e4e0c89c9a0", file.sha512Hash());
        tags = db.getTags().getTagsForFile(file);
        assertHasTag(MP3Extractor.V2NS, tags, "TP1", "Kevin MacLeod");
        assertHasTag(ImportExtractor.NS, tags, "size", "9070686");
    }
}