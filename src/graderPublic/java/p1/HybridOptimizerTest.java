package p1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.stubbing.Answer;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.ArraySortList;
import p1.sort.HybridOptimizer;
import p1.sort.HybridSort;
import p1.sort.SortList;
import p1.transformers.MethodInterceptor;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class HybridOptimizerTest {
    private static final Comparator<Integer> COMPARATOR = Comparator.naturalOrder();

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
    }

    public void checkIllegalMethods() {
        MethodInterceptor.reset();
        HybridOptimizer.optimize(new HybridSort<>(3, Integer::compare), new Integer[]{5, 4, 3, 2, 1});
        IllegalMethodsCheck.checkMethods("^java/util/Arrays.+");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H2_OptimizerTest.json", data = "sortCallTest")
    public void testSortCall(@Property("values") List<Integer> values,
                             @Property("reads") List<Integer> reads,
                             @Property("writes") List<Integer> writes,
                             @Property("expectedCalls") int expectedCalls) {

        Context context = contextBuilder()
            .subject("HybridOptimizer.optimize")
            .add("values", values)
            .build();

        HybridSort<Integer> hybridSort = spy(new HybridSort<>(5, COMPARATOR));
        AtomicInteger calls = new AtomicInteger(0);

        Answer<?> answer = invocation -> {

            SortList<Integer> sortList = invocation.getArgument(0);

            assertEquals(calls.get(), hybridSort.getK(), context,
                result -> "The k-value of the hybridSort object is wrong at the %dth call.".formatted(calls.get() + 1));

            for (int i = 0; i < sortList.getSize(); i++) {
                int finalI = i;
                assertEquals(values.get(i), sortList.get(i), context,
                    result -> ("The sortList contains the wrong value at index %d. " +
                        "Note that the sortList might have been modified by an previous call to sort()").formatted(finalI));

                sortList.set(i, -1); // fake sorting to check if a new sortList ist created each time sort() is called
            }

            calls.incrementAndGet();

            if (calls.get() > values.size() + 2) {
                fail(context, TR -> "The sort() method was called more often than necessary in the worst case (array.length + 2).");
            }


            return null;
        };

        ArgumentCaptor<Integer> mergeSortLeftCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> mergeSortRightCaptor = ArgumentCaptor.forClass(Integer.class);

        doAnswer(answer).when(hybridSort).sort(any());
        doAnswer(answer).when(hybridSort).mergeSort(any(), mergeSortLeftCaptor.capture(), mergeSortRightCaptor.capture());

        //noinspection rawtypes
        try (MockedConstruction<ArraySortList> ignored = mockConstruction(ArraySortList.class, (mock, creationContext) -> {

            SortList<Integer> sortList = (SortList<Integer>) mock;

            Integer[] actualValues;

            if (creationContext.arguments().get(0) instanceof Integer[] arr) {
                actualValues = arr;
            } else {
                actualValues = ((List<Integer>) creationContext.arguments().get(0)).toArray(Integer[]::new);
            }

            when(sortList.getReadCount()).thenAnswer(invocation -> calls.get() == 0 ? 0 : reads.get(calls.get() - 1));
            when(sortList.getWriteCount()).thenAnswer(invocation -> calls.get() == 0 ? 0 : writes.get(calls.get() - 1));
            when(sortList.getSize()).thenReturn(values.size());
            when(sortList.get(anyInt())).thenAnswer(invocation -> actualValues[(int) invocation.getArgument(0)]);

        })) {
            HybridOptimizer.optimize(hybridSort, values.toArray(Integer[]::new));
        }

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
}
