// CLASS: Node
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class holds the necessary information for a node, an essential part of the
// linked list data structure. It hold Entity and its subclasses as datatype
//-----------------------------------------

public class Node {

    private Node next;
    private Entity data;

    public Node(Entity data, Node next) {
        this.data = data;
        this.next = next;
    }

    public Node(Entity data) {
        this.data = data;
    }

    public Entity getData() { return data; }

    public void setData(Entity data) { this.data = data; }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

}
