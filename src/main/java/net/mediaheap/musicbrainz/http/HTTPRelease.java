package net.mediaheap.musicbrainz.http;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import net.mediaheap.musicbrainz.Release;

import java.time.LocalDate;

// TODO: cover-art-archive
record HTTPRelease(String asin, String date, String quality, String country,
                   @SerializedName("release-events") HTTPReleaseEvent[] releaseEvents, String packaging,
                   String title, String id, String status, @SerializedName("packaging-id") String packagingId,
                   HTTPMedium[] media, String barcode, String disambiguation,
                   @SerializedName("status-id") String statusId,
                   @SerializedName("text-representation") HTTPTextRepresentation textRepresentation) {
    @NonNull Release toRelease() {
        return Release.of(
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
}
