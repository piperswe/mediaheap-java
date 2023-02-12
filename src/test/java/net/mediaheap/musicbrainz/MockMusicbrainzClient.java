package net.mediaheap.musicbrainz;

import com.google.common.flogger.StackSize;
import lombok.NonNull;
import lombok.extern.flogger.Flogger;

import java.time.LocalDate;
import java.util.Optional;

@Flogger
public class MockMusicbrainzClient implements MusicbrainzClient {
    @Override
    public @NonNull Optional<Artist> getArtist(@NonNull String id) {
        if ("deefd67b-c917-4b0d-ab48-c5af7f92cd82".equals(id)) {
            return Optional.of(Artist.of(
                    LocalDate.now().toString(),
                    id,
                    "Lovejoy",
                    "Lovejoy",
                    "2021 indie band with Wilbur Soot",
                    "",
                    null,
                    "9d5dd675-3cf4-4296-9e39-67865ebee758",
                    "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    null,
                    "2021-05-08",
                    null,
                    "false"
            ));
        } else {
            log.atWarning().withStackTrace(StackSize.SMALL).log("Invalid artist id: %s", id);
            return Optional.empty();
        }
    }

    @Override
    public @NonNull Optional<Recording> getRecording(@NonNull String id) throws Exception {
        if ("0cb2c659-1218-4639-8d14-78f12a2752a0".equals(id)) {
            return Optional.of(Recording.of(
                    LocalDate.now().toString(),
                    id,
                    "Call Me What You Like",
                    "226",
                    "2023-02-10"
            ));
        } else {
            log.atWarning().withStackTrace(StackSize.SMALL).log("Invalid recording id: %s", id);
            return Optional.empty();
        }
    }

    @Override
    public @NonNull Optional<Release> getRelease(@NonNull String id) throws Exception {
        if ("18bb81d7-423d-4d30-9552-f83426e2125e".equals(id)) {
            return Optional.of(Release.of(
                    LocalDate.now().toString(),
                    id,
                    "Call Me What You Like",
                    "Official",
                    "normal",
                    "eng",
                    "Latn",
                    "2023-02-10",
                    "XW"
            ));
        } else {
            log.atWarning().withStackTrace(StackSize.SMALL).log("Invalid release id: %s", id);
            return Optional.empty();
        }
    }

    @Override
    public @NonNull Optional<ReleaseGroup> getReleaseGroup(@NonNull String id) throws Exception {
        if ("e838a31f-74e6-4247-bfef-5e3a3318a559".equals(id)) {
            return Optional.of(ReleaseGroup.of(
                    LocalDate.now().toString(),
                    id,
                    "Call Me What You Like",
                    "2023-02-10",
                    "Single"
            ));
        } else {
            log.atWarning().withStackTrace(StackSize.SMALL).log("Invalid release group id: %s", id);
            return Optional.empty();
        }
    }

    @Override
    public @NonNull Optional<Track> getTrack(@NonNull String releaseId, @NonNull String trackId) throws Exception {
        if ("18bb81d7-423d-4d30-9552-f83426e2125e".equals(releaseId) && "0cb2c659-1218-4639-8d14-78f12a2752a0".equals(trackId)) {
            return Optional.of(Track.of(
                    LocalDate.now().toString(),
                    trackId,
                    "1",
                    "1"
            ));
        } else {
            log.atWarning().withStackTrace(StackSize.SMALL).log("Invalid release id + track id combination: %s / %s", releaseId, trackId);
            return Optional.empty();
        }
    }

    @Override
    public @NonNull Optional<Work> getWork(@NonNull String id) throws Exception {
        if ("238a3c0a-c845-4866-8234-45d6e18f112b".equals(id)) {
            return Optional.of(Work.of(
                    LocalDate.now().toString(),
                    id,
                    "Call Me What You Like",
                    "eng",
                    null
            ));
        } else {
            log.atWarning().withStackTrace(StackSize.SMALL).log("Invalid work id: %s", id);
            return Optional.empty();
        }
    }
}
