package net.mediaheap.musicbrainz.http;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import net.mediaheap.musicbrainz.ReleaseGroup;

import java.time.LocalDate;

record HTTPReleaseGroup(String id, String title, @SerializedName("primary-type-id") String primaryTypeId,
                        @SerializedName("first-release-date") String firstReleaseDate, String disambiguation,
                        @SerializedName("secondary-type-ids") String[] secondaryTypeIds,
                        @SerializedName("primary-type") String primaryType,
                        @SerializedName("secondary-types") String[] secondaryTypes) {
    @NonNull ReleaseGroup toReleaseGroup() {
        return ReleaseGroup.of(
                LocalDate.now().toString(),
                id(),
                title(),
                firstReleaseDate(),
                primaryType()
        );
    }
}
