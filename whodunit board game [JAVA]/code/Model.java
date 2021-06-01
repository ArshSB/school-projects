// CLASS: Model
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class handles the game mechanics. Has runGame() method which starts the game
// and asks for human as well as computer players for their guesses or answers in a turn around
// the table (simulated via indices given to players). Also has setUp() method which sets up the game
// with cards and players
//-----------------------------------------

import java.util.ArrayList;
import java.util.Collections;

public class Model {

    int numPlayers;
    // holds all players in games, including human player
    ArrayList<IPlayer> players;
    // holds the perpetrator information
    ArrayList<Card> answer; // index 0: suspect, index 1: location, index 2: weapon
    // holds all cards in game via type
    ArrayList<Card> suspects, locations, weapons;

    public Model(int numPlayers, ArrayList<IPlayer> players, ArrayList<Card> suspects, ArrayList<Card> locations, ArrayList<Card> weapons) {
        this.numPlayers = numPlayers;
        this.suspects = new ArrayList<>(suspects);
        this.locations = new ArrayList<>(locations);
        this.weapons = new ArrayList<>(weapons);
        this.players = new ArrayList<>(players);
        this.answer = new ArrayList<>(3);
    }

    private void printCards(ArrayList<Card> cards) {

        for (Card card: cards)
            System.out.print(card.getValue() + "  ");

        System.out.println("");
    }

    // this method selects the perpetrator of the game and stores it in answer list
    private void chooseAnswer() {

        int random = (int)(Math.random() * suspects.size());
        answer.add(suspects.get(random));

        random = (int)(Math.random() * suspects.size());
        answer.add(locations.get(random));

        random = (int)(Math.random() * suspects.size());
        answer.add(weapons.get(random));

    }

    // this method sets up all the players in the game and gives them their index value,
    // with human player starting from index value 0
    private void setPlayers() {

        for (int counter = 0; counter < numPlayers; counter++)
            players.get(counter).setUp(numPlayers, counter, suspects, locations, weapons);

    }

    // this method distributes all cards in games (except the answer cards) to players around the table
    private void setCards() {

        System.out.println("Dealing cards...");
        // holds copy of all cards in game
        ArrayList<Card> allCards = new ArrayList<>(suspects.size() + locations.size() + weapons.size());
        allCards.addAll(suspects);
        allCards.addAll(locations);
        allCards.addAll(weapons);

        // remove answer cards so they are not distributed to players
        allCards.remove(answer.get(0));
        allCards.remove(answer.get(1));
        allCards.remove(answer.get(2));

        Collections.shuffle(allCards);

        int playerCounter = 0;

        // distribute all cards to the players around the table
        for (int counter = 0; counter < allCards.size(); counter++) {

            players.get(playerCounter).setCard(allCards.get(counter));

            // if one revolution around the table is complete and cards remain, start another revolution
            if (playerCounter == numPlayers - 1)
                playerCounter = 0;

            else
                playerCounter++;
        }
    }

    // this method checks if a guess that is an accusation is correct
    // returns true if the guess is correct, false otherwise
    private boolean checkAccusation(Guess g) {

        boolean result = false;

        if (g.getSuspect().getValue().equals(answer.get(0).getValue()) &&
                g.getLocation().getValue().equals(answer.get(1).getValue()) &&
                    g.getWeapon().getValue().equals(answer.get(2).getValue())) {

            result = true;

        }

        return result;

    }

    // this method sets up the game, by setting the players, distributing the cards, selecting answer
    // and printing all cards in game to players
    public void setUp() {

        System.out.println("Setting up players...\n");

        setPlayers();

        System.out.println("Here are all the suspects:");
        printCards(suspects);

        System.out.println("Here are all the locations:");
        printCards(locations);

        System.out.println("Here are all the weapons:");
        printCards(weapons);

        chooseAnswer();
        setCards();

    }

    // this method checks if other players can answer a current player's guess
    // returns true if another players refutes the guess, false if no one can refute
    // takes in players as parameter, which is the current players in game, a guess, and
    // current player and their index
    private boolean checkGuess(ArrayList<IPlayer> players, Guess guess, IPlayer current, int index) {

        boolean found = false;

        // ask players around the table until all players have been asked or someone answers
        for (int counter = index + 1; counter <= (players.size() + index) && !found; counter++) {

            IPlayer player = players.get(counter % numPlayers);

            // skip the player who guessed as they can't answer their own guess
            if (player.getIndex() == index)
                continue;

            System.out.printf("Asking player %d...\n", player.getIndex());
            Card card = player.canAnswer(guess, current);

            // if canAnswer return non-null card, meaning somebody refuted, set the flag accordingly and inform current player
            if (card != null) {
                current.receiveInfo(players.get(counter % numPlayers), card);
                found = true;
            }
        }

        return found;
    }

    // this method starts and runs the game, by asking players for their guesses when its their turn and
    // asking other players if they can answer the curren player's guess. Game ends when a player
    // makes correct guess or only 1 player left
    // Based on the pseudocode provided in assignment PDF
    public void runGame() {

        boolean running = true; // holds whether the game is running or not
        // holds players which remain in the game (not kicked out when wrong accusation is made)
        ArrayList<IPlayer> remainingPlayers = new ArrayList<>(players);

        System.out.println("Playing...");

        while (running) {

            // iterate around the table (of remaining players) and ask for their guess as well as ask others
            // if they can answer
            for (int player = 0; player < remainingPlayers.size() && running; player++) {

                IPlayer current = remainingPlayers.get(player);
                System.out.printf("Current turn: player %d\n", current.getIndex());
                Guess guess = current.getGuess();

                if (guess != null) {

                    System.out.printf("Player %d: %s\n", current.getIndex(), guess.getSummary());

                    // if player makes accusation, check if it is right
                    if (guess.getAccuse()) {

                        // if accusation is correct, then player has won the game
                        if (checkAccusation(guess)) {
                            running = false;
                            System.out.printf("\nPlayer %d wins game. Answer was: %s\n", current.getIndex(), guess.getSummary());
                        }

                        // otherwise if wrong accusation, remove player from game
                        else {

                            remainingPlayers.remove(current);
                            player--;
                            System.out.printf("Player %d is removed from game due to wrong accusation\n", current.getIndex());

                            // if only 1 player remain after removing current player, that player has won the game
                            if (remainingPlayers.size() == 1) {
                                running = false;
                                System.out.printf("\nLast player left, player %d wins game\n", players.get(0).getIndex());
                            }
                        }
                    }

                    // if player makes suggestion instead, simply ask if others players can answer
                    else {

                        boolean found = checkGuess(players, guess, current, current.getIndex());

                        // if no one could answer the player's guess, inform that player accordingly
                        if (!found)
                            current.receiveInfo(null, null);

                    }
                }
            }
        }
    }
}
