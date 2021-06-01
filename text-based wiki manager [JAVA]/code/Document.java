// CLASS: Document
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class manages a single document and the information associated with it
// such as its title, content, document history and document content at every change
// In general, this class implements the create, replace, delete and restore methods
// specific to an individual document. It also keeps track of the history of the document
// as well the state of the document at every change
//-----------------------------------------

public class Document {

    // name of document
    private String title;
    // linked list that holds current content of the document, each node represents a new line
    private LinkedList document;
    // linked list that holds history of document, each node represents 1 successful change to document
    private LinkedList history;
    // linked list that holds document linked lists, each node represents document content at a certain time
    private LinkedList snapshot;
    // linked list that holds IntEntity value, each node represents the time at which a change was performed
    private LinkedList timeCommands;

    // create new instance of document with given title, user who created document and time at which the document is created
    public Document (String title, int time, String username) {

        this.title = title;
        document = new LinkedList();
        history = new LinkedList();
        snapshot = new LinkedList(new ListEntity(document.copy())); // append empty document to head to represent creation of document
        timeCommands = new LinkedList(new IntEntity(time)); // append creation time
        history.insertHead(new StringEntity(String.format("User %s created document", username))); // append first history change
    }

    // default constructor
    public Document () {

        title = "";
        document = new LinkedList();
        history = new LinkedList();
        snapshot = new LinkedList();
        timeCommands = new LinkedList();

    }

    public String getTitle() { return title; }

    public void setUsername(String title) { this.title = title; }

    public LinkedList getDocument() {
        return document;
    }

    public void setDocument(LinkedList document) {
        this.document = document;
    }

    public LinkedList getHistory() { return history; }

    public void setHistory(LinkedList history) { this.history = history; }

    public LinkedList getSnapshot() { return snapshot; }

    public void setSnapshot(LinkedList snapshot) { this.snapshot = snapshot; }

    public LinkedList getTimeCommands() { return timeCommands; }

    public void setTimeCommands(LinkedList timeCommands) { this.timeCommands = timeCommands; }

    //------------------------------------------------------
    // PURPOSE: this method appends a new line to the document content
    // Parameters: String content - content to be appended to the document
    // String username - user performing the operation so it can be added to history
    // int time - time at which the operation is performed
    // Returns: void, only inserts information into linked lists
    //------------------------------------------------------
    public void append (String content, String username, int time) {

        // insert line to tail (representing inserting at end of document)
        document.insertTail(new StringEntity(content));
        // add history after change to document content
        history.insertHead(new StringEntity(String.format("User %s APPENDED '%s'", username, content)));
        // add new state of document content to snapshot so it can restores at that point later on
        snapshot.insertTail(new ListEntity(document.copy()));
        // add time command at which the append was performed
        timeCommands.insertTail(new IntEntity(time));

    }

    //------------------------------------------------------
    // PURPOSE: this method replaces a line of the document with given content and
    // also updates the necessary history information
    // Parameters: String content - content to be replaced with old content
    // String username - user performing the operation so it can be added to history
    // int time - time at which the operation is performed
    // UserManager users - access to users library so user history can be appropriately updated
    // Returns: (String) inform user if replace was successful or failed
    //------------------------------------------------------
    public String replace (String content, int line, String username, int time, UserManager users) {

        // default information to tell user about the outcome of operation
        String result = String.format("REPLACE: FAIL to replace! line %d does not exist in document %s", line, title);

        // only perform replace if the given line actually exists inside document content
        if (line >= 0 && line < document.getLength()) {

            /// holds the previous data at given line so it be included in history update
            StringEntity prevData = (StringEntity) getDocument().getNodeAtIndex(line).getData();
            users.addUserHistory(username, String.format("Replaced line %d from content '%s' to '%s' in document %s", line, prevData.getData(), content, title));
            history.insertHead(new StringEntity(String.format("User %s REPLACED line %d from '%s' to '%s'", username, line, prevData.getData(), content)));

            // call replaceAtIndex method of linked list data structure to change the content
            document.replaceAtIndex(new StringEntity(content), line);
            // insert new state of document to snapshot so it can be restored at that point later on
            snapshot.insertTail(new ListEntity(document.copy()));
            // add time command at which the operation was performed
            timeCommands.insertTail(new IntEntity(time));

            // informed user replace was successful
            result = String.format("REPLACE: SUCCESS! line %d replaced in document %s", line, title);

        }

        return result;

    }

