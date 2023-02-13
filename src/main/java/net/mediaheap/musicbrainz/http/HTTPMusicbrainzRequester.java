package net.mediaheap.musicbrainz.http;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.google.gson.Gson;
import lombok.Cleanup;
import lombok.NonNull;

import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

class HTTPMusicbrainzRequester<T> extends CacheLoader<String, Optional<T>> {
    // MusicBrainz API is limited to 1 request per second per IP. We're setting the limit to
    // 0.9req/sec so that we don't accidentally exceed it.
    @NonNull
    private static final RateLimiter rateLimiter = RateLimiter.create(0.9);

    @NonNull
    private final Function<String, String> urlGenerator;
    @NonNull
    private final Supplier<HttpClient> getHttpClient;
    @NonNull
    private final Supplier<Gson> getGson;
    @NonNull
    private final Class<T> klass;

    private HTTPMusicbrainzRequester(@NonNull Function<String, String> urlGenerator, @NonNull Supplier<HttpClient> getHttpClient, @NonNull Supplier<Gson> getGson, @NonNull Class<T> klass) {
        this.urlGenerator = urlGenerator;
        this.getHttpClient = getHttpClient;
        this.getGson = getGson;
        this.klass = klass;
    }

    private static @NonNull HttpRequest newHttpRequest(@NonNull String url) throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("User-Agent", "mediaheap-java ( https://github.com/piperswe/mediaheap-java )")
                .timeout(Duration.of(10, ChronoUnit.SECONDS))
                .GET()
                .build();
    }

    static <T> @NonNull LoadingCache<String, Optional<T>> createCache(@NonNull Function<String, String> urlGenerator, @NonNull Supplier<HttpClient> getHttpClient, @NonNull Supplier<Gson> getGson, @NonNull Class<T> klass) {
        return new HTTPMusicbrainzRequester<>(urlGenerator, getHttpClient, getGson, klass).toLoadingCache();
    }

    private @NonNull Optional<T> request(@NonNull String url) throws Exception {
        var request = newHttpRequest(url);
        rateLimiter.acquire();
        var response = getHttpClient.get().send(request, HttpResponse.BodyHandlers.ofInputStream());
        if (response.statusCode() == 503) {
            // Re-request since we hit the rate limit
            // Rate limiter should sleep long enough for us
            return request(url);
        } else if (response.statusCode() == 404) {
            return Optional.empty();
        } else if (response.statusCode() >= 300) {
            throw new Exception(String.format("Got status code %d", response.statusCode()));
        }
        @Cleanup var stream = response.body();
        @Cleanup var reader = new InputStreamReader(stream);
        return Optional.of(getGson.get().fromJson(reader, klass));
    }

    @Override
    public @NonNull Optional<T> load(@NonNull String id) throws Exception {
        var url = urlGenerator.apply(id);
        return request(url);
    }

    private @NonNull LoadingCache<String, Optional<T>> toLoadingCache() {
        return CacheBuilder.newBuilder().maximumSize(100).build(this);
    }
}
