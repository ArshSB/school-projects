// CLASS: IntEntity
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class extends Entity and stores integer which the linked list can use as a datatype
// It has a print method that simply outputs the integer and also an equal method
// that compares if another integer is equal
//-----------------------------------------

public class IntEntity extends Entity {

    private int data;

    public IntEntity(int data) {
        this.data = data;
    }

    public int getData() { return data; }

    public void setData(int data) { this.data = data; }

    public void print() { System.out.print(data); }

    public boolean equals(Entity data) {

        IntEntity other = (IntEntity) data;
        return this.data == other.getData();

    }

}
