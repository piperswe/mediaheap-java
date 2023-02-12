package net.mediaheap.musicbrainz;

import lombok.NonNull;
import lombok.Value;
import net.mediaheap.model.GenericTagConvertible;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;
import net.mediaheap.musicbrainz.http.HTTPArtist;

import java.time.LocalDate;
import java.util.List;

@Value(staticConstructor = "of")
public class Artist implements GenericTagConvertible {
    String fetched;
    String id;
    String name;
    String sortName;
    String disambiguation;
    String isni;
    String country;
    String areaId;
    String beginAreaId;
    String endAreaId;
    String begin;
    String end;
    String ended;

    public static @NonNull Artist fromHttp(@NonNull HTTPArtist http) {
        String areaId = null;
        var area = http.area();
        if (area != null) {
            areaId = area.id();
        }
        String beginAreaId = null;
        var beginArea = http.beginArea();
        if (beginArea != null) {
            beginAreaId = beginArea.id();
        }
        String endAreaId = null;
        var endArea = http.endArea();
        if (endArea != null) {
            endAreaId = endArea.id();
        }
        String begin = null;
        String end = null;
        String ended = null;
        var lifespan = http.lifeSpan();
        if (lifespan != null) {
            begin = lifespan.begin();
            end = lifespan.end();
            ended = lifespan.ended() ? "true" : "false";
        }
        String isni = null;
        if (http.isnis().length > 0) {
            isni = http.isnis()[0];
        }
        return of(
                LocalDate.now().toString(),
                http.id(),
                http.name(),
                http.sortName(),
                http.disambiguation(),
                isni,
                http.country(),
                areaId,
                beginAreaId,
                endAreaId,
                begin,
                end,
                ended
        );
    }

    public List<MediaHeapTag> getTags(@NonNull MediaHeapFile file, @NonNull String namespace) {
        return MediaHeapTagListFactory.start(file, namespace)
                .add("fetched", getFetched())
                .add("name", getName())
                .add("sort-name", getSortName())
                .add("disambiguation", getDisambiguation())
                .add("isni", getIsni())
                .add("country", getCountry())
                .add("area-id", getAreaId())
                .add("begin-area-id", getBeginAreaId())
                .add("end-area-id", getEndAreaId())
                .add("begin", getBegin())
                .add("end", getEnd())
                .add("ended", getEnded())
                .build();
    }
}
