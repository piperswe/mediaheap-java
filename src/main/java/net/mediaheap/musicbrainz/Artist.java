package net.mediaheap.musicbrainz;

import lombok.NonNull;
import net.mediaheap.model.GenericTagConvertible;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;

import java.util.List;

public record Artist(String fetched, String id, String name, String sortName, String disambiguation, String isni,
                     String country, String areaId, String beginAreaId, String endAreaId, String begin, String end,
                     String ended) implements GenericTagConvertible {
    public @NonNull List<MediaHeapTag> getTags(@NonNull MediaHeapFile file, @NonNull String namespace) {
        return MediaHeapTagListFactory.start(file, namespace)
                .add("fetched", fetched())
                .add("name", name())
                .add("sort-name", sortName())
                .add("disambiguation", disambiguation())
                .add("isni", isni())
                .add("country", country())
                .add("area-id", areaId())
                .add("begin-area-id", beginAreaId())
                .add("end-area-id", endAreaId())
                .add("begin", begin())
                .add("end", end())
                .add("ended", ended())
                .build();
    }
}
