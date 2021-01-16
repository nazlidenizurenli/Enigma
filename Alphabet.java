package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Nazli Urenli
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _characters = chars;
        charsArray = new char[chars.length()];

        int transfer;
        for (transfer = 0; transfer < chars.length(); transfer += 1) {
            charsArray[transfer] = chars.charAt(transfer);
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _characters.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        int x;
        for (x = 0; x < size(); x += 1) {
            if (_characters.charAt(x) == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        } else {
            return _characters.charAt(index);
        }
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        int x;
        for (x = 0; x < size(); x += 1) {
            if (charsArray[x] == ch) {
                return x;
            }
        }
        throw new EnigmaException("The character is not in the alphabet");
    }

    /** A character String. */
    private String _characters;

    /** A character Array. */
    private char[] charsArray;
}
