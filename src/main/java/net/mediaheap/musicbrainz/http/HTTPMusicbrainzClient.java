package net.mediaheap.musicbrainz.http;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import lombok.NonNull;
import net.mediaheap.musicbrainz.*;

import java.net.http.HttpClient;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class HTTPMusicbrainzClient implements MusicbrainzClient {
    final HttpClient client = HttpClient.newHttpClient();
    final Gson gson = new Gson();
    private final LoadingCache<String, Optional<HTTPArtist>> artistCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new HTTPMusicbrainzCacheLoader<>(
                    (id) -> String.format("https://musicbrainz.org/ws/2/artist/%s?fmt=json", id),
                    () -> this.client,
                    () -> this.gson,
                    HTTPArtist.class
            ));
    private final LoadingCache<String, Optional<HTTPRecording>> recordingCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new HTTPMusicbrainzCacheLoader<>(
                    (id) -> String.format("https://musicbrainz.org/ws/2/recording/%s?fmt=json", id),
                    () -> this.client,
                    () -> this.gson,
                    HTTPRecording.class
            ));
    private final LoadingCache<String, Optional<HTTPRelease>> releaseCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new HTTPMusicbrainzCacheLoader<>(
                    (id) -> String.format("https://musicbrainz.org/ws/2/release/%s?fmt=json&inc=recordings", id),
                    () -> this.client,
                    () -> this.gson,
                    HTTPRelease.class
            ));
    private final LoadingCache<String, Optional<HTTPReleaseGroup>> releaseGroupCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new HTTPMusicbrainzCacheLoader<>(
                    (id) -> String.format("https://musicbrainz.org/ws/2/release-group/%s?fmt=json", id),
                    () -> this.client,
                    () -> this.gson,
                    HTTPReleaseGroup.class
            ));
    private final LoadingCache<String, Optional<HTTPWork>> workCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .build(new HTTPMusicbrainzCacheLoader<>(
                    (id) -> String.format("https://musicbrainz.org/ws/2/work/%s?fmt=json", id),
                    () -> this.client,
                    () -> this.gson,
                    HTTPWork.class
            ));

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
        return releaseCache.get(releaseId).flatMap((r) -> r.getTrackById(trackId)).map(HTTPTrack::toTrack);
    }

    @Override
    public @NonNull Optional<Work> getWork(@NonNull String id) throws ExecutionException {
        return workCache.get(id).map(HTTPWork::toWork);
    }
}
