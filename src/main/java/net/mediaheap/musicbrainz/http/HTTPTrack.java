package net.mediaheap.musicbrainz.http;

import lombok.NonNull;
import net.mediaheap.musicbrainz.Track;

import java.time.LocalDate;

record HTTPTrack(String title, long position, String id, HTTPRecording recording, Long length, String number) {
    @NonNull Track toTrack() {
        return new Track(
                LocalDate.now().toString(),
                id(),
                String.valueOf(position()),
                number()
        );
    }
}
