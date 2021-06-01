//-----------------------------------------
// NAME		: Arshpreet Buttar
// COURSE		: COMP 2150
//
// REMARKS: This program implements the whodunnit game, a simplified version
// of the Clue board game. User can play against several computer players with preset
// cards. User can make right accusation and win game, but if wrong accusation user will
// be removed from game. Other features are implemented as per said in assignment PDF
//-----------------------------------------

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("How many computer players would you like to play against?");

        int numPlayers; // holds number of computer players that the user wants to play against

        while (true) {

            try {

                numPlayers = Integer.parseInt(input.nextLine());

                if (numPlayers > 0) // if the given value is positive, the user has input correctly so break out of while loop
                    break;

                else // otherwise keep asking the user for valid input
                    System.out.println("Invalid integer, please provide a positive value");

            }
            // if the user inputs a string, keep asking for valid input
            catch (NumberFormatException e) {
                System.out.println("Try again, please enter a number");
            }
        }

        // holds all players in game, including human player
        ArrayList<IPlayer> players = new ArrayList<>(numPlayers);
        players.add(new HumanPlayer());

        for (int counter = 1; counter <= numPlayers; counter++)
            players.add(new CompPlayer());

        // holds all cards in game by type
        ArrayList<Card> suspects = new ArrayList<>(8);
        ArrayList<Card> locations = new ArrayList<>(8);
        ArrayList<Card> weapons = new ArrayList<>(8);

        String[] suspectNames = {"Ram", "Thor", "Zeus", "Krishna", "Zoroaster", "Artemis", "Jupiter", "Ravan"};
        String[] locationNames = {"Athens", "Rome", "Delhi", "Paris", "Beijing", "Kyoto", "Berlin", "Kolkata"};
        String[] weaponNames = {"Stick", "Trident", "Sword", "Jug", "Lightning", "Fireball", "Arrow", "Keyboard"};

        // add the pre-set cards into lists
        for (int counter = 0; counter < 8; counter++) {
            suspects.add(new Card(0, suspectNames[counter]));
            locations.add(new Card(1, locationNames[counter]));
            weapons.add(new Card(2, weaponNames[counter]));
        }

        // make Model instance and start game
        Model model = new Model(numPlayers + 1, players, suspects, locations, weapons);
        model.setUp();
        model.runGame();

    }
}
