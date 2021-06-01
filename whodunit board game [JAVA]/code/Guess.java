// CLASS: Guess
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class holds the information about a guess a player can make:
// whether the guess is an accusation or suggestion, the suspect, location and weapon
// Also has a summary method that prints the guess to STDOUT
//-----------------------------------------

public class Guess {

    private Card suspect, location, weapon;
    private boolean accuse; // false if suggestion, true if accusation

    public Guess(Card suspect, Card location, Card weapon , boolean accuse) {
        this.suspect = suspect;
        this.location = location;
        this.weapon = weapon;
        this.accuse = accuse;
    }

    public Card getSuspect() { return suspect; }

    public Card getLocation() { return location; }

    public Card getWeapon() { return weapon; }

    public boolean getAccuse() { return accuse; }

    public String getSummary() {

        String isAccuse = (accuse)? "Accusation":"Suggestion";
        return String.format("%s: Person '%s' in location '%s' with weapon '%s'", isAccuse, suspect.getValue(), location.getValue(), weapon.getValue());

    }
}
