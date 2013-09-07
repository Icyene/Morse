package morse;

import java.io.*;
import java.util.*;
import javax.sound.midi.*;
import javax.sound.sampled.*;

/**
 *
 * @author Tudor
 */
public class Morse {

    public static final int DOT = 250, DASH = DOT * 3, FREQ = 800;
    static String[] morse = new String[]{".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".----", "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.", "-----", ".-.-.-", "--..--", "---...", "..--..", ".----.", "-....-", "-..-.", "-.--.-", "-.--.-", ".-..-.", ".--.-.", "-...-"};

    public static void main(String[] args) throws IOException, InvalidMidiDataException, MidiUnavailableException, LineUnavailableException, InterruptedException {
        boolean sound = !Arrays.asList(args).contains("-n");

        System.out.print("Hit enter to begin transmission.");
        String line;
        while ((line = new BufferedReader(new InputStreamReader(System.in)).readLine()) != null) {
            for (char c : line.toUpperCase().toCharArray()) {
                int letter = c - 'A';
                if (letter <= morse.length && letter > 0) {
                    for (char note : morse[letter].toCharArray()) {
                        System.out.print(note);
                        if (sound) {
                            AudioFormat af = new AudioFormat(8000F, 8, 1, true, false);
                            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
                            sdl.open(af);
                            sdl.start();
                            for (int i = 0; i < (note == '.' ? DOT : DASH) * 8; i++) {
                                sdl.write(new byte[]{(byte) (Math.sin(i / (8000F / FREQ) * 2.0 * Math.PI) * 127.0)}, 0, 1);
                            }
                            sdl.drain();
                            sdl.stop();
                            sdl.close();
                        }
                        Thread.sleep(DOT / 5);
                    }
                } else if (c == ' ') {
                    System.out.print("\n");
                    Thread.sleep(DOT * 4);
                }
            }
            System.out.print("\n>>> ");
        }
    }
}