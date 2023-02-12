package net.mediaheap.musicbrainz;

import lombok.NonNull;
import lombok.Value;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;

import java.util.List;

@Value(staticConstructor = "of")
public class Recording {
    String fetched;
    String id;
    String title;
    String length;
    String firstReleaseDate;

    public List<MediaHeapTag> getTags(@NonNull MediaHeapFile file, @NonNull String namespace) {
        return MediaHeapTagListFactory.start(file, namespace)
                .add("fetched", getFetched())
                .add("id", getId())
                .add("title", getTitle())
                .add("length", getLength())
                .add("first-release-date", getFirstReleaseDate())
                .build();
    }
}
