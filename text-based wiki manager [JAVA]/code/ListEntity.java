// CLASS: ListEntity
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class extends Entity and stores LinkedList Instance which the linked list can use as a datatype
// It has a print method that simply outputs the memory address of the linked list instance and also an equal method
// that compares if another linked list has same memory address
//-----------------------------------------

public class ListEntity extends Entity {

    private LinkedList data;

    public ListEntity(LinkedList data) {
        this.data = data;
    }

    public LinkedList getData() { return data; }

    public void setData(LinkedList data) { this.data = data; }

    public void print() { System.out.print(data); } //prints memory address of linked list

    public boolean equals(Entity data) { // compares memory address of two linked lists

        ListEntity other = (ListEntity) data;
        return this.data == other.getData();
    }
}
