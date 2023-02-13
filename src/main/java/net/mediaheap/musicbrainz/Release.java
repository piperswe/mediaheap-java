package net.mediaheap.musicbrainz;

import lombok.NonNull;
import net.mediaheap.model.GenericTagConvertible;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;

import java.util.List;

public record Release(String fetched, String id, String title, String status, String quality, String language,
                      String script, String date, String country) implements GenericTagConvertible {
    public @NonNull List<MediaHeapTag> getTags(@NonNull MediaHeapFile file, @NonNull String namespace) {
        return MediaHeapTagListFactory.start(file, namespace)
                .add("fetched", fetched())
                .add("id", id())
                .add("title", title())
                .add("status", status())
                .add("quality", quality())
                .add("language", language())
                .add("script", script())
                .add("date", date())
                .add("country", country())
                .build();
    }
}
