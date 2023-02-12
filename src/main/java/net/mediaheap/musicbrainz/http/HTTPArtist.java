package net.mediaheap.musicbrainz.http;

import com.google.gson.annotations.SerializedName;

public record HTTPArtist(String[] isnis, String country, @SerializedName("begin-area") HTTPArea beginArea,
                         HTTPArea area, @SerializedName("life-span") HTTPLifeSpan lifeSpan,
                         @SerializedName("end-area") HTTPArea endArea, @SerializedName("sort-name") String sortName,
                         String type, String gender, String disambiguation, String name,
                         @SerializedName("type-id") String typeId, @SerializedName("gender-id") String genderId,
                         String id, String[] ipis) {
}
