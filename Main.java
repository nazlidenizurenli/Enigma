package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Nazli Urenli
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }
    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine myMachine = readConfig();
        String continueStr = _input.nextLine();
        while (_input.hasNext()) {
            String mySett = continueStr;
            if (!mySett.contains("*")) {
                throw new EnigmaException("Invalid setting");
            }
            setUp(myMachine, mySett);
            continueStr = (_input.nextLine()).toUpperCase();
            while (continueStr.isEmpty()) {
                continueStr = (_input.nextLine()).toUpperCase();
            }
            while (!(continueStr.contains("*"))) {
                String processed = myMachine.convert
                        (continueStr.replaceAll(" ", ""));
                if (continueStr.isEmpty()) {
                    _output.append("\n");
                } else {
                    printMessageLine(processed);
                }
                if (!_input.hasNext()) {
                    continueStr = "*";
                } else {
                    continueStr = (_input.nextLine()).toUpperCase();
                }
            }
        }
    }
    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            ArrayList<Rotor> rotorsArray = new ArrayList<>();
            final String process = _config.next();
            if (process.contains("*")
                    || process.contains("(")
                    || process.contains(")")) {
                throw new EnigmaException("Wrong format.");
            }
            _alphabet = new Alphabet(process);

            if (!_config.hasNextInt()) {
                throw new EnigmaException("Can't read configuration");
            }
            final int rSlots = _config.nextInt();
            if (!_config.hasNextInt()) {
                throw new EnigmaException("Pawl is unavailable.");
            }
            final int mRotor = _config.nextInt();
            if (rSlots < 0 || mRotor < 0) {
                throw new EnigmaException("Invalid number of elements.");
            }
            if (mRotor >= rSlots) {
                throw new EnigmaException("Pawls must be less.");
            }
            helperNext = _config.next();
            while (_config.hasNext()) {
                rotorsArray.add(readRotor());
            }
            Machine myEnigma = new Machine(_alphabet,
                    rSlots, mRotor, rotorsArray);
            return myEnigma;
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String rotorName = helperNext;
            if (helperNext.contains("(") || helperNext.contains(")")) {
                throw new EnigmaException("Not a valid rotor name.");
            }
            helperNext = _config.next();
            char rotorType = helperNext.charAt(0);
            String rotorNotches = helperNext.substring(1);
            if (rotorType == 'M' && rotorNotches.isEmpty()) {
                throw new EnigmaException("No notches detected.");
            }
            String myPerm = "";
            helperNext = _config.next();
            while (helperNext.contains("(") && _config.hasNext()) {
                myPerm = myPerm + helperNext + " ";
                helperNext = _config.next();
            }
            if (!_config.hasNext()) {
                myPerm = myPerm + helperNext;
            }
            Permutation perm = new Permutation(myPerm, _alphabet);

            if (rotorType == 'R') {
                return new Reflector(rotorName, perm);
            } else if (rotorType == 'N') {
                return new FixedRotor(rotorName, perm);
            } else if (rotorType == 'M') {
                return new MovingRotor(rotorName, perm, rotorNotches);
            } else {
                throw new EnigmaException("No such rotor type exists.");
            }

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        int x;
        int n;
        int b;
        String[] arrangeSet = settings.split(" ");
        String createPerm = "";
        String[] myRotorArray = new String[M.numRotors()];
        if (arrangeSet.length - 1 < M.numRotors()) {
            throw new EnigmaException("Setting format is wrong.");
        }
        for (x = 1; x < M.numRotors() + 1; x += 1) {
            myRotorArray[x - 1] = arrangeSet[x];
        }
        for (n = 0; n < myRotorArray.length - 1; n += 1) {
            for (int s = n + 1; s < myRotorArray.length; s += 1) {
                if (myRotorArray[n].equals(myRotorArray[s])) {
                    throw new EnigmaException("Rotor is repeated.");
                }
            }
        }
        for (b = 7; b < arrangeSet.length; b += 1) {
            createPerm = createPerm.concat(arrangeSet[b] + " ");
        }
        M.insertRotors(myRotorArray);
        if (!(M.rotorArr()[0].reflecting())) {
            throw new EnigmaException("Wrong first rotor.");
        }
        M.setRotors(arrangeSet[M.numRotors() + 1]);
        M.setPlugboard(new Permutation(createPerm, _alphabet));
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        while (msg.length() != 0) {
            if (msg.length() > 5) {
                _output.append(msg.substring(0, 5) + " ");
                msg = msg.substring(5);
            } else {
                _output.append(msg + "\n");
                break;
            }
        }
    }

    /** Helper String for readconfig. */
    private String helperNext;

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

}

