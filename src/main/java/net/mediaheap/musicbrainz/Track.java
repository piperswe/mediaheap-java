package net.mediaheap.musicbrainz;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import org.musicbrainz.model.TrackWs2;
import org.musicbrainz.model.entity.ReleaseGroupWs2;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Track {
    private String fetched;
    private String id;
    private String position;
    private String number;

    public String getFetched() {
        return fetched;
    }

    public void setFetched(String fetched) {
        this.fetched = fetched;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<MediaHeapTag> getTags(MediaHeapFile file, String namespace) {
        return Arrays.asList(
                new MediaHeapTag(file, namespace, "fetched", getFetched()),
                new MediaHeapTag(file, namespace, "id", getId()),
                new MediaHeapTag(file, namespace, "position", getPosition()),
                new MediaHeapTag(file, namespace, "number", getNumber())
        );
    }

    public Track() {}

    public Track(TrackWs2 ws2) {
        this.fetched = LocalDate.now().toString();
        this.id = ws2.getId();
        this.position = String.valueOf(ws2.getPosition());
        this.number = String.valueOf(ws2.getNumber());
    }
}
