// CLASS: StringHash
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class inherits Hashable abstract class, it takes in a string key
// and returns the hash function of a string for the dictionary ADT. Also has a equal()
// method to compare 2 string keys
//-----------------------------------------

"use strict";
let Hashable = require('./Hashable.js');

class StringHash extends Hashable {

    #_val; // holds the string key

    constructor(val) {

        super();

        // make sure the given value is a string
        if (arguments.length == 1 && typeof val === 'string')
            this.#_val = val;

        else
            throw new Error("No string or non string value is given for StringHash");

    }

    get val() {
        return this.#_val;
    }

    // returns the hash function value of a string key based on the formula given in assignment PDF
    hashVal() {

        let result = 0;
        let size = this.#_val.length;
        let prime = 5;

        for (let counter = 0; counter < size; counter++)
            result += ( this.#_val.charCodeAt(counter) ) * ( prime ** ( size - counter - 1) );

        return result;

    }

    // compares whether 2 string keys are the same by comparing their string values
    equals(other) {

        let result = false;

        if (other instanceof StringHash) {

            if (other.#_val === this.#_val)
                result = true;

        }

        else if (!(other instanceof Hashable))
            throw new Error("Given object is not an instance of StringHash");

        return result;
    }
}

module.exports = StringHash;