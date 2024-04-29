package p1.sort.radix;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * An {@link RadixIndexExtractor} for extracting the index corresponding to a character in a string.
 *
 * <p>It is case-insensitive. It maps the characters 'a' to 'z' to the indices 0 to 25. All other characters are mapped to 0.
 * The position is interpreted as the position from the end of the string, i.e. position 0 corresponds to the last
 * character in the string.
 */
public class LatinStringIndexExtractor implements RadixIndexExtractor<String> {

    @Override
    public int extractIndex(String value, int position) {
        return crash(); //TODO: H3  a) - remove if implemented
    }

    @Override
    public int getRadix() {
        return 'z' - 'a' + 1; //26
    }
}
