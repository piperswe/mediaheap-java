package net.mediaheap.musicbrainz.http;

import net.mediaheap.musicbrainz.MusicbrainzClient;
import org.junit.jupiter.api.Test;
import org.musicbrainz.MBWS2Exception;

import static org.junit.jupiter.api.Assertions.*;

// These tests will become very fragile if we test specifics.
// Just test names to make sure the requests are well-formed.

class HTTPMusicbrainzClientTest {
    private static final MusicbrainzClient client = new HTTPMusicbrainzClient();

    @Test
    void getArtist() throws Exception {
        var artist = client.getArtist("deefd67b-c917-4b0d-ab48-c5af7f92cd82");
        assertEquals("Lovejoy", artist.getName());
    }

    @Test
    void getRecording() throws Exception {
        var recording = client.getRecording("0cb2c659-1218-4639-8d14-78f12a2752a0");
        assertEquals("Call Me What You Like", recording.getTitle());
    }

    @Test
    void getRelease() throws Exception {
        var release = client.getRelease("18bb81d7-423d-4d30-9552-f83426e2125e");
        assertEquals("Call Me What You Like", release.getTitle());
    }

    @Test
    void getReleaseGroup() throws Exception {
        var release = client.getReleaseGroup("e838a31f-74e6-4247-bfef-5e3a3318a559");
        assertEquals("Call Me What You Like", release.getTitle());
    }

    @Test
    void getTrack() throws Exception {
        var track = client.getTrack("18bb81d7-423d-4d30-9552-f83426e2125e", "045696dc-7ba4-488a-a038-688d625ace2a");
        assertEquals("1", track.getNumber());
    }

    @Test
    void getWork() throws Exception {
        var work = client.getWork("238a3c0a-c845-4866-8234-45d6e18f112b");
        assertEquals("Call Me What You Like", work.getTitle());
    }
}