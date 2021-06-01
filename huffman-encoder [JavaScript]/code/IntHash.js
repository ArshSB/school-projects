// CLASS: IntHash
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class inherits Hashable abstract class, it takes in a integer key
// and returns the hash function of a integer for the dictionary ADT. Also has a equal()
// method to compare 2 integer keys
//-----------------------------------------

"use strict";
let Hashable = require("./Hashable.js");

class IntHash extends Hashable {

    #_val; // holds the string key

    constructor(val) {

        super();

        // make sure the given value is an integer
        if (arguments.length == 1 && typeof val === 'number')
            this.#_val = val;

        else
            throw new Error("No integer or non integer value is given for IntHash");

    }

    get val() {
        return this.#_val;
    }

    // returns the hash function value of a integer key which is simply the value of the key
    hashVal() {
        return this.#_val;
    }

    // compares whether 2 int keys are the same by comparing their int values
    equals(other) {

        let result = false;

        if (other instanceof IntHash) {

            if(other.val === this.#_val)
                result = true;

        }

        else if (!(other instanceof Hashable))
            throw new Error("Given object is not an instance of IntHash");

        return result;
    }
}

module.exports = IntHash;