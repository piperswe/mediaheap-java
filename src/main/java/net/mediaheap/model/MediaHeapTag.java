package net.mediaheap.model;

import lombok.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public record MediaHeapTag(int id, int fileId, @NonNull String namespace, @NonNull String key, @NonNull String value) {
    public static @NonNull MediaHeapTag of(int id, @NonNull MediaHeapFile file, @NonNull String namespace, @NonNull String key, @NonNull String value) {
        return new MediaHeapTag(id, file.id(), namespace, key, value);
    }

    public static @NonNull MediaHeapTag fromResultSet(@NonNull ResultSet results) throws SQLException {
        return new MediaHeapTag(
                results.getInt("id"),
                results.getInt("fileId"),
                results.getString("namespace"),
                results.getString("key"),
                results.getString("value")
        );
    }

    public static @NonNull Optional<String> findTagValue(@NonNull Iterable<MediaHeapTag> tags, @NonNull String namespace, @NonNull String key) {
        for (var tag : tags) {
            if (tag.namespace().equals(namespace) && tag.key().equalsIgnoreCase(key)) {
                return Optional.of(tag.value());
            }
        }
        return Optional.empty();
    }

    public static @NonNull Optional<@NonNull String> findTagValueInNamespaces(@NonNull Iterable<@NonNull MediaHeapTag> tags, @NonNull Set<@NonNull String> namespaces, @NonNull String key) {
        for (var tag : tags) {
            if (namespaces.contains(tag.namespace()) && tag.key().equalsIgnoreCase(key)) {
                return Optional.of(tag.value());
            }
        }
        return Optional.empty();
    }

    public static @NonNull Optional<@NonNull String> findTagValueInNamespaces(@NonNull Iterable<@NonNull MediaHeapTag> tags, @NonNull Collection<@NonNull String> namespaces, @NonNull String key) {
        return findTagValueInNamespaces(tags, new HashSet<>(namespaces), key);
    }

    public static @NonNull Optional<@NonNull String> findTagValueInNamespaces(@NonNull Iterable<@NonNull MediaHeapTag> tags, @NonNull String[] namespaces, @NonNull String key) {
        return findTagValueInNamespaces(tags, Arrays.asList(namespaces), key);
    }

    public static @NonNull Map<@NonNull String, @NonNull Map<@NonNull String, @NonNull String>> toNamespaceKeyValueMap(@NonNull Iterable<@NonNull MediaHeapTag> tags) {
        Map<@NonNull String, @NonNull Map<@NonNull String, @NonNull String>> map = new HashMap<>();
        for (var tag : tags) {
            if (!map.containsKey(tag.namespace())) {
                map.put(tag.namespace(), new HashMap<>());
            }
            var nsMap = map.get(tag.namespace());
            nsMap.put(tag.key(), tag.value());
        }
        return map;
    }
}
