// CLASS: DocumentManager
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class manages all instances of Document created during the program
// Essentially, it acts as a library where the books are instances of Document
// In general, this class creates, appends, replaces, delete, prints and restore instances of Document
// based on if the Document already exists or if the user performing the task is a valid user
// Additionally, this class extends WikiTime which holds the time information of the program,
// which is useful for performing certain operations such as restoreDocument
//-----------------------------------------
public class DocumentManager extends WikiTime {

    private LinkedList documents;
    // stores instances of Document, where each Node is of DocumentEntity datatype
    private UserManager users;
    // stores information of valid, existing users. Used for verifying users on appropriate operations

    // constructor where an instance of UserManager is expected as input so DocumentManager has access
    // to information about all existing users
    public DocumentManager(UserManager users) {

        documents = new LinkedList();
        this.users = users;

    }

    public DocumentManager() {

        documents = new LinkedList();
        users = new UserManager();

    }

    public LinkedList getDocuments() { return documents; }

    public void setDocuments(LinkedList documents) { this.documents = documents; }

    public UserManager getUsers() { return users; }

    public void setUsers(UserManager users) { this.users = users; }

    // this method simply checks if a instance of Document with given title exists inside documents (AKA document library)
    // returns -1 if document with given title is not found, otherwise returns index of node which holds the Document instance
    private int checkTitle(String title) {
        return documents.getIndexOfNode(new StringEntity(title));
    }

    //------------------------------------------------------
    // PURPOSE: this method creates a new Document instance and adds it to the document library
    // It checks if given title of Document doesn't already exist in the library and
    // also verifies if given username is a valid user. Additionally, if a document is successfully created,
    // it adds the appropriate user history and returns the proper confirmation to the user
    // Parameters: String title - title of new Document to be created
    // String username - name of user wishing to create document
    // Returns: (String) tells user if document was created or reason as to why document was not created
    //------------------------------------------------------
    public String createDocument(String title, String username) {

        String result = ""; // used to inform user whether document was created or not
        int getTitleIndex = checkTitle(title); // check if Document with given title doesn't already exist in library
        int getUserIndex = users.checkUsername(username); // check if given username is a valid user

        // only create document if Document to be created is not a duplicate and the user is valid
        // also checks if title of Document is valid per the assignment rules (less than 80 characters and no whitespace)
        if (getTitleIndex == -1 && getUserIndex != -1 && title.length() <= 80 && !title.contains(" ")) {

            // create new instance of Document
            Document doc = new Document(title, getTime(), username);
            // add the newly created Document to the library
            documents.insertTail(new DocumentEntity(doc));
            // add the appropriate information to the user's history
            users.addUserHistory(username, String.format("CREATED document %s", title));
            // change result to inform user that document was created
            result = String.format("CREATE DOCUMENT: CONFIRMED! document '%s' created", title);

        }

        // inform user that document was was not created if the username is not a valid user
        else if (getTitleIndex == -1)
            result = String.format("CREATE DOCUMENT: NOT FOUND! user '%s' does not exist", username);

        // inform user that document was not created if the document is a duplicate
        else
            result = String.format("CREATE DOCUMENT: DUPLICATE! document '%s' already exists", title);

        // increment time by 1, regardless whether the document was created or not
        increment();
        return result;
    }

    //-----------------------------------------------------
    // PURPOSE: this method appends a new line of given content to the end of the document with given title,
    // if it exists. also verifies if given username is a valid user.
    // returns the appropriate confirmation to inform user if line was appended or not
    // Parameters: String title - title of the Document to be appended
    // String username - name of user wishing to append line
    // String content - line to be appended
    // Returns: (String) tells user if line was appended or reason as to why line was not appended
    //------------------------------------------------------
    public String appendLine(String title, String username, String content) {

        // set default information to inform the user
        String result = String.format("APPEND: NOT FOUND! document '%s' or user '%s' does not exist", title, username);
        int getTitleIndex = checkTitle(title);
        int getUserIndex = users.checkUsername(username);

        // only append line if document with given title already exists and the user is a valid user
        if (getTitleIndex != -1 && getUserIndex != -1) {

            // get the correct instance of Document with given title
            DocumentEntity doc = (DocumentEntity) documents.getNodeAtIndex(getTitleIndex).getData();
            // append new line to the Document with appropriate information
            doc.getData().append(content, username, getTime());
            // add necessary information to user's history
            users.addUserHistory(username, String.format("APPENDED '%s' in document %s", content, title));
            // change result to inform user line was successfully appended
            result = String.format("APPEND: SUCCESS! appended '%s' in document %s", content, title);

        }

        // increment time by 1 whether or not the line was appended
        increment();
        return result;

    }

