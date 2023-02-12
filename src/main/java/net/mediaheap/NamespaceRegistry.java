package net.mediaheap;

import com.google.gson.Gson;
import lombok.NonNull;
import net.mediaheap.namespace.NamespaceSchema;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NamespaceRegistry {
    @NonNull
    private static final NamespaceRegistry global = new NamespaceRegistry();
    @NonNull
    private static final Gson gson = new Gson();
    @NonNull
    private final Map<String, NamespaceSchema> registered = new HashMap<>();

    public static @NonNull NamespaceRegistry getGlobal() {
        return global;
    }

    public void register(@NonNull NamespaceSchema namespaceSchema) {
        registered.put(namespaceSchema.getUri(), namespaceSchema);
    }

    public Optional<NamespaceSchema> getSchema(@NonNull String uri) {
        return Optional.ofNullable(registered.get(uri));
    }

    private void registerBuiltinFromResource(@NonNull String path) throws IOException {
        path = "namespace/schemas/" + path;
        var resource = getClass().getResource(path);
        assert resource != null;
        NamespaceSchema namespace;
        try (var stream = resource.openStream()) {
            namespace = gson.fromJson(new InputStreamReader(stream), NamespaceSchema.class);
        }
        register(namespace);
    }

    public void registerBuiltin() throws IOException {
        registerBuiltinFromResource("file/audio/tags/mp3/id3v1.json");
        registerBuiltinFromResource("file/audio/tags/mp3/id3v2.json");
        registerBuiltinFromResource("file/audio/tags/flac.json");
        registerBuiltinFromResource("file/audio/tags/m4a.json");
        registerBuiltinFromResource("file/audio/tags/ogg.json");
        registerBuiltinFromResource("file/audio/tags/wav.json");
        registerBuiltinFromResource("musicbrainz/track/album.json");
        registerBuiltinFromResource("musicbrainz/track/album-artist.json");
        registerBuiltinFromResource("musicbrainz/track/artist.json");
        registerBuiltinFromResource("musicbrainz/track/original-album.json");
        registerBuiltinFromResource("musicbrainz/track/original-artist.json");
        registerBuiltinFromResource("musicbrainz/track/recording.json");
        registerBuiltinFromResource("musicbrainz/track/release-group.json");
        registerBuiltinFromResource("musicbrainz/track/work.json");
        registerBuiltinFromResource("musicbrainz/track.json");
    }
}
