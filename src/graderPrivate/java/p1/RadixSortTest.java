package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.mockito.ArgumentCaptor;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.ArraySortList;
import p1.sort.SortList;
import p1.sort.radix.Bucket;
import p1.sort.radix.IntegerIndexExtractor;
import p1.sort.radix.RadixSort;
import p1.transformers.MethodInterceptor;

import java.lang.reflect.Field;
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
    public void testPutBucket(@Property("value") Integer value, @Property("position") Integer position) throws ReflectiveOperationException {

        int radix = 10;

        IntegerIndexExtractor extractor = spy(new IntegerIndexExtractor(radix));
        RadixSort<Integer> radixSort = spy(new RadixSort<>(radix, extractor));
        radixSort.setMaxInputLength(1);

        int index = new IntegerIndexExtractor(radix).extractIndex(value, position);

        Context context = contextBuilder()
            .subject("RadixSort#putBucket()")
            .add("value", value)
            .add("position", position)
            .add("index", index)
            .build();

        call(() -> radixSort.putBucket(value, position), context, TR -> "putBucket() should not throw an exception.");

        checkVerify(() -> verify(extractor, times(1)).extractIndex(value, position), context,
            "extractIndex was not called with the expected values.");

        Bucket<Integer>[] buckets = getBuckets(radixSort);

        for (int i = 0; i < radix; i++) {
            int finalI = i;

            if (i == index) {
                assertEquals(1, buckets[i].size(), context, result -> "Bucket at position %d should contain exactly one element.".formatted(finalI));
                assertEquals(value, buckets[i].remove(), context, result -> "Bucket at position %d should contain the value %d.".formatted(finalI, value));
            } else {
                assertEquals(0, buckets[i].size(), context, result -> "Bucket at position %d should be empty.".formatted(finalI));
            }

        }
    }

    @ParameterizedTest()
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "putBucketCallMaxLength1Test")
    public void testPutBucketCallMaxLength1(@Property("values") List<Integer> values) {
        testPutBucketCall(values, 1, 10);
    }

    @ParameterizedTest()
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "putBucketCallTest")
    public void testPutBucketCall(@Property("values") List<Integer> values, @Property("maxInputLength") Integer maxInputLength) {
        testPutBucketCall(values, maxInputLength, 10);
    }

    private void testPutBucketCall(List<Integer> values, int maxInputLength, int radix) {
        Context context = contextBuilder()
            .subject("RadixSort#sort()")
            .add("values", values)
            .add("maxInputLength", maxInputLength)
            .build();

        RadixSort<Integer> radixSort = spy(new RadixSort<>(radix, new IntegerIndexExtractor(radix)));
        radixSort.setMaxInputLength(maxInputLength);

        ArgumentCaptor<Integer> valueCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> positionCaptor = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(radixSort).putBucket(valueCaptor.capture(), positionCaptor.capture());
        doCallRealMethod().doNothing().when(radixSort).sort(any());

        SortList<Integer> sortList = new ArraySortList<>(values);
        call(() -> radixSort.sort(sortList), context, result -> "sort() should not throw an exception.");

        List<Integer> capturedValues = valueCaptor.getAllValues();
        List<Integer> capturedPositions = positionCaptor.getAllValues();

        int expectedCalls = values.size() * maxInputLength;

        if (capturedValues.size() != expectedCalls) {
            fail(contextBuilder()
                .subject("RadixSort#sort()")
                .add("values", values)
                .add("maxInputLength", maxInputLength)
                .add("expected calls", expectedCalls)
                .add("actual calls", capturedValues.size())
                .build(), result -> "putBucket() was not called the expected amount of times.");
        }

        for (int i = 0; i < values.toArray().length; i++) {
            int finalI = i;
            assertEquals(values.get(i), capturedValues.get(i), context,
                result -> "putBucket() was called with the wrong value at the %d-th call.".formatted(finalI + 1));
            assertEquals(i / values.size(), capturedPositions.get(i), context,
                result -> "putBucket() was called with the wrong position at the %d-th call.".formatted(finalI + 1));
        }
    }

    @ParameterizedTest()
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "oneBucketOneEntryTest")
    public void testOneBucketOneEntry(@Property("buckets") List<List<Integer>> buckets, @Property("expected") List<Integer> expected, @Property("radix") int radix) throws ReflectiveOperationException {
        testBucketExtraction(buckets, expected, radix);
    }

    @ParameterizedTest()
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "oneBucketMultipleEntriesTest")
    public void testOneBucketMultipleEntries(@Property("buckets") List<List<Integer>> buckets, @Property("expected") List<Integer> expected, @Property("radix") int radix) throws ReflectiveOperationException {
        testBucketExtraction(buckets, expected, radix);
    }

    @ParameterizedTest()
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "multipleBucketsOneEntryTest")
    public void testMultipleBucketsOneEntry(@Property("buckets") List<List<Integer>> buckets, @Property("expected") List<Integer> expected, @Property("radix") int radix) throws ReflectiveOperationException {
        testBucketExtraction(buckets, expected, radix);
    }

    private void testBucketExtraction(List<List<Integer>> buckets, List<Integer> expected, Integer radix) throws ReflectiveOperationException {
        RadixSort<Integer> radixSort = spy(new RadixSort<>(radix, new IntegerIndexExtractor(radix)));
        radixSort.setMaxInputLength(1);

        doNothing().when(radixSort).putBucket(any(), anyInt());

        Bucket<Integer>[] bucketArray = getBuckets(radixSort);

        for (int i = 0; i < radix; i++) {
            for (Integer value : buckets.get(i)) {
                bucketArray[i].add(value);
            }
        }

        Context context = contextBuilder()
            .subject("RadixSort#sort()")
            .add("buckets", buckets)
            .add("expected", expected)
            .build();

        SortList<Integer> sortList = new ArraySortList<>(expected.size());
        call(() -> radixSort.sort(sortList), context, result -> "sortBuckets() should not throw an exception.");

        context = contextBuilder()
            .subject("RadixSort#sort()")
            .add("buckets", buckets)
            .add("expected", expected)
            .add("actual", sortList)
            .build();

        for (int i = 0; i < radix; i++) {
            int finalI = i;
            assertEquals(0, bucketArray[i].size(), context,
                    result -> "Bucket at position %d should be empty.".formatted(finalI));
        }

        for (int i = 0; i < expected.size(); i++) {
            int finalI = i;
            assertEquals(expected.get(i), sortList.get(i), context,
                    result -> "sortList contains wrong value at index %d.".formatted(finalI));
        }
    }

    @ParameterizedTest()
    @JsonClasspathSource(value = "H7_RadixSortTests.json", data = "sortingTest")
    public void testSorting(@Property("values") List<Integer> values,
                            @Property("expected") List<Integer> expected,
                            @Property("radix") Integer radix,
                            @Property("maxInputLength") Integer maxInputLength) {

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
        } catch (AssertionError e) {
            fail(context, result -> msg + " Original error message:\n" + e.getMessage());
        } catch (Exception e) {
            fail(context, result -> "Unexpected Exception:\n" + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private <T> Bucket<T>[] getBuckets(RadixSort<T> radixSort) throws ReflectiveOperationException {
        Field field = RadixSort.class.getDeclaredField("buckets");
        field.setAccessible(true);
        return (Bucket<T>[]) field.get(radixSort);
    }
}
