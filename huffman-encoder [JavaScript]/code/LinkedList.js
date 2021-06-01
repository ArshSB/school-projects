"use strict";
let ListNode = require("./ListNode.js");

class LinkedList {

    #_head;
    #_length;

    constructor() {

        this.#_length = 0;
        this.#_head = null;

    }

    get head() {
        return this.#_head.data;
    }

    get length() {
        return this.#_length;
    }

    insert(data) {

        let node = new ListNode(data);

        node.next = this.#_head;
        this.#_head = node;
        this.#_length++;

    }

    remove() {

        let result = null;

        if (this.#_head != null) {

            result = this.#_head.data;
            this.#_head = this.#_head.next;
            this.#_length--;

        }

        return result;
    }

    replace(data) { //replaces a node with new value if it exists

        let result = false;

        if (this.#_head != null) {

            let current = this.#_head;

            while (current != null && !result) {

                if (current.data.equals(data)) {
                    current.data = data;
                    result = true;
                }

                else
                    current = current.next;

            }
        }
    }

    contains(data) {

        let result = null;

        if (this.#_head != null) {

            let current = this.#_head;

            while (current != null && result == null) {

                if (current.data.equals(data))
                    result = current.data;

                else
                    current = current.next;
            }
        }

        return result;
    }

    isEmpty() {
        return this.#_length === 0;
    }
}

module.exports = LinkedList;
