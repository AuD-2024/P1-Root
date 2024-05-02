package p1;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.radix.LatinStringIndexExtractor;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class LatinStringIndexExtractorTest {
    @Test
    public void testValuesOutOfValueRange() {
        Context context = contextBuilder()
            .subject("LatinStringIndexExtractor.extractIndex")
            .build();


        assertEquals(19, new LatinStringIndexExtractor().extractIndex("testen", 999),
            context, result -> "Last index is selected if position is greater than string length");

        assertThrows(IndexOutOfBoundsException.class, () -> new LatinStringIndexExtractor().extractIndex("test", -5),
            context, result -> "IndexOutOfBoundsException is thrown if position is negative");
    }

    @Test
    public void getRadixTest() {
        Context context = contextBuilder()
            .subject("LatinStringIndexExtractor.getRadix")
            .build();

        assertEquals('z' - 'a' + 1, new LatinStringIndexExtractor().getRadix(),
            context, result -> "Last index is selected if position is greater than string length");
    }
}
