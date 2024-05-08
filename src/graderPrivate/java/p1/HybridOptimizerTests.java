package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.ArraySortList;
import p1.sort.HybridOptimizer;
import p1.sort.HybridSort;
import p1.transformers.MethodInterceptor;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertCallEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;

@TestForSubmission
public class HybridOptimizerTests {

    private static final Comparator<Integer> COMPARATOR = Comparator.naturalOrder();
    private static HybridSort<Integer> hybridSort;

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
        hybridSort = spy(new HybridSort<>(5, COMPARATOR));
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods(
            "^java/util/Arrays.+"
        );
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_HybridOptimizerTests.json", data = "sortCallTest")
    public void testSortCall(@Property("values") List<Integer> values,
                             @Property("reads") List<Integer> reads,
                             @Property("writes") List<Integer> writes,
                             @Property("expectedCalls") int expectedCalls) {

        Context context = contextBuilder()
            .subject("HybridOptimizer.optimize")
            .add("values", values)
            .build();

        AtomicInteger calls = new AtomicInteger(0);

        Answer<?> answer = invocation -> {

            ArraySortList<Integer> sortList = invocation.getArgument(0);

            calls.incrementAndGet();

            setReadCount(sortList, calls.get() == 0 ? 0 : reads.get(calls.get() - 1));
            setWriteCount(sortList, calls.get() == 0 ? 0 : writes.get(calls.get() - 1));

            assertEquals(calls.get() - 1, hybridSort.getK(), context,
                result -> "The k-value of the hybridSort object is wrong at the %dth call.".formatted(calls.get()));

            for (int i = 0; i < sortList.getSize(); i++) {
                int finalI = i;
                assertEquals(values.get(i), sortList.get(i), context,
                    result -> ("The sortList contains the wrong value at index %d. " +
                        "Note that the sortList might have been modified by an previous call to sort()").formatted(finalI));

                sortList.set(i, -1); // fake sorting to check if a new sortList is created each time sort() is called
            }

            if (calls.get() > values.size() + 2) {
                fail(context, TR -> "The sort() method was called more often than necessary in the worst case (array.length + 2).");
            }


            return null;
        };

        ArgumentCaptor<Integer> mergeSortLeftCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> mergeSortRightCaptor = ArgumentCaptor.forClass(Integer.class);

        doAnswer(answer).when(hybridSort).sort(any());
        doAnswer(answer).when(hybridSort).mergeSort(any(), mergeSortLeftCaptor.capture(), mergeSortRightCaptor.capture());

        HybridOptimizer.optimize(hybridSort, values.toArray(Integer[]::new));

        assertEquals(expectedCalls, calls.get(), context,
            result -> "the amount of calls to the sort method is not correct.");

        for (Integer leftValue : mergeSortLeftCaptor.getAllValues()) {
            assertEquals(0, leftValue, context,
                result -> "the left value of the mergeSort call is not correct.");
        }

        for (Integer rightValue : mergeSortRightCaptor.getAllValues()) {
            assertEquals(values.size() - 1, rightValue, context,
                result -> "the right value of the mergeSort call is not correct.");
        }

    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_HybridOptimizerTests.json", data = "nonMonotoneTest")
    public void testNonMonotone(@Property("values") List<Integer> values,
                                @Property("reads") List<Integer> reads,
                                @Property("writes") List<Integer> writes,
                                @Property("expected") int expected) {

        testOptimize(values, reads, writes, expected);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_HybridOptimizerTests.json", data = "strictlyMonotoneTest")
    public void testStrictlyMonotone(@Property("values") List<Integer> values,
                                     @Property("reads") List<Integer> reads,
                                     @Property("writes") List<Integer> writes,
                                     @Property("expected") int expected) {

        testOptimize(values, reads, writes, expected);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_HybridOptimizerTests.json", data = "monotoneTest")
    public void testMonotone(@Property("values") List<Integer> values,
                             @Property("reads") List<Integer> reads,
                             @Property("writes") List<Integer> writes,
                             @Property("expected") int expected) {

        testOptimize(values, reads, writes, expected);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_HybridOptimizerTests.json", data = "globalNotLocalTest")
    public void testGlobalNotLocal(@Property("values") List<Integer> values,
                                   @Property("reads") List<Integer> reads,
                                   @Property("writes") List<Integer> writes,
                                   @Property("expected") int expected) {

        testOptimize(values, reads, writes, expected);
    }

    private void testOptimize(List<Integer> values,
                              List<Integer> reads,
                              List<Integer> writes,
                              int expected) {

        Context context = contextBuilder()
            .subject("HybridOptimizer.optimize")
            .add("values", values)
            .add("reads", reads)
            .add("writes", writes)
            .build();

        AtomicInteger calls = new AtomicInteger(0);

        Answer<?> answer = invocation -> {

            ArraySortList<Integer> sortList = invocation.getArgument(0);

            calls.incrementAndGet();

            setReadCount(sortList, calls.get() == 0 ? 0 : reads.get(calls.get() - 1));
            setWriteCount(sortList, calls.get() == 0 ? 0 : writes.get(calls.get() - 1));

            return null;
        };

        doAnswer(answer).when(hybridSort).sort(any());

        doAnswer(answer).when(hybridSort).mergeSort(any(), anyInt(), anyInt());

        assertCallEquals(expected, () -> HybridOptimizer.optimize(hybridSort, values.toArray(Integer[]::new)), context,
            result -> "The return value of the optimize() method is wrong.");

    }

    private void setReadCount(ArraySortList<?> sortList, int value) throws ReflectiveOperationException {
        Field field = ArraySortList.class.getDeclaredField("readCount");
        field.setAccessible(true);
        field.set(sortList, value);
    }

    private void setWriteCount(ArraySortList<?> sortList, int value) throws ReflectiveOperationException {
        Field field = ArraySortList.class.getDeclaredField("writeCount");
        field.setAccessible(true);
        field.set(sortList, value);
    }

}
