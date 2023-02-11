package net.mediaheap.musicbrainz;

import lombok.NonNull;

public interface MusicbrainzClient {
    Artist getArtist(@NonNull String id) throws Exception;

    Recording getRecording(@NonNull String id) throws Exception;

    Release getRelease(@NonNull String id) throws Exception;

    ReleaseGroup getReleaseGroup(@NonNull String id) throws Exception;

    Track getTrack(@NonNull String releaseId, @NonNull String trackId) throws Exception;

    Work getWork(@NonNull String id) throws Exception;
}
