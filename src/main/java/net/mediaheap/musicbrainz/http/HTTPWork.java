package net.mediaheap.musicbrainz.http;

import com.google.gson.annotations.SerializedName;
import lombok.NonNull;
import net.mediaheap.musicbrainz.Work;

import java.time.LocalDate;

record HTTPWork(String[] iswcs, String[] languages, String id, @SerializedName("type-id") String typeId,
                String type, String[] attributes, String disambiguation, String title, String language) {
    @NonNull Work toWork() {
        String iswc = null;
        String[] iswcs = iswcs();
        if (iswcs.length > 0) {
            iswc = iswcs[0];
        }
        return Work.of(
                LocalDate.now().toString(),
                id(),
                title(),
                language(),
                iswc
        );
    }
}
