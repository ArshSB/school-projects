// CLASS: Entity
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This abstract class lists the only datatype which the linked list data structure
// uses and stores. It includes the abstract method print and equals which its subclasses, also
// datatype that the linked list can store, must implement. In general, polymorphism and inheritance
// is used to allow linked list to store a limited number of datatype that extend Entity
//-----------------------------------------

public abstract class Entity {

    public abstract void print ();

    public abstract boolean equals (Entity data);

}


