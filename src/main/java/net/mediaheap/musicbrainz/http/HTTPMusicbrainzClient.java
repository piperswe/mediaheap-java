package net.mediaheap.musicbrainz.http;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import lombok.Cleanup;
import lombok.NonNull;
import net.mediaheap.musicbrainz.*;

import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class HTTPMusicbrainzClient implements MusicbrainzClient {
    final HttpClient client = HttpClient.newHttpClient();
    final Gson gson = new Gson();
    private final LoadingCache<String, Optional<HTTPArtist>> artistCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(new HTTPMusicbrainzCacheLoader<>(
                    (id) -> String.format("https://musicbrainz.org/ws/2/artist/%s?fmt=json", id),
                    HTTPArtist.class,
                    this
            ));
    private final LoadingCache<String, Optional<HTTPRecording>> recordingCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(new HTTPMusicbrainzCacheLoader<>(
                    (id) -> String.format("https://musicbrainz.org/ws/2/recording/%s?fmt=json", id),
                    HTTPRecording.class,
                    this
            ));
    private final LoadingCache<String, Optional<HTTPRelease>> releaseCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(new HTTPMusicbrainzCacheLoader<>((id) -> String.format(
                    "https://musicbrainz.org/ws/2/release/%s?fmt=json&inc=recordings", id),
                    HTTPRelease.class,
                    this
            ));
    private final LoadingCache<String, Optional<HTTPReleaseGroup>> releaseGroupCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(new HTTPMusicbrainzCacheLoader<>((id) -> String.format(
                    "https://musicbrainz.org/ws/2/release-group/%s?fmt=json", id),
                    HTTPReleaseGroup.class,
                    this
            ));
    private final LoadingCache<String, Optional<HTTPWork>> workCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(new HTTPMusicbrainzCacheLoader<>((id) -> String.format(
                    "https://musicbrainz.org/ws/2/work/%s?fmt=json", id),
                    HTTPWork.class,
                    this
            ));

    private static @NonNull HttpRequest newHttpRequest(@NonNull String url) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("User-Agent", "mediaheap-java ( https://github.com/piperswe/mediaheap-java )")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();
    }

    <T> @NonNull Optional<T> request(@NonNull String url, @NonNull Class<T> klass) throws Exception {
        var request = newHttpRequest(url);
        var response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() == 404) {
            return Optional.empty();
        } else if (response.statusCode() >= 300) {
            throw new Exception(String.format("Got status code %d", response.statusCode()));
        }
        @Cleanup var stream = response.body();
        @Cleanup var reader = new InputStreamReader(stream);
        return Optional.of(gson.fromJson(reader, klass));
    }

    @Override
    public @NonNull Optional<Artist> getArtist(@NonNull String id) throws ExecutionException {
        return artistCache.get(id).map(HTTPArtist::toArtist);
    }

    @Override
    public @NonNull Optional<Recording> getRecording(@NonNull String id) throws ExecutionException {
        return recordingCache.get(id).map(HTTPRecording::toRecording);
    }

    @Override
    public @NonNull Optional<Release> getRelease(@NonNull String id) throws ExecutionException {
        return releaseCache.get(id).map(HTTPRelease::toRelease);
    }

    @Override
    public @NonNull Optional<ReleaseGroup> getReleaseGroup(@NonNull String id) throws ExecutionException {
        return releaseGroupCache.get(id).map(HTTPReleaseGroup::toReleaseGroup);
    }

    @Override
    public @NonNull Optional<Track> getTrack(@NonNull String releaseId, @NonNull String trackId) throws ExecutionException {
        var releaseOption = releaseCache.get(releaseId);
        if (releaseOption.isPresent()) {
            var release = releaseOption.get();
            var media = release.media();
            for (var medium : media) {
                for (var track : medium.tracks()) {
                    if (track.id().equals(trackId)) {
                        return Optional.of(track.toTrack());
                    }
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public @NonNull Optional<Work> getWork(@NonNull String id) throws ExecutionException {
        return workCache.get(id).map(HTTPWork::toWork);
    }
}
