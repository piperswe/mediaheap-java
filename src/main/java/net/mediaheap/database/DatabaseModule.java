package net.mediaheap.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import lombok.NonNull;

public class DatabaseModule extends AbstractModule {
    @NonNull
    private final DatabaseConnection c;

    DatabaseModule(@NonNull DatabaseConnection c) {
        this.c = c;
    }

    @Provides
    @NonNull
    DatabaseConnection provideDatabaseConnection() {
        return c;
    }
}
