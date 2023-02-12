package net.mediaheap.musicbrainz.http;

import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import lombok.NonNull;
import net.mediaheap.musicbrainz.*;

import java.net.http.HttpClient;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class HTTPMusicbrainzClient implements MusicbrainzClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    private final LoadingCache<String, Optional<HTTPArtist>> artists = HTTPMusicbrainzRequester.createCache(
            (id) -> String.format("https://musicbrainz.org/ws/2/artist/%s?fmt=json", id),
            () -> this.client,
            () -> this.gson,
            HTTPArtist.class
    );
    private final LoadingCache<String, Optional<HTTPRecording>> recordings = HTTPMusicbrainzRequester.createCache(
            (id) -> String.format("https://musicbrainz.org/ws/2/recording/%s?fmt=json", id),
            () -> this.client,
            () -> this.gson,
            HTTPRecording.class
    );
    private final LoadingCache<String, Optional<HTTPRelease>> releases = HTTPMusicbrainzRequester.createCache(
            (id) -> String.format("https://musicbrainz.org/ws/2/release/%s?fmt=json&inc=recordings", id),
            () -> this.client,
            () -> this.gson,
            HTTPRelease.class
    );
    private final LoadingCache<String, Optional<HTTPReleaseGroup>> releaseGroups = HTTPMusicbrainzRequester.createCache(
            (id) -> String.format("https://musicbrainz.org/ws/2/release-group/%s?fmt=json", id),
            () -> this.client,
            () -> this.gson,
            HTTPReleaseGroup.class
    );
    private final LoadingCache<String, Optional<HTTPWork>> works = HTTPMusicbrainzRequester.createCache(
            (id) -> String.format("https://musicbrainz.org/ws/2/work/%s?fmt=json", id),
            () -> this.client,
            () -> this.gson,
            HTTPWork.class
    );

    @Override
    public @NonNull Optional<Artist> getArtist(@NonNull String id) throws ExecutionException {
        return artists.get(id).map(HTTPArtist::toArtist);
    }

    @Override
    public @NonNull Optional<Recording> getRecording(@NonNull String id) throws ExecutionException {
        return recordings.get(id).map(HTTPRecording::toRecording);
    }

    @Override
    public @NonNull Optional<Release> getRelease(@NonNull String id) throws ExecutionException {
        return releases.get(id).map(HTTPRelease::toRelease);
    }

    @Override
    public @NonNull Optional<ReleaseGroup> getReleaseGroup(@NonNull String id) throws ExecutionException {
        return releaseGroups.get(id).map(HTTPReleaseGroup::toReleaseGroup);
    }

    @Override
    public @NonNull Optional<Track> getTrack(@NonNull String releaseId, @NonNull String trackId) throws ExecutionException {
        return releases.get(releaseId).flatMap((r) -> r.getTrackById(trackId)).map(HTTPTrack::toTrack);
    }

    @Override
    public @NonNull Optional<Work> getWork(@NonNull String id) throws ExecutionException {
        return works.get(id).map(HTTPWork::toWork);
    }
}
