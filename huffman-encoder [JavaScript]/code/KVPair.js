// CLASS: KVPair
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class holds a key-value pair of the dictionary ADT
// They key is of hashable type while the value is simple any value associated
// with they key. Has a equals() method that compares 2 KVPairs via their key
//-----------------------------------------

"use strict";
let Hashable = require("./Hashable.js");

class KVPair {

    #_key; // of Hashable type
    #_value;

    constructor(key, value) {

        if (arguments.length == 2 && key instanceof Hashable) {
            this.#_key = key;
            this.#_value = value;
        }

        else
            throw Error("Invalid parameter length given for KVPair or given key is not of type Hashable")
    }

    get key() {
        return this.#_key;
    }

    get value() {
        return this.#_value;
    }

    set key(newKey) {
        this.#_key = newKey;
    }

    set value(newValue) {
        this.#_value = newValue;
    }

    // compares 2 key-value pairs by calling the equals() method of the hashable key
    equals(other) {

        // only compare if the other is also a KVPair
        if (other instanceof KVPair)
            return this.#_key.equals(other.key);

        else
            throw new Error("Given object is not an instance of KVPair");

    }
}

module.exports = KVPair;