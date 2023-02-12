package net.mediaheap.musicbrainz;

import lombok.NonNull;
import lombok.Value;
import net.mediaheap.model.GenericTagConvertible;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;

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
