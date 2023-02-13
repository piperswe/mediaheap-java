package net.mediaheap.model;

import lombok.NonNull;
import lombok.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Value(staticConstructor = "of")
public class MediaHeapTag {
    int id;
    int fileId;
    @NonNull String namespace;
    @NonNull String key;
    @NonNull String value;

    public static @NonNull MediaHeapTag of(int id, @NonNull MediaHeapFile file, @NonNull String namespace, @NonNull String key, @NonNull String value) {
        return of(id, file.getId(), namespace, key, value);
    }

    public static @NonNull MediaHeapTag fromResultSet(@NonNull ResultSet results) throws SQLException {
        return of(
                results.getInt("id"),
                results.getInt("fileId"),
                results.getString("namespace"),
                results.getString("key"),
                results.getString("value")
        );
    }

    public static @NonNull Optional<String> findTagValue(@NonNull Iterable<MediaHeapTag> tags, @NonNull String namespace, @NonNull String key) {
        for (var tag : tags) {
            if (tag.getNamespace().equals(namespace) && tag.getKey().equalsIgnoreCase(key)) {
                return Optional.of(tag.getValue());
            }
        }
        return Optional.empty();
    }

    public static @NonNull Optional<@NonNull String> findTagValueInNamespaces(@NonNull Iterable<@NonNull MediaHeapTag> tags, @NonNull Set<@NonNull String> namespaces, @NonNull String key) {
        for (var tag : tags) {
            if (namespaces.contains(tag.getNamespace()) && tag.getKey().equalsIgnoreCase(key)) {
                return Optional.of(tag.getValue());
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
            if (!map.containsKey(tag.getNamespace())) {
                map.put(tag.getNamespace(), new HashMap<>());
            }
            var nsMap = map.get(tag.getNamespace());
            nsMap.put(tag.getKey(), tag.getValue());
        }
        return map;
    }
}
