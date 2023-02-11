package net.mediaheap.musicbrainz;

import lombok.NonNull;
import lombok.Value;
import net.mediaheap.model.GenericTagConvertible;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;
import org.musicbrainz.model.entity.ArtistWs2;

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

    public static @NonNull Artist fromWs2(@NonNull ArtistWs2 ws2) {
        String areaId = null;
        var area = ws2.getArea();
        if (area != null) {
            areaId = area.getId();
        }
        String beginAreaId = null;
        var beginArea = ws2.getBeginArea();
        if (beginArea != null) {
            beginAreaId = beginArea.getId();
        }
        String endAreaId = null;
        var endArea = ws2.getEndArea();
        if (endArea != null) {
            endAreaId = endArea.getId();
        }
        String begin = null;
        String end = null;
        String ended = null;
        var lifespan = ws2.getLifeSpan();
        if (lifespan != null) {
            begin = lifespan.getBegin();
            end = lifespan.getEnd();
            ended = lifespan.getEnded() ? "true" : "false";
        }
        return of(
                LocalDate.now().toString(),
                ws2.getId(),
                ws2.getName(),
                ws2.getSortName(),
                ws2.getDisambiguation(),
                ws2.getDisplayIsni(),
                ws2.getCountry(),
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
