package net.mediaheap.namespace;

import java.util.Map;

public record NamespaceSchema(String displayName, String uri, Map<String, KeySchema> keys) {
}
