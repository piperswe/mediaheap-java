package net.mediaheap;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NamespaceRegistryTest {
    private void assertBuiltinExists(NamespaceRegistry registry, String uri) {
        var schema = registry.getSchema(uri);
        assertTrue(schema.isPresent(), String.format("Schema at uri %s isn't registered", uri));
        assertEquals(schema.get().uri(), uri, String.format("Schema at uri %s doesn't have a matching uri attribute", uri));
    }

    @Test
    public void builtinsExist() throws IOException {
        var registry = new NamespaceRegistry();
        registry.registerBuiltin();
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/file/audio/tags/mp3/id3v1");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/file/audio/tags/mp3/id3v2");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/musicbrainz/track/album");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/musicbrainz/track/album-artist");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/musicbrainz/track/artist");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/musicbrainz/track/original-album");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/musicbrainz/track/original-artist");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/musicbrainz/track/recording");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/musicbrainz/track/release-group");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/musicbrainz/track/work");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/musicbrainz/track");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/file/audio/tags/flac");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/file/audio/tags/m4a");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/file/audio/tags/ogg");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/file/audio/tags/wav");
    }
}