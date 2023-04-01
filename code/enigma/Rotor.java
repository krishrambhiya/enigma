package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Krish Rambhiya
 */
class Rotor {
    /**  Perm from rotor.
    @param name int.
    @param perm int. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
    }

    /**  Returns name.*/
    String name() {
        return _name;
    }

    /** Returns alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Returns alphabet size. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _setting;
    }

    /** NEWPOSN for setting.  */
    void set(int newPosn) {
        final int k = 26;
        _setting = loopMod(newPosn, k);
    }

    /** CPOSN for char. */
    void set(char cPosn) {
        _setting = alphabet().toInt(cPosn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int finalVar = _permutation.permute(p + _setting % size());
        return loopMod(finalVar - _setting, size());
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int finalVar = _permutation.invert(e + _setting % size());
        return loopMod(finalVar - _setting, size());
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }


    /** Loopmod is a helper function used for a task.
     * @param z int.
     * @param k int.
     * @return outcome */
    int loopMod(int z, int k) {
        int outcome = z % k;
        if (outcome < 0) {
            outcome += k;
        }
        return outcome;
    }
    /** Permutation. */
    private Permutation _permutation;

    /** Setting. */
    private int _setting;

    /** Name of string. */
    private final String _name;

}
