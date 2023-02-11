package net.mediaheap.musicbrainz;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import org.musicbrainz.model.entity.ArtistWs2;
import org.musicbrainz.model.entity.RecordingWs2;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Recording {
    private String fetched;
    private String id;
    private String title;
    private String length;
    private String firstReleaseDate;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getFirstReleaseDate() {
        return firstReleaseDate;
    }

    public void setFirstReleaseDate(String firstReleaseDate) {
        this.firstReleaseDate = firstReleaseDate;
    }

    public List<MediaHeapTag> getTags(MediaHeapFile file, String namespace) {
        return Arrays.asList(
                new MediaHeapTag(file, namespace, "fetched", getFetched()),
                new MediaHeapTag(file, namespace, "id", getId()),
                new MediaHeapTag(file, namespace, "title", getTitle()),
                new MediaHeapTag(file, namespace, "length", getLength()),
                new MediaHeapTag(file, namespace, "first-release-date", getFirstReleaseDate())
        );
    }

    public Recording() {}

    public Recording(RecordingWs2 ws2) {
        this.fetched = LocalDate.now().toString();
        this.id = ws2.getId();
        this.title = ws2.getTitle();
        this.length = ws2.getDuration();
        this.firstReleaseDate = ws2.getFirstReleaseDateStr();
    }
}
