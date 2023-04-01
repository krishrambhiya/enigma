package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Krish Rambhiya
 */
class Machine {
    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        if (numRotors <= 1) {
            throw new EnigmaException("Invalid number of rotors or pawls.");
        }
        _pawls = pawls;
        if (pawls < 0 || pawls >= numRotors) {
            throw new EnigmaException("Error.");
        }
        _allRotors = new ArrayList<Rotor>();
        for (Rotor rotor: allRotors) {
            _allRotors.add(rotor);
        }
        _myrotorVals = new Rotor[_numRotors];
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        if (numRotors() != rotors.length) {
            throw new EnigmaException("not exact number of rotors.");
        } else {
            for (int j = 0; j < _allRotors.size(); j++) {
                for (int k = 0; k < rotors.length; k++) {
                    Rotor currentRotor = _allRotors.get(j);
                    if (currentRotor.name().equals(rotors[k])) {
                        _myrotorVals[k] = currentRotor;
                    }
                }
            }
        }
        if (!_myrotorVals[0].reflecting()) {
            throw new EnigmaException("error.");
        }
    }



    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (_numRotors - 1 != setting.length()) {
            throw new EnigmaException("Length is wrong for Rotor setting.");
        }
        for (int i = 1; i < _numRotors; i += 1) {
            char characSetting = setting.charAt(i - 1);
            if (!_alphabet.contains(characSetting)) {
                throw new
                        EnigmaException("character not contained in alphabet.");
            }
            _myrotorVals[i].set(characSetting);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboardVar = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        if (c > _alphabet.size() - 1 || c < 0) {
            throw new EnigmaException("error");
        }
        boolean[] movementPoss = new boolean[_numRotors];
        for (int k = 0; k < _myrotorVals.length; k++) {
            if (k == _myrotorVals.length - 1) {
                movementPoss[k] = true;
            } else if (_myrotorVals[k].rotates()
                    && _myrotorVals[k + 1].atNotch()) {
                movementPoss[k] = true;
            } else {
                movementPoss[k] = false;
            }
        }

        for (int k = 0; k < _myrotorVals.length; k += 1) {
            if (movementPoss[k]) {
                _myrotorVals[k].advance();
                if (k < _myrotorVals.length - 1) {
                    _myrotorVals[k + 1].advance();
                    k += 1;
                }
            }
        }

        c = _plugboardVar.permute(c);
        for (int i = _myrotorVals.length - 1; i >= 0; i--) {
            c = _myrotorVals[i].convertForward(c);
        }
        for (int k = 1; k < _myrotorVals.length; k++) {
            c = _myrotorVals[k].convertBackward(c);
        }
        return _plugboardVar.invert(c);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String conversionOut = "";
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == ' ') {
                conversionOut += msg.charAt(i);
            } else {
                int conversionInt = _alphabet.toInt(msg.charAt(i));
                conversionOut += _alphabet.toChar(convert(conversionInt));
            }
        }
        return conversionOut;
    }


    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** number of rotors. */
    private int _numRotors;

    /** number of pawls. */
    private int _pawls;

    /** arraylist of all rotors. */
    private ArrayList<Rotor> _allRotors;

    /** array of rotors. */
    private Rotor[] _myrotorVals;

    /** plugboard. */
    private Permutation _plugboardVar;

}
