package net.mediaheap.musicbrainz.http;

import net.mediaheap.musicbrainz.*;
import org.musicbrainz.MBWS2Exception;

public class HTTPMusicbrainzClient implements MusicbrainzClient {
    @Override
    public Artist getArtist(String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Artist();
        var artist = controller.getComplete(id);
        if (artist == null) {
            return null;
        } else {
            return new Artist(artist);
        }
    }

    @Override
    public Recording getRecording(String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Recording();
        var recording = controller.getComplete(id);
        if (recording == null) {
            return null;
        } else {
            return new Recording(recording);
        }
    }

    @Override
    public Release getRelease(String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Release();
        var release = controller.getComplete(id);
        if (release == null) {
            return null;
        } else {
            return new Release(release);
        }
    }

    @Override
    public ReleaseGroup getReleaseGroup(String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.ReleaseGroup();
        var releaseGroup = controller.getComplete(id);
        if (releaseGroup == null) {
            return null;
        } else {
            return new ReleaseGroup(releaseGroup);
        }
    }

    @Override
    public Track getTrack(String releaseId, String trackId) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Release();
        var release = controller.getComplete(releaseId);
        if (release != null) {
            var mediums = release.getMediumList();
            var tracks = mediums.getCompleteTrackList();
            for (var track : tracks) {
                if (track.getId().equals(trackId)) {
                    return new Track(track);
                }
            }
        }
        return null;
    }

    @Override
    public Work getWork(String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Work();
        var work = controller.getComplete(id);
        if (work == null) {
            return null;
        } else {
            return new Work(work);
        }
    }
}
