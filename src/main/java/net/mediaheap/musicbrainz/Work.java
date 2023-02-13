package net.mediaheap.musicbrainz;

import lombok.NonNull;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;

import java.util.List;

public record Work(String fetched, String id, String title, String language, String iswc) {
    public List<MediaHeapTag> getTags(@NonNull MediaHeapFile file, @NonNull String namespace) {
        return MediaHeapTagListFactory.start(file, namespace)
                .add("fetched", fetched())
                .add("id", id())
                .add("title", title())
                .add("language", language())
                .add("iswc", iswc())
                .build();
    }
}
