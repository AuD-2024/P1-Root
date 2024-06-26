package p1;

import org.sourcegrade.jagr.api.rubric.*;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import p1.card.CardColor;
import p1.transformers.MethodInterceptorTransformer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unused")
public class P1_RubricProvider implements RubricProvider {
    @SafeVarargs
    private static Criterion createCriterion(String shortDescription, int maxPoints, Callable<Method>... methodReferences) {

        if (methodReferences.length == 0) {
            return Criterion.builder()
                .shortDescription(shortDescription)
                .maxPoints(maxPoints)
                .build();
        }

        Grader.TestAwareBuilder graderBuilder = Grader.testAwareBuilder();

        for (Callable<Method> reference : methodReferences) {
            graderBuilder.requirePass(JUnitTestRef.ofMethod(reference));
        }

        return Criterion.builder()
            .shortDescription(shortDescription)
            .grader(graderBuilder
                .pointsFailedMin()
                .pointsPassedMax()
                .build())
            .maxPoints(maxPoints)
            .build();
    }

    private static Criterion createParentCriterion(String task, String shortDescription, Criterion... children) {
        return Criterion.builder()
            .shortDescription("H" + task + " | " + shortDescription)
            .addChildCriteria(children)
            .build();
    }

    public static final Criterion H1_1_1 = createCriterion("Die Methode [[[compare]]] der Klasse CardComparator funktioniert vollständig korrekt.", 1,
        () -> CardComparatorTest.class.getMethod("testColor", List.class, List.class, List.class),
        () -> CardComparatorTest.class.getMethod("testValue", CardColor.class, List.class, List.class));

    public static final Criterion H1_1 = createParentCriterion("1 a)", "CardComparator", H1_1_1);

    public static final Criterion H1_2_1 = createCriterion("Die Methode [[[compare]]] der Klasse CountingComparator funktioniert korrekt", 1,
        () -> CountingComparatorTest.class.getMethod("testCompare", Integer.class, Integer.class));

    public static final Criterion H1_2_2 = createCriterion("Die Methoden [[[reset]]] und [[[getComparisonsCount]]] der Klasse CountingComparator funktionieren korrekt", 1,
        () -> CountingComparatorTest.class.getMethod("testReset", Integer.class),
        () -> CountingComparatorTest.class.getMethod("testGetComparisonsCount", Integer.class));

    public static final Criterion H1_2 = createParentCriterion("1 b)", "CountingComparator", H1_2_1, H1_2_2);

    public static final Criterion H1 = createParentCriterion("1", "Comparators", H1_1, H1_2);

