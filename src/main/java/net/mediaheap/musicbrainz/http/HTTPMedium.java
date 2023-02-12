package net.mediaheap.musicbrainz.http;

import com.google.gson.annotations.SerializedName;

record HTTPMedium(@SerializedName("track-offset") long trackOffset,
                  @SerializedName("track-count") long trackCount, String title, long position,
                  HTTPTrack[] tracks, @SerializedName("format-id") String formatId, String format) {
}
