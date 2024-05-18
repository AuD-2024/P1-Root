package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.ArraySortList;
import p1.sort.SortList;
import p1.sort.radix.IntegerIndexExtractor;
import p1.sort.radix.RadixSort;
import p1.transformers.MethodInterceptor;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class RadixSortTest {
    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods();
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "putBucketTest")
    public void testPutBucket(@Property("value") Integer value, @Property("index") Integer index) {
        IntegerIndexExtractor extractor = spy(new IntegerIndexExtractor(10));
        RadixSort<Integer> radixSort = spy(new RadixSort<>(10, extractor));
        radixSort.setMaxInputLength(1);

        Context context = contextBuilder()
            .subject("RadixSort#putBucket()")
            .build();

        radixSort.putBucket(value, index);

        checkVerify(() -> verify(extractor, times(1)).extractIndex(value, index), context,
            "Extract Index was not called with the expected values.");
    }

    @ParameterizedTest()
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "singleBucketTest")
    public void oneBucketEntryMaxLength1(@Property("values") List<Integer> values, @Property("expected") List<Integer> expected) {
        testSorting(values, expected, 1, 1);
    }

    @ParameterizedTest()
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "multipleItemsTest")
    public void testMultipleInputLength(@Property("values") List<Integer> values, @Property("expected") List<Integer> expected) {
        testSorting(values, expected, 5, 10);
    }

    @ParameterizedTest()
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "oneBucketTest")
    public void testOneBucketMultipleEntries(@Property("values") List<Integer> values, @Property("expected") List<Integer> expected) {
        testSorting(values, expected, 1, 10);
    }

    private void testSorting(List<Integer> values, List<Integer> expected, Integer maxInputLength, Integer radix) {
        RadixSort<Integer> radixSort = new RadixSort<>(radix, new IntegerIndexExtractor(radix));
        radixSort.setMaxInputLength(maxInputLength);

        SortList<Integer> sortList = new ArraySortList<>(values);
        call(() -> radixSort.sort(sortList), contextBuilder()
                .subject("RadixSort#sort()")
                .add("values", values)
                .build(),
            result -> "sort() should not throw an exception.");

        Context context = contextBuilder()
            .subject("RadixSort#sort()")
            .add("values", values)
            .add("actual", sortList)
            .add("expected", expected)
            .build();

        isSorted(context, sortList, expected);
    }

    private void isSorted(Context context, SortList<Integer> sortList, List<Integer> expected) {
        for (int i = 0; i < expected.size(); i++) {
            int finalI = i;
            assertEquals(expected.get(i), sortList.get(i), context, result -> "sortList contains wrong value at index %d.".formatted(finalI));
        }
    }

    private void checkVerify(Runnable verifier, Context context, String msg) {
        try {
            verifier.run();
        } catch (MockitoAssertionError e) {
            fail(context, result -> msg + " Original error message:\n" + e.getMessage());
        } catch (Exception e) {
            fail(context, result -> "Unexpected Exception:\n" + e.getMessage());
        }
    }
}