    public static final Criterion H2_1_1 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe bereits sortiert ist", 1,
        () -> BubbleSortTests.class.getMethod("testAlreadySorted", List.class));

    public static final Criterion H2_1_2 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nur ein Element enthält", 1,
        () -> BubbleSortTests.class.getMethod("testOneItem", Integer.class));

    public static final Criterion H2_1_3 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe zwei Elemente enthält", 1,
        () -> BubbleSortTests.class.getMethod("testTwoItems", List.class));

    public static final Criterion H2_1_4 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe mehr als zwei Elemente enthält", 1,
        () -> BubbleSortTests.class.getMethod("testMultipleItems", List.class));

    public static final Criterion H2_1_5 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort verändert nur Werte im Indexbereich [left, right]", 1,
        () -> BubbleSortTests.class.getMethod("testBounds", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_1_6 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort verwendet die korrekten Lese- und Schreiboperationen in der korrekten Reihenfolge", 1,
        () -> BubbleSortTests.class.getMethod("testOperationOrder", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_1 = createParentCriterion("2 a)", "BubbleSort", H2_1_1, H2_1_2, H2_1_3, H2_1_4, H2_1_5, H2_1_6);

    public static final Criterion H2_2_1 = createCriterion("Die Methode [[[mergeSort]]] der Klasse HybridSort ruft, wenn notwendig, die Methode bubblesort mit den korrekten Werten auf", 1,
        () -> MergeSortTest.class.getMethod("testBubbleSort", List.class, Integer.class, Integer.class, Integer.class, Boolean.class));

    public static final Criterion H2_2_2 = createCriterion("Die Methode [[[mergeSort]]] der Klasse HybridSort ruft, wenn notwendig, die Methode [[[merge]]] und sich selber mit den korrekten Werten auf", 1,
        () -> MergeSortTest.class.getMethod("testMergeSortRecursion", List.class, Integer.class, Integer.class, Integer.class, Boolean.class, Integer.class));

    public static final Criterion H2_2_3 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe bereits gemerged ist", 1,
        () -> MergeSortTest.class.getMethod("testAlreadyMerged", List.class, Integer.class, Integer.class));

    public static final Criterion H2_2_4 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nicht gemerged ist und zwei Elemente enthält", 1,
        () -> MergeSortTest.class.getMethod("testMergeTwoItems", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_2_5 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nicht gemerged ist und drei Elemente enthält", 1,
        () -> MergeSortTest.class.getMethod("testMergeThreeItems", List.class, Integer.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_2_6 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert vollständig korrekt", 1,
        () -> MergeSortTest.class.getMethod("testAlreadyMerged", List.class, Integer.class, Integer.class),
        () -> MergeSortTest.class.getMethod("testMergeTwoItems", List.class, Integer.class, Integer.class, List.class),
        () -> MergeSortTest.class.getMethod("testMergeThreeItems", List.class, Integer.class, Integer.class, Integer.class, List.class),
        () -> MergeSortTest.class.getMethod("testMergeMultipleItems", List.class, Integer.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_2 = createParentCriterion("2 b)", "MergeSort", H2_2_1, H2_2_2, H2_2_3, H2_2_4, H2_2_5, H2_2_6);

    public static final Criterion H2_3_1 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer ruft die [[[sort]]] Methode mit korrekten Werten in der richtigen Reihenfolge auf", 1,
        () -> HybridOptimizerTest.class.getMethod("testSortCall", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3_2 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte nicht monoton sind und es nur ein Minimum gibt", 1,
        () -> HybridOptimizerTest.class.getMethod("testNonMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3_3 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte streng monoton fallend sind", 1,
        () -> HybridOptimizerTest.class.getMethod("testStrictlyMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3_4 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte monoton fallend sind", 1,
        () -> HybridOptimizerTest.class.getMethod("testMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3_5 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte nicht monoton sind und es mehrere Minima gibt", 1,
        () -> HybridOptimizerTest.class.getMethod("testGlobalNotLocal", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3 = createParentCriterion("2 c)", "HybridOptimizer", H2_3_1, H2_3_2, H2_3_3, H2_3_4, H2_3_5);

    public static final Criterion H2 = createParentCriterion("2", "Sortieralgorithmen", H2_1, H2_2, H2_3);

    public static final Criterion H3_1_1 = createCriterion("Die Methode [[[extractIndex]]] der Klasse LatinStringIndexExtractor funktioniert korrekt wenn die Position innerhalb des Indexbereiches des String liegt und das Zeichen ein gültiger Buchstabe ist", 2,
        () -> LatinStringIndexExtractorTest.class.getDeclaredMethod("testExtractValidIndex", String.class, int.class, int.class));

    public static final Criterion H3_1_2 = createCriterion("Die Methode [[[extractIndex]]] der Klasse LatinStringIndexExtractor funktioniert korrekt wenn die Position innerhalb des Indexbereiches des String liegt und das Zeichen kein gültiger Buchstabe ist", 1,
        () -> LatinStringIndexExtractorTest.class.getDeclaredMethod("testExtractInvalidChar", String.class, int.class));

    public static final Criterion H3_1 = createParentCriterion("3 a)", "LatinStringIndexExtractor", H3_1_1, H3_1_2);

    public static final Criterion H3_2_1 = createCriterion("Die Methode [[[putBucket]]] der Klasse RadixSort funktioniert vollständig korrekt", 1,
        () -> RadixSortTest.class.getMethod("testPutBucket", Integer.class, Integer.class));

    public static final Criterion H3_2_2 = createCriterion("Die Methode [[[sort]]] der Klasse RadixSort ruft die Methode putBucket in der korrekten Reihenfolge mit den korrekten Werten auf wenn maxInputLength 1 ist", 1,
        () -> RadixSortTest.class.getMethod("testPutBucketCallMaxLength1", List.class));

    public static final Criterion H3_2_3 = createCriterion("Die Methode [[[sort]]] der Klasse RadixSort ruft die Methode putBucket in der korrekten Reihenfolge mit den korrekten Werten auf wenn maxInputLength größer als 1 ist", 1,
        () -> RadixSortTest.class.getMethod("testPutBucketCall", List.class, Integer.class));

    public static final Criterion H3_2_4 = createCriterion("Die Methode [[[sort]]] der Klasse RadixSort schreibt die Werte aus den buckets an die korrekten Stellen in die zu sortierende Liste wenn es nur einen Bucket mit einem Eintrag gibt und maxInputLength 1 ist", 1,
        () -> RadixSortTest.class.getMethod("testOneBucketOneEntry", List.class, List.class, int.class));

    public static final Criterion H3_2_5 = createCriterion("Die Methode [[[sort]]] der Klasse RadixSort schreibt die Werte aus den buckets an die korrekten Stellen in die zu sortierende Liste wenn es nur einen Bucket mit mehreren Einträgen gibt und maxInputLength 1 ist", 1,
        () -> RadixSortTest.class.getMethod("testOneBucketMultipleEntries", List.class, List.class, int.class));

    public static final Criterion H3_2_6 = createCriterion("Die Methode sort der Klasse RadixSort schreibt die Werte aus den buckets an die korrekten Stellen in die zu sortierende Liste wenn es mehrere Buckets mit jeweils nur einem Eintrag gibt und maxInputLength 1 ist.", 1,
        () -> RadixSortTest.class.getMethod("testMultipleBucketsOneEntry", List.class, List.class, int.class));

    public static final Criterion H3_2_7 = createCriterion("Die Methode [[[sort]]] der Klasse RadixSort funktioniert vollständig korrekt", 1,
        () -> RadixSortTest.class.getMethod("testPutBucketCallMaxLength1", List.class),
        () -> RadixSortTest.class.getMethod("testPutBucketCall", List.class, Integer.class),
        () -> RadixSortTest.class.getMethod("testOneBucketOneEntry", List.class, List.class, int.class),
        () -> RadixSortTest.class.getMethod("testOneBucketMultipleEntries", List.class, List.class, int.class),
        () -> RadixSortTest.class.getMethod("testMultipleBucketsOneEntry", List.class, List.class, int.class),
        () -> RadixSortTest.class.getMethod("testSorting", List.class, List.class, Integer.class, Integer.class));

    public static final Criterion H3_2 = createParentCriterion("3 b)", "RadixSort", H3_2_1, H3_2_2, H3_2_3, H3_2_4, H3_2_5, H3_2_6, H3_2_7);

    public static final Criterion H3 = createParentCriterion("3", "Radix-Sort", H3_1, H3_2);

    public static final Rubric RUBRIC = Rubric.builder()
        .title("P1")
        .addChildCriteria(H1, H2, H3)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new MethodInterceptorTransformer());
    }
}
