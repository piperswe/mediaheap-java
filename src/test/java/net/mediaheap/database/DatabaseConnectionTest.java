package net.mediaheap.database;

import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseConnectionTest {
    @Test
    void inMemory() throws SQLException {
        var db = DatabaseConnection.inMemory();
        db.close();
    }

    @Test
    void runMigrations() throws SQLException {
        @Cleanup var db = DatabaseConnection.inMemory();
        var result = db.runMigrations();
        assertTrue(result.success);
        assertTrue(result.migrations.size() > 0);
        assertFalse(db.getConnection().isClosed(), "Database was closed immediately after being created");
    }

    @Test
    void testConnection() throws SQLException {
        @Cleanup var db = DatabaseConnection.testConnection();
        assertFalse(db.getConnection().isClosed(), "Database was closed immediately after being created");
    }
}