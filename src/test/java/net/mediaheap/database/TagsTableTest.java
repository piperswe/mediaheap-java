package net.mediaheap.database;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTagListFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagsTableTest {
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
    void insertTags() throws SQLException {
        var file = db.getFiles().insertFile(MediaHeapFile.of("", "", "", "", null));
        db.getTags().insertTags(
                MediaHeapTagListFactory.start(file, "ns")
                        .add("hello", "world")
                        .build()
        );
    }

    @Test
    void getTagsForFile() throws SQLException {
        var file = db.getFiles().insertFile(MediaHeapFile.of("", "", "", "", null));
        db.getTags().insertTags(
                MediaHeapTagListFactory.start(file, "ns")
                        .add("hello", "world")
                        .build()
        );
        var tags = db.getTags().getTagsForFile(file);
        assertEquals(1, tags.size());
        assertEquals("ns", tags.get(0).namespace());
        assertEquals("hello", tags.get(0).key());
        assertEquals("world", tags.get(0).value());
    }
}