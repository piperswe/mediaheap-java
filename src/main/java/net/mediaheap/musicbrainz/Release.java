package net.mediaheap.musicbrainz;

import lombok.NonNull;
import lombok.Value;
import net.mediaheap.model.GenericTagConvertible;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;

import java.util.List;

@Value(staticConstructor = "of")
public class Release implements GenericTagConvertible {
    String fetched;
    String id;
    String title;
    String status;
    String quality;
    String language;
    String script;
    String date;
    String country;

    public @NonNull List<MediaHeapTag> getTags(@NonNull MediaHeapFile file, @NonNull String namespace) {
        return MediaHeapTagListFactory.start(file, namespace)
                .add("fetched", getFetched())
                .add("id", getId())
                .add("title", getTitle())
                .add("status", getStatus())
                .add("quality", getQuality())
                .add("language", getLanguage())
                .add("script", getScript())
                .add("date", getDate())
                .add("country", getCountry())
                .build();
    }
}
