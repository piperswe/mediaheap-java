package net.mediaheap.model;

import lombok.NonNull;
import lombok.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    public static @NonNull MediaHeapTag fromResultSet(ResultSet results) throws SQLException {
        return of(
                results.getInt("id"),
                results.getInt("fileId"),
                results.getString("namespace"),
                results.getString("key"),
                results.getString("value")
        );
    }

    public static String findTagValue(@NonNull Iterable<MediaHeapTag> tags, @NonNull String namespace, @NonNull String key) {
        for (var tag : tags) {
            if (tag.getNamespace().equals(namespace) && tag.getKey().equalsIgnoreCase(key)) {
                return tag.getValue();
            }
        }
        return null;
    }

    public static String findTagValueInNamespaces(Iterable<MediaHeapTag> tags, Set<String> namespaces, String key) {
        for (var tag : tags) {
            if (namespaces.contains(tag.getNamespace()) && tag.getKey().equalsIgnoreCase(key)) {
                return tag.getValue();
            }
        }
        return null;
    }

    public static String findTagValueInNamespaces(Iterable<MediaHeapTag> tags, Collection<String> namespaces, String key) {
        return findTagValueInNamespaces(tags, new HashSet<>(namespaces), key);
    }

    public static String findTagValueInNamespaces(Iterable<MediaHeapTag> tags, String[] namespaces, String key) {
        return findTagValueInNamespaces(tags, Arrays.asList(namespaces), key);
    }
}
