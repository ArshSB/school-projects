// CLASS: UserEntity
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class extends Entity and stores User Instance which the linked list can use as a datatype
// It has a print method that simply outputs the username of the User instance and also an equal method
// that compares if another UserEntity has same username
//-----------------------------------------

public class UserEntity extends Entity {

    private User data;

    public UserEntity(User data) {
        this.data = data;
    }

    public User getData() { return data; }

    public void setData(User data) { this.data = data; }

    public void print() { System.out.print(data.getUsername()); }

    public boolean equals(Entity data) {

        StringEntity other = (StringEntity) data;
        return this.data.getUsername().equals(other.getData());

    }

}
