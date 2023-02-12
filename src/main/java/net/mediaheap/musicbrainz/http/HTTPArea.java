package net.mediaheap.musicbrainz.http;

import com.google.gson.annotations.SerializedName;

public record HTTPArea(String id, @SerializedName("type-id") String typeId, String disambiguation, String name,
                       String type, @SerializedName("sort-name") String sortName,
                       @SerializedName("iso-3166-2-codes") String[] iso31662Codes) {
}
