package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Nazli Urenli
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!perm.derangement()) {
            throw new EnigmaException("Derangement is not applicable.");
        }
    }
    /** Reflector should implement this. */
    @Override
    boolean reflecting() {
        return true;
    }
    /** A reflector connart advance. */
    @Override
    void advance() {
        throw new EnigmaException("Advance should not be called in reflector");
    }
    /** A reflector is unable to convert backwards. */
    @Override
    int convertBackward(int e) {
        throw error("Cannot convert backward");
    }

    /** Reflector has one distinct position. */
    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has one position");
        }
    }
}
