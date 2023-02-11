package net.mediaheap.musicbrainz;

import net.mediaheap.model.GenericTagConvertible;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import org.musicbrainz.model.entity.ArtistWs2;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Artist implements GenericTagConvertible {
    private String fetched;
    private String id;
    private String name;
    private String sortName;
    private String disambiguation;
    private String isni;
    private String country;
    private String areaId;
    private String beginAreaId;
    private String endAreaId;
    private String begin;
    private String end;
    private String ended;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getDisambiguation() {
        return disambiguation;
    }

    public void setDisambiguation(String disambiguation) {
        this.disambiguation = disambiguation;
    }

    public String getIsni() {
        return isni;
    }

    public void setIsni(String isni) {
        this.isni = isni;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getBeginAreaId() {
        return beginAreaId;
    }

    public void setBeginAreaId(String beginAreaId) {
        this.beginAreaId = beginAreaId;
    }

    public String getEndAreaId() {
        return endAreaId;
    }

    public void setEndAreaId(String endAreaId) {
        this.endAreaId = endAreaId;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEnded() {
        return ended;
    }

    public void setEnded(String ended) {
        this.ended = ended;
    }

    public List<MediaHeapTag> getTags(MediaHeapFile file, String namespace) {
        return Arrays.asList(
                new MediaHeapTag(file, namespace, "fetched", getFetched()),
                new MediaHeapTag(file, namespace, "id", getId()),
                new MediaHeapTag(file, namespace, "name", getName()),
                new MediaHeapTag(file, namespace, "sort-name", getSortName()),
                new MediaHeapTag(file, namespace, "disambiguation", getDisambiguation()),
                new MediaHeapTag(file, namespace, "isni", getIsni()),
                new MediaHeapTag(file, namespace, "country", getCountry()),
                new MediaHeapTag(file, namespace, "area-id", getAreaId()),
                new MediaHeapTag(file, namespace, "begin-area-id", getBeginAreaId()),
                new MediaHeapTag(file, namespace, "end-area-id", getEndAreaId()),
                new MediaHeapTag(file, namespace, "begin", getBegin()),
                new MediaHeapTag(file, namespace, "end", getEnd()),
                new MediaHeapTag(file, namespace, "ended", getEnded())
        );
    }

    public Artist() {}

    public Artist(ArtistWs2 ws2) {
        this.fetched = LocalDate.now().toString();
        this.id = ws2.getId();
        this.name = ws2.getName();
        this.sortName = ws2.getSortName();
        this.disambiguation = ws2.getDisambiguation();
        this.isni = ws2.getDisplayIsni();
        this.country = ws2.getCountry();
        var area = ws2.getArea();
        if (area != null) {
            this.areaId = area.getId();
        }
        var beginArea = ws2.getBeginArea();
        if (beginArea != null) {
            this.beginAreaId = beginArea.getId();
        }
        var endArea = ws2.getEndArea();
        if (endArea != null) {
            this.endAreaId = endArea.getId();
        }
        var lifespan = ws2.getLifeSpan();
        if (lifespan != null) {
            this.begin = lifespan.getBegin();
            this.end = lifespan.getEnd();
            this.ended = lifespan.getEnded() ? "true" : "false";
        }
    }
}
