package umbral.audio;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

class AudioLoader {
    private Map<String, URL> loadedTracks = new TreeMap<>();
    private Map<String, URL> loadedSounds = new TreeMap<>();

    private URL urlLoad(String path) {
        return getClass().getClassLoader().getResource(path);
    }

    private void loadYAML() {
        Yaml yaml = new Yaml();

        InputStream inputStream = null;
        try {
            inputStream = Objects.requireNonNull(getClass().getClassLoader().getResource("yaml/sounds.yml")).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sounds sounds = yaml.loadAs(inputStream, Sounds.class);

        for (Sound sound : sounds.getSounds()) {
            if (sound.getIsBGM()) {
                loadedTracks.put(sound.getSoundTag(), urlLoad(sound.getSoundURL()));
            } else {
                loadedSounds.put(sound.getSoundTag(), urlLoad(sound.getSoundURL()));
            }
        }
    }

    public void loadAudio() {
        loadYAML();
    }

    Map<String, URL> getLoadedTracks() {
        return loadedTracks;
    }

    Map<String, URL> getLoadedSounds() {
        return loadedSounds;
    }
}
