 /*
Ну вы же понимаете, что код здесь только мой?
Well, you do understand that the code here is only mine?
 */
import javax.sound.sampled.*;
import javax.sound.midi.*;
import java.io.*;
import java.net.URL;
import java.awt.Toolkit;

/**
 * File: AudioClip.java
 * Created on 2025 Nov 12, 15:34:04
 * Updated: 2025 Nov 12 — теперь использует vavi-sound для MMF/SMAF
 *
 * @author LWJGL2
 */
public class AudioClip {
    private static final int TYPE_MMF = 1;
    private static final int TYPE_MP3 = 2;
    private static final int TYPE_MIDI = 3;
    private static final int TYPE_WAV = 4;

    private Clip clip;
    private Sequencer midiSequencer;
    private Synthesizer midiSynthesizer;
    private int id;
    private String path;
    private int clipType;
    private FloatControl volumeControl;

    public AudioClip(int id, String resourcePath) {
        this.id = id;
        this.path = resourcePath;
        this.clipType = detectTypeFromPath(resourcePath);

        try {
            InputStream soundStream = loadResource(resourcePath);
            if (soundStream == null) {
                System.out.println("AudioClip: файл не найден " + resourcePath);
                return;
            }

            soundStream = new BufferedInputStream(soundStream);
            soundStream.mark(8);
            byte[] header = new byte[4];
            soundStream.read(header);
            soundStream.reset();

            int detectedType = detectTypeFromHeader(header);
            if (detectedType != -1) {
                clipType = detectedType;
            }

            if (clipType == TYPE_MMF || clipType == TYPE_MIDI) {
                loadMIDI(soundStream);  // vavi-sound обработает MMF как MIDI
            } else {
                loadWAV(soundStream);
            }

        } catch (Exception e) {
            System.err.println("AudioClip: ошибка загрузки " + resourcePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int detectTypeFromPath(String path) {
        String lower = path.toLowerCase();
        if (lower.endsWith(".mmf")) return TYPE_MMF;
        if (lower.endsWith(".mid") || lower.endsWith(".midi")) return TYPE_MIDI;
        if (lower.endsWith(".mp3")) return TYPE_MP3;
        if (lower.endsWith(".wav")) return TYPE_WAV;
        return TYPE_MMF; // default
    }

    private int detectTypeFromHeader(byte[] header) {
        // MMMD → SMAF/MMF
        if (header[0] == 'M' && header[1] == 'M' && header[2] == 'M' && header[3] == 'D') {
            return TYPE_MMF;
        }
        // MThd → Standard MIDI
        if (header[0] == 'M' && header[1] == 'T' && header[2] == 'h' && header[3] == 'd') {
            return TYPE_MIDI;
        }
        // ID3 или MP3 sync
        if (header[0] == 'I' && header[1] == 'D' && header[2] == '3') {
            return TYPE_MP3;
        }
        // RIFF....WAVE
        if (header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F') {
            return TYPE_WAV;
        }
        return -1;
    }

    // === vavi-sound: MMF и MIDI загружаются одинаково ===
    private void loadMIDI(InputStream stream) throws Exception {
        System.out.println("AudioClip: загрузка MIDI/MMF через vavi-sound...");

        // vavi-sound позволяет: MidiSystem.getSequence() → распознаёт .mmf
        Sequence sequence = MidiSystem.getSequence(stream);

        // Открываем синтезатор
        midiSynthesizer = MidiSystem.getSynthesizer();
        midiSynthesizer.open();

        // Создаём sequencer
        midiSequencer = MidiSystem.getSequencer(false);
        midiSequencer.open();
        midiSequencer.setSequence(sequence);

        // Подключаем к синтезатору
        midiSequencer.getTransmitter().setReceiver(midiSynthesizer.getReceiver());

        System.out.println("AudioClip: MIDI/MMF загружен");
        System.out.println("  Длительность: " + sequence.getMicrosecondLength() + " мкс");
        System.out.println("  Треков: " + sequence.getTracks().length);
        System.out.println("  Событий в треке 0: " + (sequence.getTracks().length > 0 ? sequence.getTracks()[0].size() : 0));
    }

    private void loadWAV(InputStream stream) throws Exception {
        AudioInputStream ais = AudioSystem.getAudioInputStream(stream);
        clip = AudioSystem.getClip();
        clip.open(ais);

        if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        }
    }

    private InputStream loadResource(String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url != null) return url.openStream();

            File file = new File(resourcePath);
            if (file.exists()) return new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // === ВОСПРОИЗВЕДЕНИЕ ===
    public void play() {
        play(100, 1);
    }

    public void play(int volume, int loopCount) {
        stop(); // Гарантированный обрыв
        if (midiSequencer != null) {
            playMIDI(volume, loopCount);
        } else if (clip != null) {
            playWAV(volume, loopCount);
        } else {
            System.out.println("[AUDIO] " + path + " (эмуляция)");
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void playWAV(int volume, int loopCount) {
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        setVolume(volume);

        if (loopCount == -1 || loopCount == 255) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else if (loopCount > 1) {
            clip.loop(loopCount - 1);
        } else {
            clip.start();
        }
    }

    private void playMIDI(int volume, int loopCount) {
        if (midiSequencer.isRunning()) midiSequencer.stop();
        midiSequencer.setMicrosecondPosition(0);

        setMIDIVolume(volume);

        if (loopCount == -1 || loopCount == 255) {
            midiSequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        } else if (loopCount > 1) {
            midiSequencer.setLoopCount(loopCount - 1);
        } else {
            midiSequencer.setLoopCount(0);
        }

        midiSequencer.start();
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
        }
        if (midiSequencer != null && midiSequencer.isRunning()) {
            midiSequencer.stop();
            midiSequencer.setMicrosecondPosition(0);
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) clip.stop();
        if (midiSequencer != null && midiSequencer.isRunning()) midiSequencer.stop();
    }

    public void resume() {
        if (clip != null && !clip.isRunning()) clip.start();
        if (midiSequencer != null && !midiSequencer.isRunning()) midiSequencer.start();
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
        if (midiSequencer != null) {
            midiSequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            if (!midiSequencer.isRunning()) midiSequencer.start();
        }
    }

    public void close() {
        if (clip != null) {
            clip.close();
            clip = null;
        }
        if (midiSequencer != null) {
            midiSequencer.close();
            midiSequencer = null;
        }
        if (midiSynthesizer != null) {
            midiSynthesizer.close();
            midiSynthesizer = null;
        }
    }

    public boolean isRunning() {
        if (clip != null) return clip.isRunning();
        if (midiSequencer != null) return midiSequencer.isRunning();
        return false;
    }

    private void setVolume(int percent) {
        if (volumeControl == null) return;
        float min = volumeControl.getMinimum();
        float max = volumeControl.getMaximum();
        float range = max - min;
        float gain = min + (range * percent / 100f);
        volumeControl.setValue(gain);
    }

    private void setMIDIVolume(int percent) {
        if (midiSynthesizer == null || !midiSynthesizer.isOpen()) return;

        try {
            Receiver receiver = midiSynthesizer.getReceiver();
            if (receiver == null) return;

            int volume = (int) (percent * 127 / 100.0);
            ShortMessage msg = new ShortMessage();

            for (int ch = 0; ch < 16; ch++) {
                msg.setMessage(ShortMessage.CONTROL_CHANGE, ch, 7, volume);
                receiver.send(msg, -1);
            }
        } catch (Exception e) {
            System.err.println("AudioClip: ошибка установки MIDI громкости: " + e.getMessage());
        }
    }

    public static boolean isSupported() {
        return true;
    }
}