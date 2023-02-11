package net.mediaheap.musicbrainz;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MockMusicbrainzClient implements MusicbrainzClient {
    private static final Logger logger = Logger.getLogger("MockMusicbrainzClient");
    @Override
    public Artist getArtist(String id) {
        if ("deefd67b-c917-4b0d-ab48-c5af7f92cd82".equals(id)) {
            var artist = new Artist();
            artist.setFetched(LocalDate.now().toString());
            artist.setId(id);
            artist.setName("Lovejoy");
            artist.setSortName("Lovejoy");
            artist.setDisambiguation("2021 indie band with Wilbur Soot");
            artist.setIsni("");
            artist.setCountry(null);
            artist.setAreaId("9d5dd675-3cf4-4296-9e39-67865ebee758");
            artist.setBeginAreaId("8a754a16-0027-3a29-b6d7-2b40ea0481ed");
            artist.setEndAreaId(null);
            artist.setBegin("2021-05-08");
            artist.setEnd(null);
            artist.setEnded("false");
            return artist;
        } else {
            logger.log(Level.WARNING, String.format("Invalid artist id: %s", id));
            return null;
        }
    }

    @Override
    public Recording getRecording(String id) throws Exception {
        if ("0cb2c659-1218-4639-8d14-78f12a2752a0".equals(id)) {
            var recording = new Recording();
            recording.setFetched(LocalDate.now().toString());
            recording.setId(id);
            recording.setTitle("Call Me What You Like");
            recording.setLength("03:46");
            recording.setFirstReleaseDate("2023-02-10");
            return recording;
        } else {
            logger.log(Level.WARNING, String.format("Invalid recording id: %s", id));
            return null;
        }
    }

    @Override
    public Release getRelease(String id) throws Exception {
        if ("18bb81d7-423d-4d30-9552-f83426e2125e".equals(id)) {
            var release = new Release();
            release.setFetched(LocalDate.now().toString());
            release.setId(id);
            release.setTitle("Call Me What You Like");
            release.setStatus("Official");
            release.setQuality("normal");
            release.setLanguage("eng");
            release.setScript("Latn");
            release.setDate("2023-02-10");
            release.setCountry("XW");
            return release;
        } else {
            logger.log(Level.WARNING, String.format("Invalid release id: %s", id));
            return null;
        }
    }

    @Override
    public ReleaseGroup getReleaseGroup(String id) throws Exception {
        if ("e838a31f-74e6-4247-bfef-5e3a3318a559".equals(id)) {
            var releaseGroup = new ReleaseGroup();
            releaseGroup.setFetched(LocalDate.now().toString());
            releaseGroup.setId(id);
            releaseGroup.setTitle("Call Me What You Like");
            releaseGroup.setFirstReleaseDate("2023-02-10");
            releaseGroup.setPrimaryType("Single");
            return releaseGroup;
        } else {
            logger.log(Level.WARNING, String.format("Invalid release group id: %s", id));
            return null;
        }
    }

    @Override
    public Track getTrack(String releaseId, String trackId) throws Exception {
        if ("18bb81d7-423d-4d30-9552-f83426e2125e".equals(releaseId) && "0cb2c659-1218-4639-8d14-78f12a2752a0".equals(trackId)) {
            var track = new Track();
            track.setFetched(LocalDate.now().toString());
            track.setId(trackId);
            track.setPosition("1");
            track.setNumber("1");
            return track;
        } else {
            logger.log(Level.WARNING, String.format("Invalid release id + track id combination: %s / %s", releaseId, trackId));
            return null;
        }
    }

    @Override
    public Work getWork(String id) throws Exception {
        if ("238a3c0a-c845-4866-8234-45d6e18f112b".equals(id)) {
            var work = new Work();
            work.setFetched(LocalDate.now().toString());
            work.setId(id);
            work.setTitle("Call Me What You Like");
            work.setLanguage("eng");
            return work;
        } else {
            logger.log(Level.WARNING, String.format("Invalid work id: %s", id));
            return null;
        }
    }
}
