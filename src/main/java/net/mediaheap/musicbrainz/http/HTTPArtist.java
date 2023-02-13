package net.mediaheap.musicbrainz.http;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import net.mediaheap.musicbrainz.Artist;

import java.time.LocalDate;

record HTTPArtist(String[] isnis, String country, @SerializedName("begin-area") HTTPArea beginArea,
                  HTTPArea area, @SerializedName("life-span") HTTPLifeSpan lifeSpan,
                  @SerializedName("end-area") HTTPArea endArea, @SerializedName("sort-name") String sortName,
                  String type, String gender, String disambiguation, String name,
                  @SerializedName("type-id") String typeId, @SerializedName("gender-id") String genderId,
                  String id, String[] ipis) {
    @NonNull Artist toArtist() {
        String areaId = null;
        var area = area();
        if (area != null) {
            areaId = area.id();
        }
        String beginAreaId = null;
        var beginArea = beginArea();
        if (beginArea != null) {
            beginAreaId = beginArea.id();
        }
        String endAreaId = null;
        var endArea = endArea();
        if (endArea != null) {
            endAreaId = endArea.id();
        }
        String begin = null;
        String end = null;
        String ended = null;
        var lifespan = lifeSpan();
        if (lifespan != null) {
            begin = lifespan.begin();
            end = lifespan.end();
            ended = lifespan.ended() ? "true" : "false";
        }
        String isni = null;
        String[] isnis = isnis();
        if (isnis.length > 0) {
            isni = isnis[0];
        }
        return new Artist(
                LocalDate.now().toString(),
                id(),
                name(),
                sortName(),
                disambiguation(),
                isni,
                country(),
                areaId,
                beginAreaId,
                endAreaId,
                begin,
                end,
                ended
        );
    }
}
