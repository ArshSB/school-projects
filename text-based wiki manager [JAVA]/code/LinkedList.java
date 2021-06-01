// CLASS: LinkedList
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class builds a singly linked list data structure
// and includes the appropriate methods for a general purpose linked list
// such as insertHead, insertTail, insertAtIndex, removeHead, removeTail, removeAtIndex
// It also includes accessory methods such as replaceAtIndex and getIndexOfNode
// The linked list takes Entity as data and uses node class to store data
//-----------------------------------------

public class LinkedList {

    private Node head; // head represents the first node of the list
    private int length; // to keep track of number of nodes in list

    public LinkedList(Entity data) {
        insertHead(data);
        length = 1;
    }

    public LinkedList() {
        head = null;
        length = 0;
    }

    public Node getHead() { return head; }

    public void setHead(Node head) {
        this.head = head;
    }

    public int getLength() { return length; }

    public void setLength(int length) {
        this.length = length;
    }

    // copy() returns a copy of the current linked list
    public LinkedList copy() {

        LinkedList copyList = new LinkedList();

        // insert a copy of every node's data into copyList
        for (int counter = 0; counter < length; counter++)
            copyList.insertTail(this.getNodeAtIndex(counter).getData());

        return copyList;

    }

    //------------------------------------------------------
    // print()
    // PURPOSE: this method prints the linked list content
    // in the specified format inside the for loop.
    // currently it prints the index at front followed by content on new line
    // Returns: void - only prints to STDOUT
    //------------------------------------------------------
    public void print() {

        // only perform print is linked list is not empty
        if (length > 0) {

            Node current = head; // set current to head to start node traversal

            // traverse all nodes of list and print the content in the specified format
            for (int counter = 0; counter < length; counter++) {

                System.out.printf("(%d) ", counter);
                current.getData().print();
                System.out.println("");
                // set current to next node for traversal
                current = current.getNext();

            }
        }

        else
            System.out.println("[Empty]");
    }

    //------------------------------------------------------
    // getIndexOfNode(Entity)
    // PURPOSE: this method returns the index of a node with given data
    // if it exists, otherwise it returns -1. The index starts from 0,
    // meaning 0 would represent the head node of list
    // PARAMETERS: takes Entity datatype as input which represents
    // the data to search for in the nodes of the list.
    // No alteration to the parameters is performed
    // Returns:
    // - (-1) if no node with given data exists in list
    // - integer from [0 to (length - 1)] if node with given data exists
    //------------------------------------------------------
    public int getIndexOfNode(Entity data) {

        int index = -1; // set default value

        // only perform search if list is not empty
        if (length > 0) {

            Node current = head; // set current to head for traversal

            // keep traversing nodes until end of list or node with given data is found, in which case record the index
            for (int counter = 0; counter < length && index == -1; counter++) {

                if (current.getData().equals(data))
                    index = counter;

                current = current.getNext();

            }
        }

        return index;
    }

    //------------------------------------------------------
    // getNodeAtIndex(int)
    // PURPOSE: this method returns a node from the list at the given index
    // Index is assumed to start from 0 (representing the head node)
    // PARAMETERS: takes int value representing the index of the node
    // No alteration to the parameters is performed
    // Returns:
    // - (null) if index is invalid (below 0 and above the length of the list)
    // - (Node) node at given index if index is valid
    //------------------------------------------------------
    public Node getNodeAtIndex(int index) {

        Node current = null; // set default to null

        // only perform search if index is valid
        if (index >= 0 && index < length) {

            current = head; // set current to head for traversal

            // traverse nodes and set current as the next node until the index position is reached
            for (int counter = 0; counter < index; counter++)
                current = current.getNext();

        }

        return current;
    }

    // this method inserts a node at the head of the linked list with given data
    public void insertHead(Entity data) {

        Node newNode = new Node(data);
        newNode.setNext(head);
        head = newNode;
        length++;

    }

