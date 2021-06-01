// CLASS: TreeNode
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class represents a node of a binary tree.
// It holds the left and right subtree pointers
// as well as data that is stored inside the node.
// Also has a weight variable that is used to build Huffman Trees
//-----------------------------------------

"use strict";

class TreeNode {

    #_data;
    #_weight;
    #_left;
    #_right;

    constructor(data, weight) {

        if (arguments.length == 2 && typeof weight === 'number') {
            // set the subtree pointers to null at start
            this.#_data = data;
            this.#_weight = weight;
            this.#_left = null;
            this.#_right = null;
        }

        else
            throw Error("Invalid parameter length or given weight is not a number for TreeNode")

    }

    get data() {
        return this.#_data;
    }

    get weight() {
        return this.#_weight;
    }

    get left() {
        return this.#_left;
    }

    get right() {
        return this.#_right;
    }

    set data(newData) {
        this.#_data = newData;
    }

    set weight(newWeight) {
        this.#_weight = newWeight;
    }

    set left(newLeft) {
        this.#_left = newLeft;
    }

    set right(newRight) {
        this.#_right = newRight;
    }

}

module.exports = TreeNode;