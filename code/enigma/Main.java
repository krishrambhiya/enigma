package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Krish Rambhiya
 */
public final class Main {

    /** Machine alphabet. */
    private Alphabet _alphabet;

    /** Type of input. */
    private Scanner _input;

    /** Type of scanner. */
    private Scanner _config;

    /** Encoded messages file. */
    private PrintStream _output;

    /** rotors available to us. */
    private ArrayList<Rotor> _alltheRotors = new ArrayList<>();

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine newerMachine = readConfig();
        String newsetting = _input.nextLine();
        String newmessage = "";
        setUp(newerMachine, newsetting);

        while (_input.hasNextLine()) {
            String newerString = _input.nextLine();
            if (newerString.contains("*")) {
                setUp(newerMachine, newerString);
            } else {
                newmessage = newerMachine.convert(
                        newerString.replaceAll(" ", ""));
                printMessageLine(newmessage);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String newAplh = _config.next();
            if (newAplh.contains("*")
                    || newAplh.contains(")") || newAplh.contains("(")) {
                throw new EnigmaException("Bad formatting for alphabet.");
            }
            _alphabet = new Alphabet(newAplh);
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();

            _alltheRotors = new ArrayList<>();
            while (_config.hasNext()) {
                _alltheRotors.add(readRotor());
            }

            return new Machine(_alphabet, numRotors, pawls, _alltheRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String newname = _config.next();
            String newtype = _config.next();
            String cycleset = "";
            while (_config.hasNext("\\(.*\\)")) {
                cycleset += _config.next();
            }
            Permutation perm = new Permutation(cycleset, _alphabet);
            switch (newtype.charAt(0)) {
            case 'M':
                String notches = "";
                for (int i = 1; i < newtype.length(); i++) {
                    notches += newtype.charAt(i);
                }
                return new MovingRotor(newname, perm, notches);
            case 'N':
                return new FixedRotor(newname, perm);
            case 'R':
                return new Reflector(newname, perm);
            default:
                throw new EnigmaException("Not in specified types.");
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] newsettingArr = settings.split(" ");
        if (newsettingArr.length - 1 < M.numRotors()) {
            throw new EnigmaException("Doesnt match description");
        }
        if (!newsettingArr[0].equals("*")) {
            throw new EnigmaException("should start with *");
        }
        String newcycles = "";
        String[] newrotors = new String[M.numRotors()];

        if (newsettingArr[0].equals("*")) {
            for (int i = 1; i < M.numRotors() + 1; i++) {
                newrotors[i - 1] = newsettingArr[i];
            }

            for (int i = 0; i < newrotors.length - 1; i++) {
                for (int j = i + 1; j < newrotors.length; j++) {
                    if (newrotors[i].equals(newrotors[j])) {
                        throw new EnigmaException("Rotor cannot repeat");
                    }
                }
            }

            for (int i = M.numRotors() + 2; i < newsettingArr.length; i++) {
                newcycles += newsettingArr[i];
            }
            Permutation q  = new Permutation(newcycles, _alphabet);
            M.setPlugboard(q);
            M.insertRotors(newrotors);
            M.setRotors(newsettingArr[M.numRotors() + 1]);
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        msg = msg.replace(" ", "");
        String newmessage = "";
        while (msg.length() > 5) {
            newmessage += msg.substring(0, 5) + " ";
            msg = msg.substring(5);
        }
        newmessage += msg;
        _output.println(newmessage);
    }
}
