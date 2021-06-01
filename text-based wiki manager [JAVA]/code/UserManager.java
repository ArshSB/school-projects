// CLASS: UserManager
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class manages all instances of User created during the program
// Essentially, it acts as a library where the books are instances of users
// In general, this class creates, adds history, and prints history of instances of User
// based on if the user performing the task is a valid user
// Additionally, this class extends WikiTime which holds the time information of the program,
// which is used to increment command time when methods are called
//-----------------------------------------

public class UserManager extends WikiTime {

    private LinkedList users; // stores instances of User, where each Node is of UserEntity datatype

    public UserManager() {
        users = new LinkedList();
    }

    public LinkedList getUsers() { return users; }

    public void setUsers(LinkedList users) { this.users = users; }

    // this method simply checks if a instance of User with given name exists inside users (AKA user library)
    // returns -1 if user with given name is not found, otherwise returns index of node which holds the User instance
    public int checkUsername(String username) {
        return users.getIndexOfNode(new StringEntity(username));
    }

    //------------------------------------------------------
    // PURPOSE: this method creates a new User instance and adds it to the user library
    // It checks if given username is a valid name and if the user is not a duplicate.
    // it also returns the proper confirmation to the user about the outcome of operation
    // Parameters: String username - name of user to be created
    // Returns: (String) informs user if User was created or reason as to why not
    //------------------------------------------------------
    public String createUser(String username) {

        // default to inform user the outcome
        String result = String.format("CREATE USER: DUPLICATE! user '%s' already exists", username);
        int checkUsername = checkUsername(username);

        // only create User if user to be created is not a duplicate and the user is valid
        // also checks if username is valid per the assignment rules (less than 80 characters and no whitespace)
        if (checkUsername == -1 && username.length() <= 80 && !username.contains(" ")) {

            // create new instance of user
            User user = new User(username);
            // add newly created user to library
            users.insertTail(new UserEntity(user));
            // inform user the operation was successful
            result = String.format("CREATE USER: CONFIRMED! user '%s' created", username);

        }

        // increment time by 1, regardless whether the user was created or not
        increment();
        return result;

    }

    //------------------------------------------------------
    // PURPOSE: this method updates history of the given user
    // It checks if given username is a valid name
    // Parameters: String username - name of user whose history is to be updated
    // String content - content of history to be appended
    // Returns: void
    //------------------------------------------------------
    public void addUserHistory(String username, String content) {

        // check if given username is a valid user
        int checkUsername = checkUsername(username);
        // get the user instance with given username
        UserEntity user = (UserEntity) users.getNodeAtIndex(checkUsername).getData();
        // add the given content to history linked list of user instance
        user.getData().getHistory().insertTail(new StringEntity(content));

    }

    //------------------------------------------------------
    // PURPOSE: this method prints the user history of the given user
    // It checks if given username is a valid user
    // Parameters: String username - name of user whose history is to be printed
    // Returns: (String) informs user is operation was successful or not
    //------------------------------------------------------
    public String printUserHistory(String username) {

        String result = String.format("PRINT USER HISTORY: NOT FOUND! user '%s' does not exist", username);
        int checkUsername = checkUsername(username);

        // only print history if given username is valid user
        if (checkUsername != -1) {
            // get the user instance with given username
            UserEntity user = (UserEntity) users.getNodeAtIndex(checkUsername).getData();
            System.out.println(String.format("Printing history of user '%s'...", username));
            // print the linked list that stores history of user
            user.getData().getHistory().print();

            result = String.format("----------------------------"); // formatting for better viewing
        }

        increment();
        return result;

    }
}
