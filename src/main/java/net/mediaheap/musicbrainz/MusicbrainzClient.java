package net.mediaheap.musicbrainz;

import lombok.NonNull;

import java.util.Optional;

public interface MusicbrainzClient {
    @NonNull Optional<Artist> getArtist(@NonNull String id) throws Exception;

    @NonNull Optional<Recording> getRecording(@NonNull String id) throws Exception;

    @NonNull Optional<Release> getRelease(@NonNull String id) throws Exception;

    @NonNull Optional<ReleaseGroup> getReleaseGroup(@NonNull String id) throws Exception;

    @NonNull Optional<Track> getTrack(@NonNull String releaseId, @NonNull String trackId) throws Exception;

    @NonNull Optional<Work> getWork(@NonNull String id) throws Exception;
}
