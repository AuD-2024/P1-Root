package p1;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Grader;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import p1.card.CardColor;
import p1.transformers.MethodInterceptorTransformer;
import p1.transformers.SuperPartitionTransformer;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings("unused")
public class P1_RubricProvider_Private implements RubricProvider {
    //TODO add changes to the criterions from public tests
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

    public static final Criterion H1_3_1 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe bereits sortiert ist", 1,
        () -> BubbleSortTests.class.getMethod("testAlreadySorted", List.class));

    public static final Criterion H1_3_2 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nur ein Element enthält", 1,
        () -> BubbleSortTests.class.getMethod("testOneItem", Integer.class));

    public static final Criterion H1_3_3 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe zwei Elemente enthält", 1,
        () -> BubbleSortTests.class.getMethod("testTwoItems", List.class));

    public static final Criterion H1_3_4 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe mehr als zwei Elemente enthält", 1,
        () -> BubbleSortTests.class.getMethod("testMultipleItems", List.class));

    public static final Criterion H1_3_5 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort verändert nur Werte im Indexbereich [left, right]", 1,
        () -> BubbleSortTests.class.getMethod("testBounds", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H1_3_6 = createCriterion("Die Methode [[[bubblesort]]] der Klasse HybridSort verwendet die korrekten Lese- und Schreiboperationen in der korrekten Reihenfolge", 1,
        () -> BubbleSortTests.class.getMethod("testOperationOrder", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H1_3 = createParentCriterion("1 c)", "bubblesort", H1_3_1, H1_3_2, H1_3_3, H1_3_4, H1_3_5, H1_3_6);

    public static final Criterion H1_4_1 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort ruft, wenn notwendig, die Methode bubblesort mit den korrekten Werten auf", 1,
        () -> MergeSortTests.class.getMethod("testBubbleSortCall", List.class, Integer.class, Integer.class, Integer.class, Boolean.class));

    public static final Criterion H1_4_2 = createCriterion("Die Methode [[[mergeSort]]] der Klasse HybridSort ruft, wenn notwendig, die Methode [[[partition]]] und sich selber mit den korrekten Werten auf", 1,
        () -> MergeSortTests.class.getMethod("testMergeSortRecursion", List.class, Integer.class, Integer.class, Integer.class, Boolean.class, Integer.class));

    public static final Criterion H1_4_3 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe bereits partitioniert ist", 1,
        () -> MergeSortTests.class.getMethod("testAlreadyPartitioned", List.class, Integer.class, Integer.class));

    public static final Criterion H1_4_4 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nicht partitioniert ist und zwei Elemente enthält", 1,
        () -> MergeSortTests.class.getMethod("testPartitionTwoItems", List.class, Integer.class, Integer.class, List.class));

    public static final Criterion H1_4_5 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert korrekt wenn die Eingabe nicht partitioniert ist und drei Elemente enthält", 1,
        () -> MergeSortTests.class.getMethod("testPartitionThreeItems", List.class, Integer.class, Integer.class, Integer.class, List.class));

    public static final Criterion H1_4_6 = createCriterion("Die Methode [[[merge]]] der Klasse HybridSort funktioniert vollständig korrekt", 1,
        () -> MergeSortTests.class.getMethod("testAlreadyPartitioned", List.class, Integer.class, Integer.class),
        () -> MergeSortTests.class.getMethod("testPartitionTwoItems", List.class, Integer.class, Integer.class, List.class),
        () -> MergeSortTests.class.getMethod("testPartitionThreeItems", List.class, Integer.class, Integer.class, Integer.class, List.class),
        () -> MergeSortTests.class.getMethod("testPartitionMultipleItems", List.class, Integer.class, Integer.class, Integer.class, List.class));

    public static final Criterion H1_4 = createParentCriterion("1 d)", "Quicksort", H1_4_1, H1_4_2, H1_4_3, H1_4_4, H1_4_5, H1_4_6);

    public static final Criterion H1_5_1 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer ruft die [[[sort]]] Methode mit korrekten Werten in der richtigen Reihenfolge auf", 1,
        () -> HybridOptimizerTests.class.getMethod("testSortCall", List.class, List.class, List.class, int.class));

    public static final Criterion H1_5_2 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte nicht monoton sind und es nur ein Minimum gibt", 1,
        () -> HybridOptimizerTests.class.getMethod("testNonMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H1_5_3 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte streng monoton fallend sind", 1,
        () -> HybridOptimizerTests.class.getMethod("testStrictlyMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H1_5_4 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte monoton fallend sind", 1,
        () -> HybridOptimizerTests.class.getMethod("testMonotone", List.class, List.class, List.class, int.class));

    public static final Criterion H1_5_5 = createCriterion("Die Methode [[[optimize]]] der Klasse HybridOptimizer funktioniert korrekt wenn die Messwerte nicht monoton sind und es mehrere Minima gibt", 1,
        () -> HybridOptimizerTests.class.getMethod("testGlobalNotLocal", List.class, List.class, List.class, int.class));

    public static final Criterion H1_5 = createParentCriterion("1 e)", "HybridOptimizer", H1_5_1, H1_5_2, H1_5_3, H1_5_4, H1_5_5);

    public static final Criterion H1 = createParentCriterion("1", "Sortieralgorithmen", H1_1, H1_2, H1_3, H1_4, H1_5);

    public static final Rubric RUBRIC = Rubric.builder()
        .title("P1")
        .addChildCriteria(H1)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new SuperPartitionTransformer());
        configuration.addTransformer(new MethodInterceptorTransformer());
    }
}
