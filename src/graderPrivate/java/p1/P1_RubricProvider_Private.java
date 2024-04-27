package p1;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Grader;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import p1.card.CardColor;
import p1.transformers.MethodInterceptorTransformer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unused")
public class P1_RubricProvider_Private implements RubricProvider {
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

    public static final Criterion H1_1_1 = createCriterion("Die Methode [[[compare]]] der Klasse CardComparator funktioniert korrekt wenn die Farben der verglichenen Karten unterschiedlich sind", 1,
        () -> CardComparatorTests.class.getMethod("testColor", List.class, List.class, List.class));

    public static final Criterion H1_1_2 = createCriterion("Die Methode [[[compare]]] der Klasse CardComparator funktioniert korrekt wenn die Farben der verglichenen Karten gleich sind", 1,
        () -> CardComparatorTests.class.getMethod("testValue", CardColor.class, List.class, List.class));

    public static final Criterion H1_1 = createParentCriterion("1 a)", "CardComparator", H1_1_1, H1_1_2);

    public static final Criterion H1_2_1 = createCriterion("Die Methode [[[compare]]] der Klasse CountingComparator funktioniert korrekt", 1,
        () -> CountingComparatorTest.class.getMethod("testCompare", Integer.class, Integer.class));

    public static final Criterion H1_2_2 = createCriterion("Die Methoden [[[reset]]] und [[[getComparisonsCount]]] der Klasse CountingComparator funktionieren korrekt", 1,
        () -> CountingComparatorTest.class.getMethod("testReset", Integer.class),
        () -> CountingComparatorTest.class.getMethod("testGetComparisonsCount", Integer.class));

    public static final Criterion H1_2 = createParentCriterion("1 b)", "CountingComparator", H1_2_1, H1_2_2);

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

    public static final Criterion H2_2_1 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort ruft, wenn notwendig, die Methode bubblesort mit den korrekten Werten auf", 1,
        () -> MergeSortTests.class.getMethod("testBubbleSort", List.class, Integer.class, Integer.class, Integer.class, Boolean.class));

    public static final Criterion H2_2_2 = createCriterion("Die Methode [[[mergeSort]]] der Klasse HybridSort ruft, wenn notwendig, die Methode [[[merge]]] und sich selber mit den korrekten Werten auf", 1,
        () -> MergeSortTests.class.getMethod("testMergeSortRecursion", List.class, Integer.class, Integer.class, Integer.class, Boolean.class, Integer.class));

    public static final Criterion H2_2_3 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe bereits gemerged ist", 1,
        () -> MergeSortTests.class.getMethod("testAlreadyMerged", List.class, Integer.class, Integer.class));

    public static final Criterion H2_2_4 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nicht gemerged ist und zwei Elemente enthält", 1,
        () -> MergeSortTests.class.getMethod("testMergeTwoItems", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_2_5 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nicht gemerged ist und drei Elemente enthält", 1,
        () -> MergeSortTests.class.getMethod("testMergeThreeItems", List.class, Integer.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_2_6 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert vollständig korrekt", 1,
        () -> MergeSortTests.class.getMethod("testAlreadyMerged", List.class, Integer.class, Integer.class),
        () -> MergeSortTests.class.getMethod("testMergeTwoItems", List.class, Integer.class, Integer.class, List.class),
        () -> MergeSortTests.class.getMethod("testMergeThreeItems", List.class, Integer.class, Integer.class, Integer.class, List.class),
        () -> MergeSortTests.class.getMethod("testMergeMultipleItems", List.class, Integer.class, Integer.class, Integer.class, List.class));

    public static final Criterion H2_2 = createParentCriterion("2 b)", "MergeSort", H2_2_1, H2_2_2, H2_2_3, H2_2_4, H2_2_5, H2_2_6);

    public static final Criterion H2_3_1 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer ruft die [[[sort]]] Methode mit korrekten Werten in der richtigen Reihenfolge auf", 1,
        () -> HybridOptimizerTests.class.getMethod("testSortCall", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3_2 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte nicht monoton sind und es nur ein Minimum gibt", 1,
        () -> HybridOptimizerTests.class.getMethod("testNonMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3_3 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte streng monoton fallend sind", 1,
        () -> HybridOptimizerTests.class.getMethod("testStrictlyMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3_4 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte monoton fallend sind", 1,
        () -> HybridOptimizerTests.class.getMethod("testMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3_5 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte nicht monoton sind und es mehrere Minima gibt", 1,
        () -> HybridOptimizerTests.class.getMethod("testGlobalNotLocal", List.class, List.class, List.class, int.class));

    public static final Criterion H2_3 = createParentCriterion("2 c)", "HybridOptimizer", H2_3_1, H2_3_2, H2_3_3, H2_3_4, H2_3_5);

    public static final Criterion H1 = createParentCriterion("1", "Comparators", H1_1, H1_2, H2_1, H2_2, H2_3);
    public static final Criterion H2 = createParentCriterion("2", "Sortieralgorithmen", H1_1, H1_2, H2_1, H2_2, H2_3);

    public static final Rubric RUBRIC = Rubric.builder()
        .title("P1")
        .addChildCriteria(H1)
        .addChildCriteria(H2)
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
