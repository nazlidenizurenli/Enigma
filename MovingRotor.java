package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Nazli Urenli
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).*/
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = notches;
    }
    /** A moving rotates. */
    @Override
    boolean rotates() {
        return true;
    }
    /** If the setting is equal to the notch the rotor should rotate. */
    @Override
    boolean atNotch() {
        for (int x = 0; x < _notches.length(); x = x + 1) {
            if (alphabet().toInt(_notches.charAt(x)) == this.setting()) {
                return true;
            }
        }
        return false;
    }
    /** Enable rotors to advance by changing their setting. */
    @Override
    void advance() {
        int newSetting = setting() + 1;
        set(permutation().wrap(newSetting));
    }
    /** The notches for my rotors. */
    private String _notches;

}
