package net.mediaheap.musicbrainz;

import lombok.NonNull;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;

import java.util.List;

public record ReleaseGroup(String fetched, String id, String title, String firstReleaseDate, String primaryType) {
    public List<MediaHeapTag> getTags(@NonNull MediaHeapFile file, @NonNull String namespace) {
        return MediaHeapTagListFactory.start(file, namespace)
                .add("fetched", fetched())
                .add("id", id())
                .add("title", title())
                .add("first-release-date", firstReleaseDate())
                .add("primary-type", primaryType())
                .build();
    }
}
