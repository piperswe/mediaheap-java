package net.mediaheap.musicbrainz.http;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import net.mediaheap.musicbrainz.Recording;

import java.time.LocalDate;

record HTTPRecording(@SerializedName("first-release-date") String firstReleaseDate, long length, String title,
                     boolean video, String id, String disambiguation) {
    @NonNull Recording toRecording() {
        return Recording.of(
                LocalDate.now().toString(),
                id(),
                title(),
                String.valueOf(length()),
                firstReleaseDate()
        );
    }
}
