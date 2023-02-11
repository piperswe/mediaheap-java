package net.mediaheap.musicbrainz.http;

import lombok.NonNull;
import net.mediaheap.musicbrainz.*;
import org.musicbrainz.MBWS2Exception;

public class HTTPMusicbrainzClient implements MusicbrainzClient {
    @Override
    public Artist getArtist(@NonNull String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Artist();
        var artist = controller.getComplete(id);
        if (artist == null) {
            return null;
        } else {
            return Artist.fromWs2(artist);
        }
    }

    @Override
    public Recording getRecording(@NonNull String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Recording();
        var recording = controller.getComplete(id);
        if (recording == null) {
            return null;
        } else {
            return Recording.fromWs2(recording);
        }
    }

    @Override
    public Release getRelease(@NonNull String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Release();
        var release = controller.getComplete(id);
        if (release == null) {
            return null;
        } else {
            return Release.fromWs2(release);
        }
    }

    @Override
    public ReleaseGroup getReleaseGroup(@NonNull String id) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.ReleaseGroup();
        var releaseGroup = controller.getComplete(id);
        if (releaseGroup == null) {
            return null;
        } else {
            return ReleaseGroup.fromWs2(releaseGroup);
        }
    }

    @Override
    public Track getTrack(@NonNull String releaseId, @NonNull String trackId) throws MBWS2Exception {
        var controller = new org.musicbrainz.controller.Release();
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
        var controller = new org.musicbrainz.controller.Work();
        var work = controller.getComplete(id);
        if (work == null) {
            return null;
        } else {
            return Work.fromWs2(work);
        }
    }
}
