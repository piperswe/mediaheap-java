package net.mediaheap.musicbrainz.http;

import com.google.gson.Gson;
import lombok.NonNull;
import net.mediaheap.musicbrainz.*;
import org.musicbrainz.MBWS2Exception;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class HTTPMusicbrainzClient implements MusicbrainzClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    @Override
    public Artist getArtist(@NonNull String id) throws Exception {
        var url = String.format("https://musicbrainz.org/ws/2/artist/%s?fmt=json", id);
        var request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("User-Agent", "mediaheap-java ( https://github.com/piperswe/mediaheap-java )")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 404) {
            return null;
        } else if (response.statusCode() >= 300) {
            throw new Exception(String.format("Got status code %d", response.statusCode()));
        }
        var json = response.body();
        var artist = gson.fromJson(json, HTTPArtist.class);
        return Artist.fromHttp(artist);
    }

    @Override
    public Recording getRecording(@NonNull String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Recording("mediaheap-java", "", "https://github.com/piperswe/mediaheap-java");
        var recording = controller.getComplete(id);
        if (recording == null) {
            return null;
        } else {
            return Recording.fromWs2(recording);
        }
    }

    @Override
    public Release getRelease(@NonNull String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Release("mediaheap-java", "", "https://github.com/piperswe/mediaheap-java");
        var release = controller.getComplete(id);
        if (release == null) {
            return null;
        } else {
            return Release.fromWs2(release);
        }
    }

    @Override
    public ReleaseGroup getReleaseGroup(@NonNull String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.ReleaseGroup("mediaheap-java", "", "https://github.com/piperswe/mediaheap-java");
        var releaseGroup = controller.getComplete(id);
        if (releaseGroup == null) {
            return null;
        } else {
            return ReleaseGroup.fromWs2(releaseGroup);
        }
    }

    @Override
    public Track getTrack(@NonNull String releaseId, @NonNull String trackId) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Release("mediaheap-java", "", "https://github.com/piperswe/mediaheap-java");
        var release = controller.getComplete(releaseId);
        if (release != null) {
            var mediums = release.getMediumList();
            var tracks = mediums.getCompleteTrackList();
            for (var track : tracks) {
                if (track.getId().equals(trackId)) {
                    return Track.fromWs2(track);
                }
            }
        }
        return null;
    }

    @Override
    public Work getWork(@NonNull String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Work("mediaheap-java", "", "https://github.com/piperswe/mediaheap-java");
        var work = controller.getComplete(id);
        if (work == null) {
            return null;
        } else {
            return Work.fromWs2(work);
        }
    }
}
