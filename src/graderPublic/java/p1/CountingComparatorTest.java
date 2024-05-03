package p1;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.card.Card;
import p1.card.CardColor;
import p1.comparator.CardComparator;
import p1.comparator.CountingComparator;
import p1.transformers.MethodInterceptor;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class CountingComparatorTest {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void checkIllegalMethods()  {
        MethodInterceptor.reset();

        CountingComparator<Integer> countingComparator = new CountingComparator<>(Integer::compareTo);
        countingComparator.compare(2, 3);
        countingComparator.getComparisonsCount();
        countingComparator.reset();

        IllegalMethodsCheck.checkMethods();
    }

    @Test
    public void testSortingOrder() {
        Context context = contextBuilder()
            .subject("CountingComparator.compare")
            .build();

        CountingComparator comparator = new CountingComparator(new CardComparator());

        assertEquals(0, comparator.compare(new Card(CardColor.CLUBS, 2), new Card(CardColor.CLUBS, 2)),
            context, result -> "Cards do not return the correct result when equal");

        assertEquals(1, comparator.compare(new Card(CardColor.CLUBS, 3), new Card(CardColor.CLUBS, 2)),
            context, result -> "Cards do not return the correct result when less");

        assertEquals(-1, comparator.compare(new Card(CardColor.CLUBS, 2), new Card(CardColor.CLUBS, 3)),
            context, result -> "Cards do not return the correct result when greater");
    }

}
