package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.ArraySortList;
import p1.sort.HybridSort;
import p1.sort.SortList;
import p1.transformers.MethodInterceptor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.call;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static p1.OperationSortList.Operation.toHTMLList;

@TestForSubmission
public class BubbleSortTests {

    private static final Comparator<Integer> COMPARATOR = Comparator.naturalOrder();
    private static final HybridSort<Integer> HYBRID_SORT = new HybridSort<>(5, COMPARATOR);

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods();
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H3_BubbleSortTests.json", data = "alreadySortedTest")
    public void testAlreadySorted(@Property("values") List<Integer> values) {
        testSorting(values, 0, values.size() - 1, values);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H3_BubbleSortTests.json", data = "oneItemTest")
    public void testOneItem(@Property("value") Integer value) {
        Context context = contextBuilder()
            .subject("HybridSort#bubbleSort()")
            .add("values", "[%d]".formatted(value))
            .add("left", 0)
            .add("right", 0)
            .add("comparator", "natural_order")
            .build();

        SortList<Integer> sortList = new ArraySortList<>(List.of(value));
        call(() -> HYBRID_SORT.bubbleSort(sortList, 0, 0), context,
            result -> "bubbleSort() should not throw an exception.");

        assertTrue(Objects.equals(sortList.get(0), value), context,
            result -> "Calling bubbleSort() on a sortList with one item should not change the value.");
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H3_BubbleSortTests.json", data = "twoItemsTest")
    public void testTwoItems(@Property("values") List<Integer> values) {
        testSorting(values, 0, values.size() - 1, values.stream().sorted(COMPARATOR).toList());
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H3_BubbleSortTests.json", data = "multipleItemsTest")
    public void testMultipleItems(@Property("values") List<Integer> values) {
        testSorting(values, 0, values.size() - 1, values.stream().sorted(COMPARATOR).toList());
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H3_BubbleSortTests.json", data = "boundsTest")
    public void testBounds(@Property("values") List<Integer> values,
                           @Property("left") Integer left,
                           @Property("right") Integer right,
                           @Property("expected") List<Integer> expected) {

        testSorting(values, left, right, expected);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H3_BubbleSortTests.json", data = "operationOrderTest")
    public void testOperationOrder(@Property("values") List<Integer> values,
                                   @Property("left") Integer left,
                                   @Property("right") Integer right,
                                   @Property("operations") List<String> operations) {

        List<OperationSortList.Operation> ops = operations.stream().map(OperationSortList.Operation::of).toList();

        OperationSortList sortList = new OperationSortList(values);
        call(() -> HYBRID_SORT.bubbleSort(sortList, left, right), contextBuilder()
            .subject("HybridSort#bubbleSort()")
            .add("values", values)
            .add("left", left)
            .add("right", right)
            .add("comparator", "natural_order")
            .build(),
            result -> "bubbleSort() should not throw an exception.");

        Context context = contextBuilder()
            .subject("HybridSort#bubbleSort()")
            .add("values", values)
            .add("left", left)
            .add("right", right)
            .add("comparator", "natural_order")
            .add("actual operations", toHTMLList(sortList.operations))
            .add("expected operations", toHTMLList(ops))
            .build();

        assertEquals(ops.size(), sortList.operations.size(), context,
            result -> "bubbleSort() did not perform the correct amount of read and write operations on the sortList.");

        for (int i = 0; i < ops.size(); i++) {
            int finalI = i;
            assertEquals(ops.get(i), sortList.operations.get(i), context,
                result -> "bubbleSort() did not perform the correct operations on the sortList at index %d.".formatted(finalI));
        }
    }

    private void testSorting(List<Integer> values, Integer left, Integer right, List<Integer> expected) {
        SortList<Integer> sortList = new ArraySortList<>(values);
        call(() -> HYBRID_SORT.bubbleSort(sortList, left, right), contextBuilder()
            .subject("HybridSort#bubbleSort()")
            .add("values", values)
            .add("left", left)
            .add("right", right)
            .add("comparator", "natural_order")
            .build(),
            result -> "bubbleSort() should not throw an exception.");

        Context context = contextBuilder()
            .subject("HybridSort#bubbleSort()")
            .add("values", values)
            .add("left", left)
            .add("right", right)
            .add("comparator", "natural_order")
            .add("actual", sortList)
            .add("expected", expected)
            .build();

        assertTrue(isSorted(sortList, left, right, expected), context,
            result -> "The sortList should be sorted between the indices %d and %d (inclusive) after calling bubbleSort()."
                .formatted(left, right));
    }

    private boolean isSorted(SortList<Integer> sortList, Integer left, Integer right, List<Integer> expected) {

        for (int i = left; i < right; i++) {
            if (!Objects.equals(sortList.get(i), expected.get(i))) {
                return false;
            }
        }

        return true;
    }

}
