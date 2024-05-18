package p1.sort.radix;

/**
 * A {@link RadixIndexExtractor} for extracting the index corresponding to a character in a string.
 *
 * <p>It is case-insensitive. It maps the characters 'a' to 'z' to the indices 0 to 25. All other characters are mapped to 0.
 * The position is interpreted as the position from the end of the string, i.e. position 0 corresponds to the last
 * character in the string.
 */
public class LatinStringIndexExtractor implements RadixIndexExtractor<String> {

    @Override
    public int extractIndex(String value, int position) {

        if (position < 0) {
            throw new IndexOutOfBoundsException("The position must be greater than or equal to 0.");
        }

        if (position > value.length() - 1) {
            throw new IndexOutOfBoundsException("The position must be less than the length of given String.");
        }

        char c = Character.toLowerCase(value.charAt(value.length() - position - 1));

        if (c < 'a' || c > 'z') {
            return 0;
        }

        return c - 'a';
    }

    @Override
    public int getRadix() {
        return 'z' - 'a' + 1; //26
    }
}
