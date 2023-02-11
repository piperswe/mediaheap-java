package net.mediaheap.importer;

import net.mediaheap.database.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static net.mediaheap.importer.TestHelpers.assertHasTag;
import static net.mediaheap.importer.TestHelpers.testFilePath;
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
    void getPathMimeType() {
    }

    @Test
    void importFromWithMimeType() throws SQLException, IOException {
        var file = importer.importFromWithMimeType(testFilePath("flac/filled_tags.flac"), "audio/flac");
        assertNotNull(file);
        assertTrue(file.getPath().endsWith("filled_tags.flac"));
        assertEquals("audio/flac", file.getFileType());
        assertEquals("3f7a61ec2b8d8953605fc578625a483713252832530ecf3c3de34d75a0c762c0", file.getSha512Hash());
        var tags = db.getTags().getTagsForFile(file);
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "TITLE", "Title");

        file = importer.importFromWithMimeType(testFilePath("flac/filled_tags.flac"), "audio/not-flac");
        assertNotNull(file);
        assertTrue(file.getPath().endsWith("filled_tags.flac"));
        assertEquals("audio/not-flac", file.getFileType());
    }

    @Test
    void importFrom() throws SQLException, IOException {
        var file = importer.importFrom(testFilePath("flac/filled_tags.flac"));
        assertNotNull(file);
        assertTrue(file.getPath().endsWith("filled_tags.flac"));
        assertTrue(file.getFileType().equals("audio/x-flac") || file.getFileType().equals("audio/flac"), String.format("MIME type %s doesn't seem to be FLAC", file.getFileType()));
        assertEquals("3f7a61ec2b8d8953605fc578625a483713252832530ecf3c3de34d75a0c762c0", file.getSha512Hash());
        var tags = db.getTags().getTagsForFile(file);
        assertHasTag(AudioTaggerExtractorTest.FLAC_NS, tags, "TITLE", "Title");

        file = importer.importFrom(testFilePath("mp3/Study and Relax.mp3"));
        assertNotNull(file);
        assertTrue(file.getPath().endsWith("Study and Relax.mp3"));
        assertEquals("audio/mpeg", file.getFileType());
        assertEquals("ea519c62546258f788e2e2f1b795d29cdc6b68aebb0bf2132a1c3e4e0c89c9a0", file.getSha512Hash());
        tags = db.getTags().getTagsForFile(file);
        assertHasTag(MP3Extractor.V2NS, tags, "TP1", "Kevin MacLeod");
    }
}