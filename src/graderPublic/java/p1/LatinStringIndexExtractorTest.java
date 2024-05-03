package p1;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.radix.LatinStringIndexExtractor;
import p1.transformers.MethodInterceptor;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class LatinStringIndexExtractorTest {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void checkIllegalMethods() {
        MethodInterceptor.reset();

       new LatinStringIndexExtractor().getRadix();
       new LatinStringIndexExtractor().extractIndex("Algorithmik", 4);

        IllegalMethodsCheck.checkMethods("^java/lang/String.+", "^java/lang/Character.+");
    }

    @Test
    public void testExtractIndexValid() {
        Context context = contextBuilder()
            .subject("LatinStringIndexExtractor.extractIndex")
            .build();


        assertEquals(4, new LatinStringIndexExtractor().extractIndex("testen", 1),
            context, result -> "Correct index from the middle of string is extracted");

        assertEquals(19, new LatinStringIndexExtractor().extractIndex("testen", 5),
            context, result -> "Correct index from the beginning of string is extracted");

        assertEquals(13, new LatinStringIndexExtractor().extractIndex("testen", 0),
            context, result -> "Correct index from the end of string is extracted");
    }

    @Test
    public void testExtractInvalidChar() {
        Context context = contextBuilder()
            .subject("LatinStringIndexExtractor.extractIndex")
            .build();

        assertEquals(0, new LatinStringIndexExtractor().extractIndex("testen!", 0),
            context, result -> "Correct index with invalid char is extracted");
    }
}