    //-----------------------------------------------------
    // PURPOSE: this method replaces a line of Document with given title, if it exists.
    // It also verifies if the user is a valid user and if the line to be replaced is valid
    // returns the appropriate confirmation to inform user if line was replaced or not
    // Parameters: String title - title of the Document whose line is to be replaced
    // String username - name of user wishing to replace line
    // int line - line number to be replaced in Document
    // String content - new line to be added
    // Returns: (String) tells user if line was replaced or reason as to why line was not replaced
    //------------------------------------------------------
    public String replaceLine(String title, String username, int line, String content) {

        // set default information to inform the user
        String result = String.format("REPLACE: NOT FOUND! document '%s' or user '%s' does not exist", title, username);
        int getTitleIndex = checkTitle(title);
        int getUserIndex = users.checkUsername(username);

        // only replace line if document with given title exists and the user is a valid user
        if (getTitleIndex != -1 && getUserIndex != -1) {

            // get instance of Document with given title
            DocumentEntity doc = (DocumentEntity) documents.getNodeAtIndex(getTitleIndex).getData();
            // call replace method of Document instance with necessary information,
            // which will return the appropriate information to inform user if line was replaced or not
            result = doc.getData().replace(content, line, username, getTime(), users);

        }

        increment();
        return result;

    }

    //-----------------------------------------------------
    // PURPOSE: this method deleted a line of Document with given title, if it exists.
    // It also verifies if the user is a valid user and if the line to be deleted is valid
    // returns the appropriate confirmation to inform user if line was replaced or not
    // Parameters: String title - title of the Document whose line is to be deleted
    // String username - name of user wishing to delete line
    // int line - line number to be deleted in Document
    // Returns: (String) tells user if line was deleted or reason as to why line was not deleted
    //------------------------------------------------------
    public String deleteLine(String title, String username, int line) {

        String result = String.format("DELETE: NOT FOUND! document '%s' or user '%s' does not exist", title, username);
        int getTitleIndex = checkTitle(title);
        int getUserIndex = users.checkUsername(username);

        if (getTitleIndex != -1 && getUserIndex != -1) {

            // get instance of Document with given title
            DocumentEntity doc = (DocumentEntity) documents.getNodeAtIndex(getTitleIndex).getData();
            // call delete method of Document with necessary information, which will return the appropriate information
            // to inform user if line was replaced or not
            result = doc.getData().delete(line, username, getTime(), users);

        }

        increment();
        return result;

    }

    //-----------------------------------------------------
    // PURPOSE: this method restores content of a Document with given title at given time,
    // if it exists or existed at that time
    // It verifies if a document with given title actually exists and if user is valid user
    // Parameters: String username - name of user wishing to restore Document
    // String title - name of Document to restore
    // int time - command time at which the Document is to be restored at
    // Returns: (String) tells user if Document was restored or not
    //------------------------------------------------------
    public String restoreDocument(String username, String title, int time) {

        String result = String.format("RESTORE: NOT FOUND! document '%s' / user '%s' does not exist or invalid time given", title, username);
        int getTitleIndex = checkTitle(title);
        int getUserIndex = users.checkUsername(username);

        // only perform restore if Document with given title exists and user is a valid user
        if (getUserIndex != -1 && getTitleIndex != -1 && time < (getTime()) &&  time >= 0) {

            DocumentEntity doc = (DocumentEntity) documents.getNodeAtIndex(getTitleIndex).getData();
            // call restore method of Document with necessary information, which will return the appropriate information
            // to inform user if Document was properly restores or not
            result = doc.getData().restore(username, time, users);

        }

        increment();
        return result;

    }

    //-----------------------------------------------------
    // PURPOSE: this method prints the content of Document instance with given title, if it exists
    // It verifies if a document with given title actually exists in library and returns the appropriate confirmation
    // to inform user if document was printed or not
    // Parameters: String title - name of Document to be printed
    // Returns: (String) tells user if document was printed or reason as to why it was not printed
    //------------------------------------------------------
    public String printDocument(String title) {

        String result = String.format("PRINT DOCUMENT: NOT FOUND! document '%s' does not exist", title);
        int getTitleIndex = checkTitle(title);

        if (getTitleIndex != -1) {

            System.out.println(String.format("Printing document '%s'...", title));
            // get instance of Document with given title
            DocumentEntity doc = (DocumentEntity) documents.getNodeAtIndex(getTitleIndex).getData();
            // simply call the print method of the linked list data structure to print content of document
            doc.getData().getDocument().print();

            result = String.format("----------------------------"); // formatting for easier viewing

        }

        increment();
        return result;

    }

    //-----------------------------------------------------
    // PURPOSE: this method prints the complete history of Document with given title, if it exists
    // It verifies if a document with given title actually exists in library and returns the appropriate confirmation
    // to inform user if document history was printed or not
    // Parameters: String title - name of Document whose history is to be printed
    // Returns: (String) tells user if document history was printed or reason as to why it was not printed
    //------------------------------------------------------
    public String printHistory(String title) {

        String result = String.format("PRINT DOCUMENT HISTORY: NOT FOUND! document '%s' does not exist", title);
        int getTitleIndex = checkTitle(title);

        // only print history if document with given title exists in library
        if (getTitleIndex != -1) {

            System.out.println(String.format("Printing history of document '%s'...", title));
            DocumentEntity doc = (DocumentEntity) documents.getNodeAtIndex(getTitleIndex).getData();
            // simply call print method of linked list data structure to print the history content of the Document
            doc.getData().getHistory().print();

            result = String.format("----------------------------");

        }

        increment();
        return result;

    }

}
