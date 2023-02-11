package net.mediaheap.musicbrainz;

import lombok.NonNull;
import lombok.Value;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.model.MediaHeapTagListFactory;
import org.musicbrainz.model.TrackWs2;

import java.time.LocalDate;
import java.util.List;

@Value(staticConstructor = "of")
public class Track {
    String fetched;
    String id;
    String position;
    String number;

    public static @NonNull Track fromWs2(@NonNull TrackWs2 ws2) {
        return of(
                LocalDate.now().toString(),
                ws2.getId(),
                String.valueOf(ws2.getPosition()),
                String.valueOf(ws2.getNumber())
        );
    }

    public List<MediaHeapTag> getTags(@NonNull MediaHeapFile file, @NonNull String namespace) {
        return MediaHeapTagListFactory.start(file, namespace)
                .add("fetched", getFetched())
                .add("id", getId())
                .add("position", getPosition())
                .add("number", getNumber())
                .build();
    }
}
