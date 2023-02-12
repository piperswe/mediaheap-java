package net.mediaheap.musicbrainz.http;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;

import java.util.Optional;
import java.util.function.Function;

class HTTPMusicbrainzCacheLoader<T> extends CacheLoader<String, Optional<T>> {
    @NonNull
    private final Function<String, String> urlGenerator;
    @NonNull
    private final Class<T> klass;
    @NonNull
    private final HTTPMusicbrainzClient client;

    HTTPMusicbrainzCacheLoader(@NonNull Function<String, String> urlGenerator, @NonNull Class<T> klass, @NonNull HTTPMusicbrainzClient client) {
        this.urlGenerator = urlGenerator;
        this.klass = klass;
        this.client = client;
    }

    @Override
    public @NonNull Optional<T> load(@NonNull String id) throws Exception {
        var url = urlGenerator.apply(id);
        return client.request(url, klass);
    }
}
