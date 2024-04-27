package p1;

import p1.sort.radix.LatinStringIndexExtractor;
import p1.transformers.MethodInterceptor;

public class LatinStringIndexExtractorTest {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void checkIllegalMethods() {
        MethodInterceptor.reset();

       new LatinStringIndexExtractor().getRadix();
       new LatinStringIndexExtractor().extractIndex("Algorithmik", 4);

        IllegalMethodsCheck.checkMethods("^java/lang/String.+", "^java/lang/Character.+");
    }
}
