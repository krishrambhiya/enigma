package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */
    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testsize() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(4, p.size());
        p = new Permutation("(BACD)", new Alphabet("ABCDEF"));
        assertEquals(6, p.size());
    }


    @Test
    public void testpermuteint() {
        Permutation p = new Permutation("(BACD)",  new Alphabet("ABCD"));
        assertEquals(2, p.permute(0));
        assertEquals(0, p.permute(1));
        assertEquals(3, p.permute(2));
        assertEquals(2, p.permute(4));
        p = new Permutation("(BACD)", new Alphabet("ABCDEF"));
        assertEquals(5, p.permute(5));
    }

    @Test
    public void testinvertint() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(1, p.invert(0));
        assertEquals(3, p.invert(1));
        assertEquals(0, p.invert(2));
        assertEquals(3, p.invert(5));
        p = new Permutation("(BACD)", new Alphabet("ABCDEF"));
        assertEquals(5, p.permute(5));
    }

    @Test
    public void testpermutechar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals('C', p.permute('A'));
        assertEquals('A', p.permute('B'));
        assertEquals('B', p.permute('D'));
        p = new Permutation("(BACD)", new Alphabet("ABCDEF"));
        assertEquals('F', p.invert('F'));
    }

    @Test
    public void testinvertchar() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals('B', p.invert('A'));
        assertEquals('D', p.invert('B'));
        assertEquals('A', p.invert('C'));
        p = new Permutation("(BACD)", new Alphabet("ABCDEF"));
        assertEquals('F', p.invert('F'));
    }

    @Test
    public void testalphabet() {
        Alphabet newalpha = new Alphabet("ABCD");
        Permutation p = new Permutation("(BACD)", newalpha);
        assertEquals(newalpha, p.alphabet());
    }

    @Test
    public void testderangement() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(true, p.derangement());
        p = new Permutation("(BAC)", new Alphabet("ABCD"));
        assertEquals(false, p.derangement());
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        Permutation p = new Permutation("(BACD)", new Alphabet("ABCD"));
        p.invert('F');
    }

}
