package set;

import set.enums.CardColor;
import set.enums.Count;
import set.enums.Shading;
import set.enums.Shape;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = generateDeck();
    }

    //kartyapakli generalasa
    private List<Card> generateDeck() {
        List<Card> fullDeck = new ArrayList<>();
        for (Count count : Count.values()) {
            for (CardColor color : CardColor.values()) {
                for (Shape shape : Shape.values()) {
                    for (Shading shading : Shading.values()) {
                        fullDeck.add(new Card(count, shape, color, shading));
                    }
                }
            }
        }
        return fullDeck;
    }

    // osszekeverjuk a paklit
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // kiosztjuk a "felso" kartyat
    public Card dealCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    // hany kartya maradt a pakliban
    public int remainingCards() {
        return cards.size();
    }
}
