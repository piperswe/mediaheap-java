package net.mediaheap.importer;

import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.TagTextField;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AudioTaggerExtractor implements Extractor {
    private static final String COPYRIGHT_SYMBOL = new String(new byte[]{(byte) 0xa9});

    private final String ns;

    public AudioTaggerExtractor(String ns) {
        this.ns = ns;
    }

    static void addTagsFromTag(MediaHeapFile file, List<MediaHeapTag> tags, Tag fileTag, String namespace) throws UnsupportedEncodingException {
        if (fileTag != null) {
            for (Iterator<TagField> it = fileTag.getFields(); it.hasNext(); ) {
                var field = it.next();
                if (field instanceof TagTextField textField) {
                    tags.add(new MediaHeapTag(file, namespace, field.getId().replace(COPYRIGHT_SYMBOL, "_c"), textField.getContent()));
                }
            }
        }
    }

    @Override
    public List<MediaHeapTag> extractTagsFrom(MediaHeapFile file) throws IOException {
        try {
            var audioFile = AudioFileIO.read(file.getFile());
            List<MediaHeapTag> tags = new ArrayList<>();
            addTagsFromTag(file, tags, audioFile.getTag(), ns);
            return tags;
        } catch (CannotReadException | InvalidAudioFrameException | TagException | ReadOnlyFileException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
