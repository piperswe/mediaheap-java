package net.mediaheap.musicbrainz;

import lombok.NonNull;
import lombok.Value;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;
import org.musicbrainz.model.entity.RecordingWs2;

import java.time.LocalDate;
import java.util.List;

@Value(staticConstructor = "of")
public class Recording {
    String fetched;
    String id;
    String title;
    String length;
    String firstReleaseDate;

    public static @NonNull Recording fromWs2(@NonNull RecordingWs2 ws2) {
        return of(
                LocalDate.now().toString(),
                ws2.getId(),
                ws2.getTitle(),
                String.valueOf(ws2.getDurationInMillis() / 1000),
                ws2.getFirstReleaseDateStr()
        );
    }

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
