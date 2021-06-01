// CLASS: StringEntity
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class extends Entity and stores strings which the linked list can use as a datatype
// It has a print method that simply outputs the string and also an equal method
// that compares if another string is equal via java's default equals method
//-----------------------------------------

public class StringEntity extends Entity {

    private String data;

    public StringEntity(String data) {
        this.data = data;
    }

    public String getData() { return data; }

    public void setData(String data) { this.data = data; }

    public void print() { System.out.print(data); }

    public boolean equals(Entity data) {

        StringEntity other = (StringEntity) data;
        return this.data.equals(other.getData());

    }
}
