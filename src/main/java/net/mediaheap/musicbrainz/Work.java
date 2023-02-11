package net.mediaheap.musicbrainz;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import org.musicbrainz.model.entity.ReleaseWs2;
import org.musicbrainz.model.entity.WorkWs2;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Work {
    private String fetched;
    private String id;
    private String title;
    private String language;
    private String iswc;

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getIswc() {
        return iswc;
    }

    public void setIswc(String iswc) {
        this.iswc = iswc;
    }

    public List<MediaHeapTag> getTags(MediaHeapFile file, String namespace) {
        return Arrays.asList(
                new MediaHeapTag(file, namespace, "fetched", getFetched()),
                new MediaHeapTag(file, namespace, "id", getId()),
                new MediaHeapTag(file, namespace, "title", getTitle()),
                new MediaHeapTag(file, namespace, "language", getLanguage()),
                new MediaHeapTag(file, namespace, "iswc", getIswc())
        );
    }

    public Work() {}

    public Work(WorkWs2 ws2) {
        this.fetched = LocalDate.now().toString();
        this.id = ws2.getId();
        this.title = ws2.getTitle();
        this.language = ws2.getTextLanguage();
        this.iswc = ws2.getIswc();
    }
}
