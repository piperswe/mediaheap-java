package net.mediaheap.musicbrainz;

import net.mediaheap.model.GenericTagConvertible;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import org.musicbrainz.model.entity.ReleaseGroupWs2;
import org.musicbrainz.model.entity.ReleaseWs2;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Release implements GenericTagConvertible {
    private String fetched;
    private String id;
    private String title;
    private String status;
    private String quality;
    private String language;
    private String script;
    private String date;
    private String country;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<MediaHeapTag> getTags(MediaHeapFile file, String namespace) {
        return Arrays.asList(
                new MediaHeapTag(file, namespace, "fetched", getFetched()),
                new MediaHeapTag(file, namespace, "id", getId()),
                new MediaHeapTag(file, namespace, "title", getTitle()),
                new MediaHeapTag(file, namespace, "status", getStatus()),
                new MediaHeapTag(file, namespace, "quality", getQuality()),
                new MediaHeapTag(file, namespace, "language", getLanguage()),
                new MediaHeapTag(file, namespace, "script", getScript()),
                new MediaHeapTag(file, namespace, "date", getDate()),
                new MediaHeapTag(file, namespace, "country", getCountry())
        );
    }

    public Release() {}

    public Release(ReleaseWs2 ws2) {
        this.fetched = LocalDate.now().toString();
        this.id = ws2.getId();
        this.title = ws2.getTitle();
        this.status = ws2.getStatus();
        this.quality = ws2.getQualityStr();
        this.language = ws2.getTextLanguage();
        this.script = ws2.getTextScript();
        this.date = ws2.getDateStr();
        this.country = ws2.getCountryId();
    }
}
