package net.mediaheap.musicbrainz.http;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import net.mediaheap.musicbrainz.Release;

import java.time.LocalDate;
import java.util.Optional;

// TODO: cover-art-archive
record HTTPRelease(String asin, String date, String quality, String country,
                   @SerializedName("release-events") HTTPReleaseEvent[] releaseEvents, String packaging,
                   String title, String id, String status, @SerializedName("packaging-id") String packagingId,
                   HTTPMedium[] media, String barcode, String disambiguation,
                   @SerializedName("status-id") String statusId,
                   @SerializedName("text-representation") HTTPTextRepresentation textRepresentation) {
    @NonNull Release toRelease() {
        return new Release(
                LocalDate.now().toString(),
                id(),
                title(),
                status(),
                quality(),
                textRepresentation().language(),
                textRepresentation().script(),
                date(),
                country()
        );
    }

    @NonNull Optional<HTTPTrack> getTrackById(@NonNull String id) {
        for (var medium : media()) {
            for (var track : medium.tracks()) {
                if (track.id().equals(id)) {
                    return Optional.of(track);
                }
            }
        }
        return Optional.empty();
    }
}
