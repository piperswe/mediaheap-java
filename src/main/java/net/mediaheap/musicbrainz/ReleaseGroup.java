package net.mediaheap.musicbrainz;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import org.musicbrainz.model.entity.RecordingWs2;
import org.musicbrainz.model.entity.ReleaseGroupWs2;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class ReleaseGroup {
    private String fetched;
    private String id;
    private String title;
    private String firstReleaseDate;
    private String primaryType;

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

    public String getFirstReleaseDate() {
        return firstReleaseDate;
    }

    public void setFirstReleaseDate(String firstReleaseDate) {
        this.firstReleaseDate = firstReleaseDate;
    }

    public String getPrimaryType() {
        return primaryType;
    }

    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    public List<MediaHeapTag> getTags(MediaHeapFile file, String namespace) {
        return Arrays.asList(
                new MediaHeapTag(file, namespace, "fetched", getFetched()),
                new MediaHeapTag(file, namespace, "id", getId()),
                new MediaHeapTag(file, namespace, "title", getTitle()),
                new MediaHeapTag(file, namespace, "first-release-date", getFirstReleaseDate()),
                new MediaHeapTag(file, namespace, "primary-type", getPrimaryType())
        );
    }

    public ReleaseGroup() {}

    public ReleaseGroup(ReleaseGroupWs2 ws2) {
        this.fetched = LocalDate.now().toString();
        this.id = ws2.getId();
        this.title = ws2.getTitle();
        this.firstReleaseDate = ws2.getFirstReleaseDateStr();
        this.primaryType = ws2.getPrimaryType();
    }
}
