// CLASS: HumanPlayer
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class handles the human player in game. It handles how
// the human player interacts with other players and asks the human for input
// whenever necessary, such as selecting guess or refuting other players
// Implements IPlayer interface and its methods
//-----------------------------------------

import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer implements IPlayer {

    private int numPlayers, index;
    // player piles hold the cards which the human has by type, while the normal piles are all cards in game
    private ArrayList<Card> playerPeople, playerPlaces, playerWeapons, people, places, weapons;
    Scanner input;

    public HumanPlayer() {
    }

    public void setUp(int numPlayers, int index, ArrayList<Card> ppl, ArrayList<Card> places, ArrayList<Card> weapons) {
        this.numPlayers = numPlayers;
        this.index = index;
        this.people = new ArrayList<>(ppl);
        this.places = new ArrayList<>(places);
        this.weapons = new ArrayList<>(weapons);
        this.playerPeople = new ArrayList<>(ppl.size());
        this.playerPlaces = new ArrayList<>(places.size());
        this.playerWeapons = new ArrayList<>(weapons.size());
        input = new Scanner(System.in);
    }

    // this method adds a card to the player's hand
    public void setCard(Card c) {

        if (c != null) {

            System.out.printf("You received the card '%s'\n", c.getValue());

            if (c.getType() == 0)
                playerPeople.add(c);

            else if(c.getType() == 1)
                playerPlaces.add(c);

            else
                playerWeapons.add(c);

        }
    }

    public int getIndex() {
        return index;
    }

    // this method acts as helper method to canAnswer. Adds cards to contains pile which includes
    // the cards which the player's hand and all cards in game have in common
    private void canAnswerHelper(ArrayList<Card> cards, ArrayList<Card> contains, String guess) {

        for (int count = 0; count < cards.size(); count++) {

            if (guess.equals(cards.get(count).getValue()))
                contains.add(cards.get(count));

        }
    }

    // this method handles what happens when human player is asked if they can answer
    // another player's guess. Returns Card as null if no answer, otherwise returns one of the card
    // which the player has in their hand and refute with
    public Card canAnswer(Guess g, IPlayer ip) {

        Card card = null;

        // holds cards which player's hand and all cards in game have in common
        ArrayList<Card> contains = new ArrayList<>(playerPeople.size() + playerPlaces.size() + playerWeapons.size());

        canAnswerHelper(playerPeople, contains, g.getSuspect().getValue());
        canAnswerHelper(playerPlaces, contains, g.getLocation().getValue());
        canAnswerHelper(playerWeapons, contains, g.getWeapon().getValue());

        int count = contains.size();

        // if no cards in common, human player can't answer
        if (count == 0)
            System.out.printf("Player %d asked you about '%s', but you couldn't answer\n", ip.getIndex(), g.getSummary());

        // if only 1 card in common, simlpy return that card as answer
        else if (count == 1) {

            card = contains.get(0);
            System.out.printf("Player %d asked you about '%s', you only have one card '%s', you show it to them\n", ip.getIndex(), g.getSummary(), card.getValue());

        }

        // if more than 1 card in common, ask human player which card to return as refute
        else if (count > 1) {

            System.out.printf("Player %d asked you about '%s'. Which do you show?\n", ip.getIndex(), g.getSummary());

            // print all cards which player has in common
            for (int counter = 0; counter < count; counter++)
                System.out.printf("%d : %s\n", counter, contains.get(counter).getValue());

            int index; // holds index value of card to refute

            while (true) {

                try {

                    index = Integer.parseInt(input.nextLine());

                    if (index >= 0 && index < count) // if the given value is correct and within range, the user has input correctly so break out of while loop
                        break;

                    else // otherwise keep asking the user for valid input
                        System.out.println("Invalid, please try again");

                }
                // if the user inputs a string, keep asking for valid input
                catch (NumberFormatException e) {
                    System.out.println("Try again, please enter a number");
                }
            }

            card = contains.get(index);
        }

        return card;
    }

    // this method acts as helper method to getGuess
    // It asks for user input to select which card to build guess with via type, which is passed as arraylist in param
    // Returns card from a pile which human player has selected for their guess
    private Card getGuessHelper(ArrayList<Card> cards) {

        Card card = null;

        // print cards which the player has
        for (int counter = 0; counter < cards.size(); counter++)
            System.out.printf("%d : %s\n", counter, cards.get(counter).getValue());

        int index; // holds index value of card to select for guess

        while (true) {

            try {

                index = Integer.parseInt(input.nextLine());

                if (index >= 0 && index < cards.size()) // if the given value is correct and within range, the user has input correctly so break out of while loop
                    break;

                else // otherwise keep asking the user for valid input
                    System.out.println("Invalid, please try again");

            }
            // if the user inputs a string, keep asking for valid input
            catch (NumberFormatException e) {
                System.out.println("Try again, please enter a number");
            }
        }

        card = cards.get(index);

        return card;
    }

    // this method asks the human player for a guess via user input from the player's hand
    // return a valid non-null guess
    public Guess getGuess() {

        Guess guess = null;

        System.out.printf("It is your turn\nWhich person do you want to suggest?\n");
        Card suspect = getGuessHelper(people);

        System.out.println("Which location do you want to suggest?");
        Card location = getGuessHelper(places);

        System.out.println("Which weapon do you want to suggest?");
        Card weapon = getGuessHelper(weapons);

        System.out.println("Is this an accusation? (Y/N)");
        boolean accuse = false;
        String answer = input.nextLine().toLowerCase();

        while (!answer.equals("y") && !answer.equals("n")) {
            System.out.println("Invalid input, please enter (Y/N):");
            answer = input.nextLine().toLowerCase();
        }

        if (answer.equals("y"))
            accuse = true;

        else if (answer.equals("n"))
            accuse = false;

        guess = new Guess(suspect, location, weapon, accuse);

        return guess;

    }

    // this method lets the human player know that their guess was either refuted or no one could answer
    // after the human player has made a guess
    public void receiveInfo(IPlayer ip, Card c) {

        if (ip == null || c == null)
            System.out.println("No one could refute your suggestion");

        else
            System.out.printf("Player %d refuted your suggestion by showing you card '%s'\n", ip.getIndex(), c.getValue());

    }
}
