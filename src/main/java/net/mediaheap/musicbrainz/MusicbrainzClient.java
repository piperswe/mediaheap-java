package net.mediaheap.musicbrainz;

public interface MusicbrainzClient {
    Artist getArtist(String id) throws Exception;
    Recording getRecording(String id) throws Exception;
    Release getRelease(String id) throws Exception;
    ReleaseGroup getReleaseGroup(String id) throws Exception;
    Track getTrack(String releaseId, String trackId) throws Exception;
    Work getWork(String id) throws Exception;
}
