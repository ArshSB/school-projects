import java.util.ArrayList;

public interface IPlayer {

    public void setUp( int numPlayers, int index, ArrayList<Card> ppl, ArrayList<Card> places, ArrayList<Card> weapons);

    public void setCard (Card c);

    public int getIndex();

    public Card canAnswer(Guess g, IPlayer ip);

    public Guess getGuess();

    public void receiveInfo(IPlayer ip, Card c);

}