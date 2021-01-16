package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Nazli Urenli
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */

    FixedRotor(String name, Permutation perm) {
        super(name, perm);
    }
    /** A fixed rotor can not advance. */
    @Override
    void advance() {
        throw new EnigmaException("Fixed rotors "
                + "cannot advance.");
    }


}
