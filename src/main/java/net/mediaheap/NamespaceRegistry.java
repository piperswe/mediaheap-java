package net.mediaheap;

import com.google.gson.Gson;
import net.mediaheap.namespace.NamespaceSchema;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class NamespaceRegistry {
    private static final NamespaceRegistry global = new NamespaceRegistry();
    private static final Gson gson = new Gson();
    private final Map<String, NamespaceSchema> registered = new HashMap<>();

    public static NamespaceRegistry getGlobal() {
        return global;
    }

    public void register(NamespaceSchema namespaceSchema) {
        registered.put(namespaceSchema.getUri(), namespaceSchema);
    }

    public NamespaceSchema getSchema(String uri) {
        return registered.get(uri);
    }

    private void registerBuiltinFromResource(String path) throws IOException {
        path = "/net/mediaheap/namespaces/" + path;
        var resource = getClass().getResource(path);
        assert resource != null;
        NamespaceSchema namespace;
        try (var stream = resource.openStream()) {
            namespace = gson.fromJson(new InputStreamReader(stream), NamespaceSchema.class);
        }
        register(namespace);
    }

    public void registerBuiltin() throws IOException {
        registerBuiltinFromResource("id3/v1.json");
        registerBuiltinFromResource("id3/v2.json");
        registerBuiltinFromResource("flac.json");
        registerBuiltinFromResource("m4a.json");
        registerBuiltinFromResource("ogg.json");
        registerBuiltinFromResource("wav.json");
    }
}
