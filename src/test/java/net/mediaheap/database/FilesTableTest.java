package net.mediaheap.database;

import net.mediaheap.model.MediaHeapFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FilesTableTest {
    private DatabaseConnection db;

    @BeforeEach
    void setUp() throws SQLException {
        db = DatabaseConnection.testConnection();
    }

    @AfterEach
    void tearDown() throws SQLException {
        db.close();
        db = null;
    }

    @Test
    void insertFile() throws SQLException {
        var file = db.getFiles().insertFile(MediaHeapFile.of("/", "sha256", "sha512", "md5", null));
        assertNotNull(file);
        assertEquals("/", file.path());
        assertEquals("sha256", file.sha256Hash());
        assertEquals("sha512", file.sha512Hash());
        assertEquals("md5", file.md5Hash());
        assertNull(file.fileType());
    }

    @Test
    void getFile() throws SQLException {
        var file = db.getFiles().insertFile(MediaHeapFile.of("/", "sha256", "sha512", "md5", "type"));
        assertNotNull(file);
        assertEquals("/", file.path());
        assertEquals("sha256", file.sha256Hash());
        assertEquals("sha512", file.sha512Hash());
        assertEquals("md5", file.md5Hash());
        assertEquals("type", file.fileType());
        file = db.getFiles().getFile(file.id());
        assertNotNull(file);
        assertEquals("/", file.path());
        assertEquals("sha256", file.sha256Hash());
        assertEquals("sha512", file.sha512Hash());
        assertEquals("md5", file.md5Hash());
        assertEquals("type", file.fileType());
    }
}