package p1;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.resource.New;
import org.mockito.ArgumentCaptor;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import p1.card.Card;
import p1.card.CardColor;
import p1.comparator.CardComparator;
import p1.transformers.MethodInterceptor;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class CardComparatorTests {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void checkIllegalMethods()  {
        MethodInterceptor.reset();

        new CardComparator().compare(new Card(CardColor.CLUBS, 2), new Card(CardColor.HEARTS, 3));
        new CardComparator().compare(new Card(CardColor.CLUBS, 2), new Card(CardColor.CLUBS, 3));

        IllegalMethodsCheck.checkMethods("^java/lang/Integer.+");
    }

    @Test
    public void testSortingOrder() {
        Context context = contextBuilder()
            .subject("CardComparator.compare")
            .build();

        assertEquals(0, new CardComparator().compare(new Card(CardColor.CLUBS, 2), new Card(CardColor.CLUBS, 2)),
            context, result -> "Cards do not return the correct result when equal");

        assertEquals(1, new CardComparator().compare(new Card(CardColor.CLUBS, 3), new Card(CardColor.CLUBS, 2)),
            context, result -> "Cards do not return the correct result when less");

        assertEquals(-1, new CardComparator().compare(new Card(CardColor.CLUBS, 2), new Card(CardColor.CLUBS, 3)),
            context, result -> "Cards do not return the correct result when greater");
    }
}
