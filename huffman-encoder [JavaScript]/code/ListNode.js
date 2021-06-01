// CLASS: ListNode
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class represents a node of a linked list.
// It holds the next pointer of the next node in the linked list
// as well as data that is stored inside the node.
//-----------------------------------------

"use strict";

class ListNode {

    #_data;
    #_next;

    constructor(data) {

        if (arguments.length == 1) {
            this.#_data = data;
            this.#_next = null;
        }

        else
            throw Error("No parameter given for ListNode");

    }

    get data() {
        return this.#_data;
    }

    get next() {
        return this.#_next;
    }

    set data(data) {
        this.#_data = data;
    }

    set next(next) {
        this.#_next = next;
    }

}

module.exports = ListNode;