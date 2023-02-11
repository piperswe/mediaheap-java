package net.mediaheap.importer;

import lombok.Cleanup;
import lombok.NonNull;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import net.mediaheap.musicbrainz.MusicbrainzClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class MusicbrainzExtractor implements Extractor {
    @NonNull
    private final MusicbrainzClient client;

    public MusicbrainzExtractor(@NonNull MusicbrainzClient client) {
        this.client = client;
    }

    private @NonNull List<MediaHeapTag> extractAlbum(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws Exception {
        var id = MediaHeapTag.findTagValueInNamespaces(existingTags, AudioTaggerExtractor.AUDIO_NAMESPACES, "musicbrainz_albumid");
        if (id == null) {
            return Collections.emptyList();
        }
        var release = client.getRelease(id);
        if (release == null) {
            return Collections.emptyList();
        }
        return release.getTags(file, "https://schemas.mediaheap.net/musicbrainz/track/album");
    }

    private @NonNull List<MediaHeapTag> extractAlbumArtist(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws Exception {
        var id = MediaHeapTag.findTagValueInNamespaces(existingTags, AudioTaggerExtractor.AUDIO_NAMESPACES, "musicbrainz_albumartistid");
        if (id == null) {
            return Collections.emptyList();
        }
        var artist = client.getArtist(id);
        if (artist == null) {
            return Collections.emptyList();
        }
        return artist.getTags(file, "https://schemas.mediaheap.net/musicbrainz/track/album-artist");
    }

    private @NonNull List<MediaHeapTag> extractArtist(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws Exception {
        var id = MediaHeapTag.findTagValueInNamespaces(existingTags, AudioTaggerExtractor.AUDIO_NAMESPACES, "musicbrainz_artistid");
        if (id == null) {
            return Collections.emptyList();
        }
        var artist = client.getArtist(id);
        if (artist == null) {
            return Collections.emptyList();
        }
        return artist.getTags(file, "https://schemas.mediaheap.net/musicbrainz/track/artist");
    }

    private @NonNull List<MediaHeapTag> extractOriginalAlbum(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws Exception {
        var id = MediaHeapTag.findTagValueInNamespaces(existingTags, AudioTaggerExtractor.AUDIO_NAMESPACES, "musicbrainz_originalalbumid");
        if (id == null) {
            return Collections.emptyList();
        }
        var release = client.getRelease(id);
        if (release == null) {
            return Collections.emptyList();
        }
        return release.getTags(file, "https://schemas.mediaheap.net/musicbrainz/track/original-album");
    }

    private @NonNull List<MediaHeapTag> extractOriginalArtist(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws Exception {
        var id = MediaHeapTag.findTagValueInNamespaces(existingTags, AudioTaggerExtractor.AUDIO_NAMESPACES, "musicbrainz_originalartistid");
        if (id == null) {
            return Collections.emptyList();
        }
        var artist = client.getArtist(id);
        if (artist == null) {
            return Collections.emptyList();
        }
        return artist.getTags(file, "https://schemas.mediaheap.net/musicbrainz/track/original-artist");
    }

    private @NonNull List<MediaHeapTag> extractRecording(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws Exception {
        var id = MediaHeapTag.findTagValueInNamespaces(existingTags, AudioTaggerExtractor.AUDIO_NAMESPACES, "musicbrainz_recordingid");
        if (id == null) {
            return Collections.emptyList();
        }
        var recording = client.getRecording(id);
        if (recording == null) {
            return Collections.emptyList();
        }
        return recording.getTags(file, "https://schemas.mediaheap.net/musicbrainz/track/recording");
    }

    private @NonNull List<MediaHeapTag> extractReleaseGroup(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws Exception {
        var id = MediaHeapTag.findTagValueInNamespaces(existingTags, AudioTaggerExtractor.AUDIO_NAMESPACES, "musicbrainz_releasegroupid");
        if (id == null) {
            return Collections.emptyList();
        }
        var releaseGroup = client.getReleaseGroup(id);
        if (releaseGroup == null) {
            return Collections.emptyList();
        }
        return releaseGroup.getTags(file, "https://schemas.mediaheap.net/musicbrainz/track/release-group");
    }

    private @NonNull List<MediaHeapTag> extractWork(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws Exception {
        var id = MediaHeapTag.findTagValueInNamespaces(existingTags, AudioTaggerExtractor.AUDIO_NAMESPACES, "musicbrainz_workid");
        if (id == null) {
            return Collections.emptyList();
        }
        var work = client.getWork(id);
        if (work == null) {
            return Collections.emptyList();
        }
        return work.getTags(file, "https://schemas.mediaheap.net/musicbrainz/track/work");
    }

    private @NonNull List<MediaHeapTag> extractTrack(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws Exception {
        var trackId = MediaHeapTag.findTagValueInNamespaces(existingTags, AudioTaggerExtractor.AUDIO_NAMESPACES, "musicbrainz_trackid");
        if (trackId == null) {
            return Collections.emptyList();
        }
        var releaseId = MediaHeapTag.findTagValueInNamespaces(existingTags, AudioTaggerExtractor.AUDIO_NAMESPACES, "musicbrainz_albumid");
        if (releaseId == null) {
            return Collections.emptyList();
        }
        var track = client.getTrack(releaseId, trackId);
        if (track == null) {
            return Collections.emptyList();
        }
        return track.getTags(file, "https://schemas.mediaheap.net/musicbrainz/track");
    }

    @Override
    public @NonNull List<MediaHeapTag> extractTagsFrom(@NonNull MediaHeapFile file, @NonNull List<MediaHeapTag> existingTags) throws IOException {
        try {
            @Cleanup("shutdown") var scope = new ScheduledThreadPoolExecutor(8);
            var album = scope.submit(() -> extractAlbum(file, existingTags));
            var albumArtist = scope.submit(() -> extractAlbumArtist(file, existingTags));
            var artist = scope.submit(() -> extractArtist(file, existingTags));
            var originalAlbum = scope.submit(() -> extractOriginalAlbum(file, existingTags));
            var originalArtist = scope.submit(() -> extractOriginalArtist(file, existingTags));
            var recording = scope.submit(() -> extractRecording(file, existingTags));
            var releaseGroup = scope.submit(() -> extractReleaseGroup(file, existingTags));
            var work = scope.submit(() -> extractWork(file, existingTags));
            var track = scope.submit(() -> extractTrack(file, existingTags));

            List<MediaHeapTag> tags = new ArrayList<>();
            tags.addAll(album.get());
            tags.addAll(albumArtist.get());
            tags.addAll(artist.get());
            tags.addAll(originalAlbum.get());
            tags.addAll(originalArtist.get());
            tags.addAll(recording.get());
            tags.addAll(releaseGroup.get());
            tags.addAll(work.get());
            tags.addAll(track.get());
            return tags;
        } catch (ExecutionException | InterruptedException e) {
            throw new MusicbrainzException(e);
        }
    }

    public static class MusicbrainzException extends IOException {
        public MusicbrainzException(Throwable cause) {
            super(cause);
        }
    }
}
