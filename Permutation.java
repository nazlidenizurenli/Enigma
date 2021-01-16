package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Nazli Urenli
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. Add the cycle to CYCLES*/
    private void addCycle(String cycle) {
        if (cycle.length() == 0) {
            throw new EnigmaException("empty cycle is not allowed");
        } else {
            _cycles = _cycles + " " + cycle;
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     * alphabet size. */
    int permute(int p) {
        char fix = _alphabet.toChar(wrap(p));
        char fixed = permute(fix);
        int result = _alphabet.toInt(fixed);
        return result;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char fix = _alphabet.toChar(wrap(c));
        char fixed = invert(fix);
        int result = _alphabet.toInt(fixed);
        return result;
    }

    /** Return the permuted char P at index i. */
    char permute(char p) {
        int n = 0;
        for (int x = 0; x < _cycles.length(); x = x + 1) {
            if (_cycles.charAt(x) == p) {
                n = x;
            }
        }
        if (n == 0) {
            return p;
        }
        if (_cycles.charAt(n + 1) == ')') {
            if (_cycles.charAt(n - 1) == '(') {
                return p;
            } else {
                while (_cycles.charAt(n) != '(') {
                    n = n - 1;
                }
            }
        }
        return _cycles.charAt(n + 1);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int n = 0;
        int x;
        for (x = 0; x < _cycles.length(); x = x + 1) {
            if (_cycles.charAt(x) == c) {
                n = x;
            }
        }
        if (n == 0) {
            return c;
        }
        if (_cycles.charAt(n - 1) == '(') {
            if (_cycles.charAt(n + 1) == ')') {
                return c;
            } else {
                while (_cycles.charAt(n) != ')') {
                    n = n + 1;
                }
            }
        }
        return _cycles.charAt(n - 1);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int totalChars = 0;
        for (int i = 0; i < _cycles.length(); i++) {
            if (_cycles.charAt(i) != '('
                    || _cycles.charAt(i) != ')'
                    || _cycles.charAt(i) != ' ') {
                totalChars++;
            }
        }
        if (totalChars == _alphabet.size()) {
            return true;
        }
        for (int i = 1; i < _cycles.length(); i++) {
            if (_cycles.charAt(i - 1) == '(' && _cycles.charAt(i + 1) == ')') {
                return false;
            }
        }
        return true;
    }
    /** Alphabet corresponding to this permutation. */
    private Alphabet _alphabet;

    /** Cycles corresponding to this permutation. */
    private String _cycles;
}
