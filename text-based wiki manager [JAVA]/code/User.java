// CLASS: User
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class manages a single user and the information associated with it
// such as the user's username and their history at every valid command.
//-----------------------------------------

public class User {

    private String username;
    // linked list that holds history of user, each node represents 1 successful command performed by the user
    private LinkedList history;

    public User (String username) {

        this.username = username;
        history = new LinkedList();

    }

    public User() {

        username = "";
        history = new LinkedList();

    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public LinkedList getHistory() {
        return history;
    }

    public void setHistory(LinkedList history) {
        this.history = history;
    }

}