    //------------------------------------------------------
    // insertTail(Entity)
    // PURPOSE: this method inserts a node at the end of the linked list (tail)
    // with given data.
    // No alteration to the parameters is performed
    // Parameters: Entity - data that the node to be inserted will hold
    // Returns: void, only performs insertion
    //------------------------------------------------------
    public void insertTail(Entity data) {

        // only attempt to insert at tail if list is not empty
        if (length > 0) {

            Node newNode = new Node(data); // node to be inserted as new tail node
            newNode.setNext(null); // set next of node to be insert to null since it will be new tail node
            Node tail = getNodeAtIndex(length - 1); // get the current tail node
            tail.setNext(newNode); // set current tail node's next to be the new tail node
            length++;

        }
        // otherwise simply insert at head
        else
            insertHead(data);

    }

    //------------------------------------------------------
    // insertAtIndex(Entity, int)
    // PURPOSE: this method inserts a node with given data at the given index into the linked list
    // index is assumed to start from 0 (representing the head node)
    // No alteration to the parameters is performed
    // Parameters: Entity - data that the node to be inserted will hold
    // int - index at which the node is to be inserted at
    // Returns: void, only performs insertion
    //------------------------------------------------------
    public void insertAtIndex(Entity data, int index) {

        // if given index is 0, simply insert at head
        if (index == 0)
            insertHead(data);

        // if given index is last position of list, simply insert at tail
        else if (index == length - 1)
            insertTail(data);

        // otherwise check if index is valid before performing insertion in middle
        else if (index >= 0 && index < length) {

            Node newNode = new Node(data); // new node to be inserted
            Node indexNode = getNodeAtIndex(index - 1); // get the current node at given index
            newNode.setNext(indexNode.getNext()); // set new node next to be the current node
            indexNode.setNext(newNode); // set next of current node to new node, completing insertion
            length++;
        }
    }

    // this method simply removes the first node of the linked list
    public void removeHead() {

        // only perform removal if list is not empty
        if (head != null && length > 0) {

            head = head.getNext();
            length--;

        }
    }

    // this method removes the last node of the linked list
    public void removeTail() {

        // only perform tail node removal if more than 1 node exists in list
        if (length > 1){

            Node secondLast = getNodeAtIndex(length - 2); // get current second last node of list
            secondLast.setNext(null);
            // set second last node next to be null, java garbage collection will collect the tail node, completing removal
            length--;

        }
        // otherwise if only 1 or less node exists, simple remove at head
        else
            removeHead();

    }

    //------------------------------------------------------
    // removeAtIndex(int)
    // PURPOSE: this method removes node at given index from the linked list
    // index starts from 0 which represents the first node of the list
    // No alteration to the parameters is performed
    // Parameters: int - index at which the occupying node is to be removed
    // Returns: void, only performs removal
    //------------------------------------------------------
    public void removeAtIndex(int index) {

        // perform removal only when the linked list is not empty
        if (length > 0) {

            if (index == 0)
                removeHead();

            else if (index == length - 1)
                removeTail();

            // check if given index is valid
            else if (index >= 0 && index < length) {

                Node previous = getNodeAtIndex(index - 1); // get node left of the given index
                previous.setNext(previous.getNext().getNext());
                // set previous next to be the next of node to be deleted, completing removal
                length--;

            }
        }
    }

    //------------------------------------------------------
    // replaceAtIndex(Entity, int)
    // PURPOSE: this method replaces the data of node at given index
    // index is assumed to start from 0 (representing the head node)
    // No alteration to the parameters is performed
    // Parameters: Entity - new data to replace the old data of node at given index
    // int - index of node at which the replacement should occur
    // Returns: void, only replaces data
    //------------------------------------------------------
    public void replaceAtIndex(Entity data, int index) { //index is 0 : length - 1

        // perform replacement only when list is not empty
        if (length > 0) {

            // check if given index is valid
            if (index >= 0 && index < length) {

                Node indexNode = getNodeAtIndex(index);
                indexNode.setData(data); //

            }
        }
    }

    // this method simply deletes the entire linked list by setting head to null
    // and have java garbage collection collect the content of the list
    public void reset() {

        head = null;
        length = 0;

    }


}
