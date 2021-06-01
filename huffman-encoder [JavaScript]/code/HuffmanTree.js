// CLASS: HuffmanTree
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class represents a Huffman Tree, has a createTree method that
// creates a tree with one node with given character and weight. Also has a
// combineTrees() that combines the given subtrees with the current tree. Has
// a compareTo() that checks if 2 huffman trees are same (same weight and same lowest char)
// or different. Has a search() that returns the path of a leaf node with given char
// as a string of "0" and "1"
//-----------------------------------------

"use strict";
let TreeNode = require("./TreeNode.js");
let LinkedList = require("./LinkedList.js");

class HuffmanTree {

    #_treeRoot; //root node of tree, its weight is the weight of the entire huffman tree

    constructor() {
        this.#_treeRoot = null;
    }

    get treeRoot() {
        return this.#_treeRoot;
    }

    // this function creates treeRoot with given character and weight
    createTree(char, weight) {
        this.#_treeRoot = new TreeNode(char, weight);
    }

    // this functions combines the current tree with 2 subtrees given, left and right. also updates the weight
    // of the tree after combining
    combineTrees(left, right) {

        // give the new root an empty value as its not a leaf node and therefore doesn't hold a char value
        this.#_treeRoot = new TreeNode("", left.treeRoot.weight + right.treeRoot.weight);
        this.#_treeRoot.left = left.treeRoot;
        this.#_treeRoot.right = right.treeRoot;

    }

    // this functions checks if 2 huffman trees are equal based on the criteria listed on assignment PDF
    // returns a boolean representing whether they are equal (0), whether the given tree comes before the
    // current tree (-1) or if the current tree comes before given tree (1)
    compareTo(other) {

        let result;

        // only compare if the other tree is a huffman tree
        if (other instanceof HuffmanTree) {

            let totalWeight = this.#_treeRoot.weight;
            let otherTotalWeight = other.#_treeRoot.weight;

            if (totalWeight === otherTotalWeight) {

                // find the smallest character from each tree in case they have same weight
                let smallestChar = this.#_getSmallestValue();
                let otherSmallestChar = other.#_getSmallestValue();

                if (smallestChar < otherSmallestChar)
                    result = -1;

                else if (smallestChar > otherSmallestChar)
                    result = 1;

                else
                    result = 0;

            }

            else if (totalWeight > otherTotalWeight)
                result = 1;

            else
                result = -1;

        }

        else
            throw new Error("Given object is not a Huffman Tree");

        return result;
    }

    // this functions returns a string of "1" (right) and "0" (left) which represents
    // the path of the given character from the root node of the huffman tree. Returns empty string
    // if char doesn't exist inside the tree
    search(char) {

        let pathString = this.#_searchHelper(this.#_treeRoot, char);
        return pathString.split("").reverse().join("");

    }

    #_searchHelper(node, char) {

        // base case where the node is null
        if (node != null) {

            // once a leaf node is found, check if it has the character we are looking for, otherwise return undefined
            if (node.left == null && node.right == null) {

                if (node.data == char)
                    return "";

            }

            else {

                // recurse every left node until the leaf node is reached
                let leftPath = this.#_searchHelper(node.left, char);
                // then recurse every right node to reach other leaf node
                let rightPath = this.#_searchHelper(node.right, char);

                // while recursing to left node, add "0" and "1" when recursing right node
                if (leftPath != undefined)
                    return leftPath + "0";

                else if (rightPath != undefined)
                    return rightPath + "1";

            }
        }
    }

    // this function is a private helper function that returns the smallest character based on the ASCII value of the character
    // in the huffman tree so it can be used to compare 2 trees later on
    #_getSmallestValue() {

        // list will hold all the characters inside the tree
        let list = new LinkedList();
        this.#_getSmallestValueHelper(this.#_treeRoot, list);

        // the following code finds the smallest character in the list and returns it
        let smallest = list.remove();
        let checkNext = list.remove();

        while (smallest != null && checkNext != null) {

            if (smallest > checkNext)
                smallest = checkNext;

            checkNext = list.remove();
        }

        return smallest;
    }

    #_getSmallestValueHelper(node, list) {

        // base case where node is null
        if (node != null) {

            // if node is leaf node, insert its data into the list
            if (node.left == null && node.right == null)
                list.insert(node.data);

            // otherwise recurse to all left nodes
            if (node.left != null)
                this.#_getSmallestValueHelper(node.left, list)

            // and recurse to all right nodes
            if (node.right != null)
                this.#_getSmallestValueHelper(node.right, list);

        }
    }
}

module.exports = HuffmanTree;