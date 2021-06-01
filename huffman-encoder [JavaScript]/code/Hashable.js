// CLASS: Hashable
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This abstract class has 2 subclasses: IntHash, StringHash
// which are used as keys for the Dictionary ADT. Has hashVal() and equals()
// function which the 2 subclasses must implement
//-----------------------------------------

"use strict";

class Hashable {

    constructor() {

        // so that you can't create an instance of an abstract class
        if(this.constructor === Hashable) {
            throw new Error("Do not create an abstract class");
        }

    }

    // returns the hash function of a key
    hashVal(){
        throw new Error("Missing hashVal function in a concrete class.");
    }

    // return boolean that compares if the key are same
    equals(other) {
        throw new Error("Missing equals function in a concrete class.");
    }

}

module.exports = Hashable;