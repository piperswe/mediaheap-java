package net.mediaheap.database;

import lombok.Getter;
import lombok.NonNull;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection implements AutoCloseable {
    @Getter
    private final @NonNull Connection connection;

    private DatabaseConnection(@NonNull Connection connection) {
        this.connection = connection;
    }

    public static @NonNull DatabaseConnection of(@NonNull Connection connection) {
        return new DatabaseConnection(connection);
    }

    public static @NonNull DatabaseConnection inMemory() throws SQLException {
        return of(DriverManager.getConnection("jdbc:sqlite::memory:"));
    }

    public static @NonNull DatabaseConnection testConnection() throws SQLException {
        var conn = inMemory();
        conn.runMigrations();
        return conn;
    }

    public MigrateResult runMigrations() {
        var flyway = Flyway.configure().dataSource(new SingleConnectionDataSource(connection)).locations("classpath:/net/mediaheap/database/migrations").load();
        return flyway.migrate();
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    public @NonNull TagsTable getFiles() {
        return new TagsTable(this);
    }
}
