package SoundEffects;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Sound {
    private Clip clip;
    private final String[] files = {
            "sound/ButtonOver.wav",
            "sound/SnakeDie.wav",
            "sound/SnakeEat.wav"
    };

    public void setFile(int i) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(files[i]);
            if (inputStream == null) {
                throw new RuntimeException("Sound file not found: " + files[i]);
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException("Error loading sound file: " + files[i], e);
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Reset to start
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
}