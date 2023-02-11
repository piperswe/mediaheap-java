package net.mediaheap.importer;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MP3Extractor implements Extractor {
    static final String V1NS = "https://schemas.mediaheap.net/id3/v1";
    static final String V2NS = "https://schemas.mediaheap.net/id3/v2";

    @Override
    public List<MediaHeapTag> extractTagsFrom(MediaHeapFile file) throws IOException {
        try {
            var audioFile = AudioFileIO.read(file.getFile());
            if (!(audioFile instanceof MP3File mp3)) {
                return Collections.emptyList();
            }
            List<MediaHeapTag> tags = new ArrayList<>();
            AudioTaggerExtractor.addTagsFromTag(file, tags, mp3.getTag(), V1NS);
            AudioTaggerExtractor.addTagsFromTag(file, tags, mp3.getID3v2Tag(), V2NS);
            return tags;
        } catch (CannotReadException | InvalidAudioFrameException | TagException | ReadOnlyFileException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
