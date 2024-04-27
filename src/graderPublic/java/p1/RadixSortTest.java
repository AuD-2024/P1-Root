package p1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.json.JsonClasspathSource;
import org.junitpioneer.jupiter.json.Property;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.sort.ArraySortList;
import p1.sort.SortList;
import p1.sort.radix.IntegerIndexExtractor;
import p1.sort.radix.LatinStringIndexExtractor;
import p1.sort.radix.RadixSort;
import p1.transformers.MethodInterceptor;

import java.util.List;
import java.util.Objects;

import static org.mockito.Mockito.spy;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;

@SuppressWarnings("DuplicatedCode")
@TestForSubmission
public class RadixSortTest {
    private static RadixSort<Integer> radixSort;

    @BeforeEach
    public void setup() {
        MethodInterceptor.reset();
        radixSort = spy(new RadixSort<>(26, new IntegerIndexExtractor(10)));
        radixSort.setMaxInputLength(500);
    }

    @AfterEach
    public void checkIllegalMethods() {
        IllegalMethodsCheck.checkMethods();
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_RadixSortTests.json", data = "twoItemsTest")
    public void testSortingTwoItems(@Property("values") List<Integer> values, @Property("expected") List<Integer> expected) {
        testSorting(values, expected);
    }

    @ParameterizedTest
    @JsonClasspathSource(value = "H5_RadixSortTests.json", data = "multipleItemsTest")
    public void testMultipleItems(@Property("values") List<Integer> values, @Property("expected") List<Integer> expected) {
        testSorting(values, expected);
    }

    private void testSorting(List<Integer> values, List<Integer> expected) {
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

        assertTrue(isSorted(sortList,expected), context,
            result -> "The sortList should be sorted after calling sort().");
    }

    private boolean isSorted(SortList<Integer> sortList,List<Integer> expected) {

        for (int i = 0; i < expected.size(); i++) {
            if (!Objects.equals(sortList.get(i), expected.get(i))) {
                return false;
            }
        }

        return true;
    }
}


