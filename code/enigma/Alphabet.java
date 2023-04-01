package enigma;
import java.util.LinkedHashSet;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Krish Rambhiya
 */
class Alphabet {

    /** Char array. */
    private String _characters;

    /** Alphabet array. */
    private char[] _alphabet;

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _characters = chars;
        LinkedHashSet<Character> newhashSet = new LinkedHashSet<>();
        char[] buildNewAlph = new char[_characters.length()];
        for (int i = 0; i < _characters.length(); i++) {
            if (!newhashSet.add(_characters.charAt(i))) {
                return;
            } else {
                char addtionalChar = _characters.charAt(i);
                buildNewAlph[i] = addtionalChar;
            }
        }
        _alphabet = buildNewAlph;
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
        for (char charSearch : _alphabet) {
            if (charSearch == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index < 0 || size() <= index) {
            throw new EnigmaException("Range exceeding for index");
        }
        return _alphabet[index];
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        for (int i = 0; i < size(); i++) {
            if (_alphabet[i] == ch) {
                return i;
            }
        }
        throw new EnigmaException("not present in alphabet");
    }
}
