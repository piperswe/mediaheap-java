package net.mediaheap.database;

import com.google.common.util.concurrent.AbstractScheduledService;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.extern.flogger.Flogger;

import java.time.Duration;

@Flogger
public class DatabaseOptimizeService extends AbstractScheduledService {
    private final DatabaseConnection db;

    public DatabaseOptimizeService(@NonNull DatabaseConnection db) {
        this.db = db;
    }

    @Override
    protected void runOneIteration() throws Exception {
        log.atInfo().log("Optimizing database...");
        @Cleanup var stmt = db.getConnection().createStatement();
        stmt.execute("PRAGMA optimize;");
        log.atInfo().log("Optimized!");
    }

    @Override
    protected @NonNull Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(Duration.ZERO, Duration.ofHours(1));
    }
}
