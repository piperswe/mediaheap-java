package net.mediaheap.database;

import lombok.Cleanup;
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

    @Getter
    private final @NonNull FilesTable files;
    @Getter
    private final @NonNull TagsTable tags;

    private DatabaseConnection(@NonNull Connection connection) {
        this.connection = connection;
        files = new FilesTable(this);
        tags = new TagsTable(this);
    }

    private static void execStmt(@NonNull Connection c, @NonNull String sql) throws SQLException {
        @Cleanup var stmt = c.createStatement();
        stmt.execute(sql);
    }

    public static @NonNull DatabaseConnection of(@NonNull Connection connection) throws SQLException {
        //language=SQLite
        execStmt(connection, "PRAGMA foreign_keys = ON;");
        //language=SQLite
        execStmt(connection, "PRAGMA journal_mode = WAL;");
        //language=SQLite
        execStmt(connection, "PRAGMA synchronous = NORMAL;");
        //language=SQLite
        execStmt(connection, "PRAGMA mmap_size = 30000000000;");
        //language=SQLite
        execStmt(connection, "PRAGMA page_size = 32768;");
        //language=SQLite
        execStmt(connection, "PRAGMA auto_vacuum = INCREMENTAL;");
        return new DatabaseConnection(connection);
    }

    public static @NonNull DatabaseConnection temporaryDatabase() throws SQLException {
        return of(DriverManager.getConnection("jdbc:sqlite:"));
    }

    public static @NonNull DatabaseConnection testConnection() throws SQLException {
        var conn = temporaryDatabase();
        conn.runMigrations();
        return conn;
    }

    public static @NonNull DatabaseConnection localConnection() throws SQLException {
        return of(DriverManager.getConnection("jdbc:sqlite:mediaheap.sqlite"));
    }

    public MigrateResult runMigrations() {
        var flyway = Flyway.configure().dataSource(new SingleConnectionDataSource(connection)).locations("classpath:/net/mediaheap/database/migrations").load();
        return flyway.migrate();
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }
}
