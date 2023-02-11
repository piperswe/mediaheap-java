package net.mediaheap.namespace;

import java.util.Map;

public class NamespaceSchema {
    private String displayName;
    private String uri;
    private Map<String, KeySchema> keys;

    public String getDisplayName() {
        return displayName;
    }

    public String getUri() {
        return uri;
    }

    public Map<String, KeySchema> getKeys() {
        return keys;
    }
}
