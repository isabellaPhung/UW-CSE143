import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

// A version of GuitarHero that also takes input from a MIDI keyboard.
public class GuitarHeroMidi implements Receiver {
    // A constant that maps MIDI pitches to GuitarHero pitches. A pitch of 0 in
    // GuitarHero is concert A, which is 69 in Midi.
    public static final int MIDI_PITCH_TO_PITCH = -69;

    // The guitar to play notes on.
    private Guitar guitar;
    // A queue of events to get pitches from, thread safe so that multiple
    // sources can add to it.
    private BlockingQueue<Character> events;

    // Returns the key for pitch or null if it's invalid.
    private static Character pitchToKey(int pitch) {
        int index = pitch + 24;
        if (index >= 0 && index < Guitar37.KEYBOARD.length()) {
            return Guitar37.KEYBOARD.charAt(pitch + 24);
        } else {
            return null;
        }
    }

    // Close this receiver.
    @Override
    public void close() {
        // Do nothing.
    }

    // Sends a MIDI message for this receiver to proccess with the given
    // message and timeStamp.
    @Override
    public void send(MidiMessage message, long timeStamp) {
        // The way my MIDI keyboard seems to work is that on release it
        // sends another NOTE_ON message with volume 0, so this check means
        // that it will only play on the first NOTE_ON message.
        if (message.getStatus() == ShortMessage.NOTE_ON
            && message.getLength() >= 3 && message.getMessage()[2] > 0) {
            Character key = pitchToKey(
                message.getMessage()[1] + MIDI_PITCH_TO_PITCH);
            if (key != null) {
                events.add(key);
            } else {
                System.out.println("Bad pitch: " + message.getMessage()[1]);
            }
        }
    }

    // Construct a player that will play notes on the given guitar.
    public GuitarHeroMidi(Guitar guitar) {
        this.guitar = guitar;
        events = new LinkedBlockingQueue<Character>();
    }

    // Run the main program, that is, wait for events and play them on the
    // guitar as they come up.
    public void processEvents() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                events.add(StdDraw.nextKeyTyped());
            }
            Character key = events.poll();
            while (key != null) {
                if (guitar.hasString(key)) {
                    guitar.pluck(key);
                } else {
                    System.out.println("bad key: " + key);
                }
                key = events.poll();
            }
            StdAudio.play(guitar.sample());
            guitar.tic();
        }
    }

    // Prompts the user to select a MIDI device, and return the info of that
    // device if one was selected, or null if the user decides not to use a
    // device.
    public static MidiDevice.Info getUserDeviceSelection(Scanner console) {
        MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
        if (info.length == 0) {
            System.err.println("No midi devices.");
            return null;
        }
        printDeviceSelection(info);
        int selection = getUserIntSelection(console, "Please choose a device. ",
            1, info.length + 1);
        if (selection == info.length + 1) {
            return null;
        } else {
            return info[selection - 1];
        }
    }

    // Prints a selection menu for info.
    private static void printDeviceSelection(MidiDevice.Info[] info) {
        for (int i = 1; i <= info.length; i++) {
            System.out.println(i + ". " + info[i - 1].getName() + ": "
                + info[i - 1].getDescription());
        }
        System.out.println((info.length + 1) + ". No midi device");
    }

    // Repeatedly prompts the user for a number until they give enter a valid
    // integer in the range [min, max].
    private static int getUserIntSelection(Scanner console, String prompt,
        int min, int max) {
        System.out.print(prompt);
        boolean hasNumber = false;
        int number = 0;
        while (!hasNumber) {
            try {
                number = Integer.parseInt(console.nextLine());
                if (min <= number && number <= max) {
                    hasNumber = true;
                } else {
                    System.out.print("Please enter a number from " + min
                        + " to " + max + " ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number. ");
            }
        }
        return number;
    }

    // Sets up a guitar with 37 Strings that will play notes from MIDI devices
    // and a regular computer keyboard.
    public static void main(String[] args) {
        MidiDevice.Info info = getUserDeviceSelection(new Scanner(System.in));
        GuitarHeroMidi player = new GuitarHeroMidi(new Guitar37());
        if (info != null) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                if (!device.isOpen()) {
                    device.open();
                }
                device.getTransmitter().setReceiver(player);

            } catch (MidiUnavailableException e) {
                System.err
                    .println("Failed to get MIDI device " + info.getName());
            }
        }
        player.processEvents();
    }
}
