package net.mediaheap;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NamespaceRegistryTest {
    private void assertBuiltinExists(NamespaceRegistry registry, String uri) {
        var schema = registry.getSchema(uri);
        assertNotNull(schema, String.format("Schema at uri %s isn't registered", uri));
        assertEquals(schema.getUri(), uri, String.format("Schema at uri %s doesn't have a matching uri attribute", uri));
    }

    @Test
    public void testBuiltinsExist() throws IOException {
        var registry = new NamespaceRegistry();
        registry.registerBuiltin();
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/id3/v1");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/id3/v2");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/flac");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/m4a");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/ogg");
        assertBuiltinExists(registry, "https://schemas.mediaheap.net/wav");
    }
}