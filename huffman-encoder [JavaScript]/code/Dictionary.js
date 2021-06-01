// CLASS: Dictionary
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class implements the dictionary ADT via a hashtable
// that uses separate chaining to handle collisions. Has a put() method that
// takes in a KVPair object and inserts into the hashtable. Also has get() method
// which returns the value associated with the given key if it exists as well
// as contains() which returns whether the given key exists inside the hashtable
//-----------------------------------------

"use strict";
let Hashable = require("./Hashable.js");
let LinkedList = require("./LinkedList");
let KVPair = require("./KVPair.js");

class Dictionary {

    #_table; // the hashtable: array of linked lists
    #_length; // the max size of the hashtable
    #_size; // current size of the hashtable representing how many KVPairs are inside hashtable

    constructor(length) {

        if (arguments.length == 1 && length >= 0) {
            this.#_size = 0;
            this.#_length = length;
            this.#_table = new Array(length);

            for (let counter = 0; counter < length; counter++)
                this.#_table[counter] = new LinkedList();
        }

        else {
            throw Error("Dictionary must be given a length or negative length given");
        }

    }

    get length() {
        return this.#_length;
    }

    get size() {
        return this.#_size;
    }

    // returns if hashtable is empty
    isEmpty() {
        return this.#_size === 0;
    }

    // takes in a hashable key and value and inserts into the hashtable at the index of the hash function
    // of the given key. If key already exists the old value is replaced with new value, otherwise the KVPair
    // is added to the hashtable
    put(k, v) {

        // only insert if the given key is of hashtable type
        if (this.#_checkHashable(k) && this.#_length > 0) {

            // get the index using the hash function of key and modulo so it can fit inside hashtable
            let index = k.hashVal() % this.#_length;

            // check first if key value already exists inside the hashtable in which case replace the values
            let replace = this.#_table[index].replace(new KVPair(k, v));

            // if replace is false, meaning key doesnt exist inside hashtable, simply insert KVPair and increment size
            if (!replace) {
                this.#_table[index].insert(new KVPair(k, v));
                this.#_size++;
            }

        }
    }

    // this method takes in a hashable key and returns the associated value from the hashtable, if it exists
    // returns undefined in the case the key doesn't exist inside hashtable
    get(k) {

        // only try getting value if key is of hashtable type
        if (this.#_checkHashable(k) && this.#_length > 0) {

            // get the index using the hash function of key
            let index = k.hashVal() % this.#_length;

            // check if the key exists inside the linked list (giving a default -1 value for the value) and return
            // the value associated with the key
            let checkContains = this.#_table[index].contains(new KVPair(k, -1));

            // only return the value if key exists
            if (checkContains != null)
                return checkContains.value;

            // else javascript will automatically return undefined if checkContains == null
        }
    }

    // this method takes in a hashable key and returns boolean if the key exists in the hashtable
    contains(k) {

        let result = false;

        if (typeof this.get(k) !== 'undefined')
            result = true;

        return result;

    }

    // private helper function that checks if the given key is of type hashable
    #_checkHashable(other) {

        if (other instanceof Hashable)
            return true;

        else
            throw new Error("Parameter must be of type Hashable");
    }
}

module.exports = Dictionary;