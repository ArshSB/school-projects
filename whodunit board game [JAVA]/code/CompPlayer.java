// CLASS: CompPlayer
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class handles the computer player in game. It handles how
// the computer player interacts with other players and makes educated guesses
// based on previous guesses. Computer player never makes false accusation
// Implements IPlayer interface and its methods
//-----------------------------------------

import java.util.ArrayList;
import java.util.Collections;

public class CompPlayer implements IPlayer {

    private int numPlayers, index; // holds number of players in game and index of computer
    private ArrayList<Card> cards, people, places, weapons; // cards is all cards given to player, while
    // people, places, weapons are all cards in the game, but not including what the player has

    public CompPlayer() {
    }

    public void setUp(int numPlayers, int index, ArrayList<Card> ppl, ArrayList<Card> places, ArrayList<Card> weapons) {
        this.numPlayers = numPlayers;
        this.index = index;
        this.people = new ArrayList<>(ppl);
        this.places = new ArrayList<>(places);
        this.weapons = new ArrayList<>(weapons);
        this.cards = new ArrayList<>(ppl.size() + places.size() + weapons.size());
    }

    // this method adds a card to the player's hand while removing that card from the people, places, location
    // pile which are used to make guesses outside of the player's hand
    public void setCard(Card c) {

        if (c != null) { // only add if card exists

            cards.add(c);

            // remove that card from its respective pile

            if (c.getType() == 0)
                people.remove(c);

            else if(c.getType() == 1)
                places.remove(c);

            else
                weapons.remove(c);

        }
    }

    public int getIndex() {
        return index;
    }

    // this method handles what happens when computer player is asked if they can answer
    // another player's guess. Returns Card as null if no answer, otherwise returns one of the card
    // which the player has in their hand and refute with
    public Card canAnswer(Guess g, IPlayer ip) {

        Card card = null;
        Collections.shuffle(cards);

        // loop through all the cards in player hand and check if any of them is equal to
        // the cards in the guess, in which case return that card
        for (int counter = 0; counter < cards.size(); counter++) {

            String cardName = cards.get(counter).getValue();

            boolean checkSuspect = g.getSuspect().getValue().equals(cardName);
            boolean checkLocation = g.getLocation().getValue().equals(cardName);
            boolean checkWeapon = g.getWeapon().getValue().equals(cardName);

            if (checkSuspect || checkLocation || checkWeapon)
                card = cards.get(counter);
        }

        return card;
    }

    // this method gets the player's guess, which will be always outside of the player's hand
    // Also, only returns an accusation if computer player is sure it is correct, otherwise returns
    // suggestion
    public Guess getGuess() {

        Guess guess = null;

        // shuffle all cards and get the first card to build guess
        Collections.shuffle(people);
        Collections.shuffle((places));
        Collections.shuffle(weapons);

        int total = people.size() + places.size() + weapons.size();

        // suggestion guess
        if (total > 3)
            guess = new Guess(people.get(0), places.get(0), weapons.get(0), false);

        // accusation guess when the answer pile has only 3 cards
        else if (total == 3)
            guess = new Guess(people.get(0), places.get(0), weapons.get(0), true);

        return guess;

    }

    // this method handles what happens when a player makes a guess and receives answers from other players
    // if parameters are null this means that no one could answer, otherwise another player refuted their guess
    public void receiveInfo(IPlayer ip, Card c) {

        if (ip == null || c == null)
            System.out.printf("No one could refute player %d guess\n", index);

        else {

            System.out.println("Player " + ip.getIndex() + " answered");

            // remove the card which was used to refute guess from the answer pile so that player
            // can make a improved guess next time

            if (c.getType() == 0)
                people.remove(c);

            else if (c.getType() == 1)
                places.remove(c);

            else
                weapons.remove(c);

        }
    }
}
