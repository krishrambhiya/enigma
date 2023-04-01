package enigma;

import java.util.ArrayList;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Krish Rambhiya
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        String newtemporaryCycle = cycles.replace("(", "");
        ArrayList<ArrayList<Character>> newBuildCycle = new ArrayList<>();
        ArrayList<Character> newCurrentCycle = new ArrayList<>();
        for (int i = 0; i < newtemporaryCycle.length(); i++) {
            char newCurrentChar = newtemporaryCycle.charAt(i);
            if (newCurrentChar == ' ') {
                continue;
            } else if (!(newCurrentChar == ')')) {
                newCurrentCycle.add(newCurrentChar);
            } else if (newCurrentChar == ')') {
                newBuildCycle.add(newCurrentCycle);
                newCurrentCycle = new ArrayList<>();
            }
        }
        _cycleset = newBuildCycle;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        ArrayList<Character> newBuildCycle = new ArrayList<>();
        for (int i = 0; i < cycle.length(); i++) {
            newBuildCycle.add(cycle.charAt(i));
        }
        _cycleset.add(newBuildCycle);
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
     *  alphabet size. */
    int permute(int p) {
        char neworiCharacter = _alphabet.toChar(wrap(p));
        char newpermCharacter = permute(neworiCharacter);
        return _alphabet.toInt(newpermCharacter);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char neworiCharacter = _alphabet.toChar(wrap(c));
        char newpermCharacter = invert(neworiCharacter);
        return _alphabet.toInt(newpermCharacter);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char permutatedCharacter = p;
        for (ArrayList<Character> currentCycle : _cycleset) {
            if (currentCycle.contains(p)) {
                int index = currentCycle.indexOf(p);
                if (index != currentCycle.size() - 1) {
                    permutatedCharacter = currentCycle.get(index + 1);
                } else {
                    permutatedCharacter = currentCycle.get(0);
                }
            }
        }
        return permutatedCharacter;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char invertedCharacter = c;
        for (ArrayList<Character> currentCycle : _cycleset) {
            if (currentCycle.contains(c)) {
                int index = currentCycle.indexOf(c);
                if (index == 0) {
                    invertedCharacter =
                            currentCycle.get(currentCycle.size() - 1);
                } else {
                    invertedCharacter = currentCycle.get(index - 1);
                }

            }
        }
        return invertedCharacter;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i++) {
            if (permute(_alphabet.toChar(i)) == _alphabet.toChar(i)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** An arraylist of arraylists, each containing a cycle. */
    private ArrayList<ArrayList<Character>> _cycleset;

}
