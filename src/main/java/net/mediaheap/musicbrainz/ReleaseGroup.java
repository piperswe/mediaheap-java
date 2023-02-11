package net.mediaheap.musicbrainz;

import lombok.NonNull;
import lombok.Value;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;
import org.musicbrainz.model.entity.ReleaseGroupWs2;

import java.time.LocalDate;
import java.util.List;

@Value(staticConstructor = "of")
public class ReleaseGroup {
    String fetched;
    String id;
    String title;
    String firstReleaseDate;
    String primaryType;

    public static @NonNull ReleaseGroup fromWs2(@NonNull ReleaseGroupWs2 ws2) {
        return of(
                LocalDate.now().toString(),
                ws2.getId(),
                ws2.getTitle(),
                ws2.getFirstReleaseDateStr(),
                ws2.getPrimaryType()
        );
    }

    public List<MediaHeapTag> getTags(@NonNull MediaHeapFile file, @NonNull String namespace) {
        return MediaHeapTagListFactory.start(file, namespace)
                .add("fetched", getFetched())
                .add("id", getId())
                .add("title", getTitle())
                .add("first-release-date", getFirstReleaseDate())
                .add("primary-type", getPrimaryType())
                .build();
    }
}
