// CLASS: Card
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class holds the information about a card of the game:
// an integer that represents the type of the card (suspect, location or weapon)
// and a string that is the name of the card
//-----------------------------------------

public class Card {

    private int type;   // 0 for suspect, 1 for location, 2 for weapon
    private String value;

    public Card(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() { return type; }
    public String getValue() { return value; }
}
