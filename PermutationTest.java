package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Nazli Urenli
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

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }
    @Test
    public void testNotInAlphabet() {
        Permutation p = new Permutation("(BACD)",
                new Alphabet("ABCD"));
        p.invert('E');
        p.invert('1');
        p.permute('E');
        p.permute('1');
    }
    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(BFHLZM) (ACTNK) "
                + "(ED) (RS) (WX) (P) (J) (G)",
                new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals('M', p.invert('B'));
        assertEquals('P', p.invert('P'));
        assertEquals('E', p.invert('D'));
        assertEquals('A', p.invert('C'));
        assertEquals('W', p.invert('X'));
        assertEquals('G', p.invert('G'));
        assertEquals('K', p.invert('A'));
    }
    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(BFHLZM) (ACTNK) (ED)"
                + " (RS) (WX) (P) (J) (G)",
                new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals('F', p.permute('B'));
        assertEquals('B', p.permute('M'));
        assertEquals('T', p.permute('C'));
        assertEquals('A', p.permute('K'));
        assertEquals('E', p.permute('D'));
        assertEquals('P', p.permute('P'));
        assertEquals('J', p.permute('J'));
    }
    @Test
    public void testInvertInt() {
        Permutation p = new Permutation("(BFHLZM) (ACTNK) (ED)"
                + " (RS) (WX) (P) (J) (G)",
                new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals(12, p.invert(1));
        assertEquals(15, p.invert(15));
        assertEquals(4, p.invert(3));
        assertEquals(0, p.invert(2));
        assertEquals(22, p.invert(23));
        assertEquals(16, p.invert(16));
        assertEquals(10, p.invert(0));
    }
    @Test
    public void testPermuteInt() {
        Permutation p = new Permutation("(BFHLZM) (ACTNK) (ED)"
                + " (RS) (WX) (P) (J) (G)",
                new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals(5, p.permute(1));
        assertEquals(1, p.permute(12));
        assertEquals(19, p.permute(2));
        assertEquals(0, p.permute(10));
        assertEquals(4, p.permute(3));
        assertEquals(15, p.permute(15));
        assertEquals(9, p.permute(9));
    }
}