    //------------------------------------------------------
    // PURPOSE: this method deletes a line of the document at given position
    // also updates the necessary history information
    // Parameters: int line - line number at which the deletion is to be performed
    // String username - user performing the operation so it can be added to history
    // int time - time at which the operation is performed
    // UserManager users - access to users library so user history can be appropriately updated
    // Returns: (String) inform user if deletion was successful or failed
    //------------------------------------------------------
    public String delete (int line, String username, int time, UserManager users) {

        String result = String.format("DELETE: FAIL to delete! line %d does not exist in document %s", line, title);

        // only perform deletion if the given line is valid
        if (line >= 0 && line < document.getLength()) {

            StringEntity prevData = (StringEntity) getDocument().getNodeAtIndex(line).getData();
            users.addUserHistory(username, String.format("DELETED line %d of content '%s' in document %s", line, prevData.getData() , title));

            history.insertHead(new StringEntity(String.format("User %s DELETED line %d of content '%s'", username, line, prevData.getData())));
            // call removeAtIndex of linked list data structure to remove the node at given index (line)
            document.removeAtIndex(line);
            snapshot.insertTail(new ListEntity(document.copy()));

            timeCommands.insertTail(new IntEntity(time));
            result = String.format("DELETE: SUCCESS! line %d deleted in document %s", line, title);

        }

        return result;

    }

    //------------------------------------------------------
    // PURPOSE: this method is helper method for restoring document at given index
    // Parameters: int index - index of snapshot at which document is restored
    // String username - user performing the operation so it can be added to history
    // int time - time at which restore is performed
    // UserManager users - access to users library so user history can be appropriately updated
    // Returns: void, only updates information
    //------------------------------------------------------
    private void restoreHelper(int index, String username, int time, UserManager users) {

        // perform restore only if valid index is given
        if (index >= 0) {

            // get the snapshot data at given index
            ListEntity lastSnapshot = (ListEntity) snapshot.getNodeAtIndex(index).getData();
            // set document to the document in lastSnapshot, completing restore
            document = lastSnapshot.getData();
            // insert new change to document to snapshot
            snapshot.insertTail(new ListEntity(document.copy()));

            users.addUserHistory(username, String.format("RESTORED document '%s' to time %d", title, time));
            history.insertHead(new StringEntity(String.format("User %s restored to time %d", username, time)));
            // insert time at which the restore was performed for future reference
            timeCommands.insertTail(new IntEntity(users.getTime()));

        }
    }

    //------------------------------------------------------
    // PURPOSE: this method restores the document content at the given time
    // It checks if the document actually existed at that time, in which case restore is not performed
    // otherwise the document is restored using the data saved in snapshot and
    // determined at which position it should be restored to using the information from timeCommand
    // Parameters: String username - user performing the operation so it can be added to history
    // int time - time at which the restore operation should be performed
    // UserManager users - access to users library so user history can be appropriately updated
    // Returns: (String) inform user if restore was successful or not
    //------------------------------------------------------
    public String restore (String username, int time, UserManager users) {

        // default information to tell user the outcome of operation
        String result = String.format("RESTORE: SUCCESS! document %s restored to time %d", title, time);

        // get the time at which the document was created
        IntEntity firstEditTime = (IntEntity) timeCommands.getNodeAtIndex(0).getData();
        // get the time at which the last change to document was performed
        IntEntity lastEditTime = (IntEntity) timeCommands.getNodeAtIndex(timeCommands.getLength() - 1).getData();

        // check if the given time exists inside timeCommand, in which case we will simply get the index at
        // findTime of snapshot and restore document at that point. if not, findTime will have a value of -1
        int findTime = timeCommands.getIndexOfNode(new IntEntity(time));

        // if the given time is smaller than the time document was created at, then restore fails
        if (time < firstEditTime.getData())
            result = String.format("RESTORE: NOT FOUND. Document %s did not exist at time %d", title, time);

        // if given time  is greater than the last edit on document, simply restore document to last update (last node of snapshot)
        else if (time > lastEditTime.getData())
            restoreHelper(timeCommands.getLength() - 1, username, time, users);

        // if given time exists in timeCommands, simply restore document to the node at index findTime of snapshot
        else if (findTime != -1)
            restoreHelper(findTime, username, time, users);

        // otherwise a search will have to be performed to determine the right position to restore the document to
        else {

            int snapshotIndex = -1; // holds the index at which the given time is lower than the time in timeCommands

            // traverse through the timeCommands until end or valid snapshotIndex is found
            for (int counter = 0; counter < timeCommands.getLength() && snapshotIndex == -1; counter++) {

                // get the IntEntity instance at index counter
                IntEntity snapshotTime = (IntEntity) timeCommands.getNodeAtIndex(counter).getData();

                // check if given time is lower than snapShotTime, in which case we have found the appropriate
                // index of snapshot at which restore should be performed
                if (time < snapshotTime.getData())
                    snapshotIndex = counter - 1;
            }

            // call helper method to perform restore and update necessary information
            restoreHelper(snapshotIndex, username, time, users);

        }

        return result;
    }
}
