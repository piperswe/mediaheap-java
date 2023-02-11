package net.mediaheap.database;

import net.mediaheap.model.MediaHeapTagListFactory;
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

    @Test
    void insertTags() throws SQLException {
        db.getFiles()
                .insertTags(
                        MediaHeapTagListFactory.start(-1, "ns")
                                .add("hello", "world")
                                .build()
                );
    }

    @Test
    void getTagsForFile() throws SQLException {
        db.getFiles()
                .insertTags(MediaHeapTagListFactory.start(-1, "ns").add("hello", "world").build());
        var tags = db.getFiles().getTagsForFile(-1);
        assertEquals(1, tags.size());
        assertEquals("ns", tags.get(0).getNamespace());
        assertEquals("hello", tags.get(0).getKey());
        assertEquals("world", tags.get(0).getValue());
    }
}