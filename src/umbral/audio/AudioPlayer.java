package umbral.audio;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

public class AudioPlayer {
    private Map<String, Music> tracks = new TreeMap<>();
    private Map<String, kuusisto.tinysound.Sound> sfx = new TreeMap<>();

    public AudioPlayer() {
        AudioLoader audioLoader = new AudioLoader();
        audioLoader.loadAudio();
        Map<String, URL> sounds = audioLoader.getLoadedSounds(),
                         bgm = audioLoader.getLoadedTracks();

        TinySound.init();
        TinySound.setGlobalVolume(0.60);

        for (int i = 0; i < sounds.size(); i++) {
            String currentTag = sounds.keySet().toArray()[i].toString();
            Sound temp = TinySound.loadSound(sounds.get(currentTag));
            sfx.put(currentTag, temp);
        }

        for (int i = 0; i < bgm.size(); i++) {
            String currentTag = bgm.keySet().toArray()[i].toString();
            Music temp = TinySound.loadMusic(bgm.get(currentTag));
            tracks.put(currentTag, temp);
        }
    }

    public void play(String soundTag) {
        new Thread(() -> sfx.get(soundTag).play()).start();
    }

    public void stop(String soundTag) {
        new Thread(() -> {
            if (tracks.get(soundTag).playing())
                tracks.get(soundTag).stop();
        }).start();
    }

    public void loop(String soundTag) {
        new Thread(() -> {
            if (!tracks.get(soundTag).playing())
                tracks.get(soundTag).play(true);
        }).start();
    }

    public boolean isPlaying(String soundTag) {
        return tracks.get(soundTag).playing();
    }
}
