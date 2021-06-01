// CLASS: DocumentEntity
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class extends Entity and stores Document Instance which the linked list can use as a datatype
// It has a print method that simply outputs the name of the Document instance and also an equal method
// that compares if another DocumentEntity has same name
//-----------------------------------------

public class DocumentEntity extends Entity {

    private Document data;

    public DocumentEntity(Document data) { this.data = data; }

    public Document getData() { return data; }

    public void setData(Document data) { this.data = data; }

    public void print() { System.out.print(data.getTitle()); }

    public boolean equals(Entity data) {

        StringEntity other = (StringEntity) data;
        return this.data.getTitle().equals(other.getData());

    }

}
