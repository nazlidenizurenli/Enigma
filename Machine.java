package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Nazli Urenli
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _arrayRotors = (ArrayList<Rotor>) allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        myRotors = new Rotor[_numRotors];
        for (int x = 0; x < myRotors.length; x = x + 1) {
            for (int n = 0; n < _arrayRotors.size(); n = n + 1) {
                if (_arrayRotors.get(n).name().toLowerCase().equals(
                        rotors[x].toLowerCase())) {
                    myRotors[x] = _arrayRotors.get(n);
                }
            }
        }

    }
    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (numRotors() - 1 != setting.length()) {
            throw new EnigmaException("Incorrect setting");
        } else {
            for (int x = 1; x < _numRotors; x = x + 1) {
                int connect;
                connect = _alphabet.toInt(setting.charAt(x - 1));
                myRotors[x].set(connect);
            }
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        boolean[] rotatableRotor = new boolean[myRotors.length];
        rotatableRotor[myRotors.length - 1] = true;
        int x;
        for (x = rotatableRotor.length - 1; x > 1; x = x - 1) {
            if (myRotors[x].atNotch()
                    && myRotors[x - 1].rotates()) {
                rotatableRotor[x - 1] = true;
                rotatableRotor[x] = true;
            }
        }
        int upgrade;
        for (upgrade = 0; upgrade < myRotors.length; upgrade += 1) {
            if (rotatableRotor[upgrade]) {
                myRotors[upgrade].advance();
            }
        }
        int inputLetter = _plugboard.permute(c);
        int n;
        for (n = _numRotors - 1; n > 0; n = n - 1) {
            inputLetter = myRotors[n].convertForward(inputLetter);
        }
        int outputLetter = myRotors[0].convertForward(inputLetter);
        int s;
        for (s = 1; s < _numRotors; s = s + 1) {
            outputLetter = myRotors[s].convertBackward(outputLetter);
        }
        int finalLetter = _plugboard.permute(outputLetter);
        return finalLetter;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String output = "";
        int i;
        for (i = 0; i < msg.length(); i += 1) {
            int fix = convert(_alphabet.toInt(msg.charAt(i)));
            char fixed = _alphabet.toChar(fix);
            output = output + fixed;
        }
        return output;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors in the machine which is greater than 1. */
    private int _numRotors;

    /** Number of pawls in the machine. Not all rotors have pawls. */
    private int _pawls;

    /** The permutation associated with my plugboard. */
    private Permutation _plugboard;

    /** All of the rotors in this new machine. */
    private Collection<Rotor> _allRotors;

    /** An array of the rotors I have in this new machine. */
    private Rotor[] myRotors;

    /** An list of rotors as arrays.*/
    private ArrayList<Rotor> _arrayRotors;

    /** Use the rotor Array in Main. Returns myRotors. */
    public Rotor[] rotorArr() {
        return myRotors;
    }
}
