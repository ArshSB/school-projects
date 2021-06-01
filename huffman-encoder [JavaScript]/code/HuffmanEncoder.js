// CLASS: HuffmanEncoder
//
// Author: Arsh Buttar, 7893871
//
// REMARKS: This class encodes an input file using huffman trees as explained
// in the assignment PDF. An output file containing the encoding is outputted
// with the same name as the input file but with a .huff extension
//-----------------------------------------

"use strict";
let fs = require("fs");
let HuffmanTree = require("./HuffmanTree.js");
let Dictionary = require("./Dictionary.js");
let StringHash = require("./StringHash.js");

class HuffmanEncoder {

    #_fileName; // file name of the input file
    #_freqDict; // dictionary that holds the frequency of all characters from the input file
    #_pathDict; // dictionary that holds the all path encoding of unique characters from the input file
    #_uniqueChar; // an array that holds all unique char that appear in input file
    #_input; // reads the input file

    constructor(fileName) {

        if (arguments.length == 1 && typeof fileName === 'string') {
            this.#_fileName = fileName;
            this.#_uniqueChar = [];
            this.#_input = fs.readFileSync(this.#_fileName, "utf8");
            this.#_pathDict = new Dictionary(256);
            // 256 is maximum number of ASCII characters therefore the perfect length for a dictionary
            // that works with characters
            this.#_freqDict = new Dictionary(256);
            fs.writeFileSync(this.#_fileName + ".huff", "");
        }

        else
            throw new Error("No file name or non string value is given for HuffmanEncoder");
    }

    encode() {

        console.log("Starting encoding...");

        console.log("(1/4) Counting character frequencies...");
        this.#_countCharFreq();

        console.log("(2/4) Building Huffman trees...");
        let trees = new Array(this.#_uniqueChar.length);

        // the following creates huffman trees with each unique char that appears in input file as its data
        // and the frequency of that character as its weight
        for (let tree = 0; tree < trees.length; tree++) {

            let char = this.#_uniqueChar[tree];
            trees[tree] = new HuffmanTree();
            trees[tree].createTree(char, this.#_freqDict.get(new StringHash(char)));

        }

        console.log("(3/4) Now sorting and combining Huffman trees...");

        // the following code sorts all the trees and combines the smallest 2 trees into 1 tree
        // and repeats the process until only 1 tree inside the trees array remain, as explained in
        // assignment PDF
        while (trees.length > 1) {

            trees = trees.sort( (first, second) => first.compareTo(second) );
            let newTree = new HuffmanTree();
            newTree.combineTrees(trees[0], trees[1]);
            // add new tree to array and remove the first 2 trees from array
            trees.push(newTree);
            trees.shift();
            trees.shift();

        }


        console.log("(4/4) Writing character encoding to output file...");

        // store the path encoding of all unique char to the pathDict dictionary
        for (let char = 0; char < this.#_uniqueChar.length; char++) {

            let pathString = trees[0].search(this.#_uniqueChar[char]);
            this.#_pathDict.put(new StringHash(this.#_uniqueChar[char]), pathString);

        }

        // holds the entire encoding as a string
        let fileEncoding = "";

        // go through every char that appears in input file in order and append that character's path encoding
        // to the fileEncoding string
        for (let char = 0; char < this.#_input.length; char++) {

            fileEncoding += this.#_pathDict.get(new StringHash(this.#_input[char])) + " ";

        }

        // append encoding to output file
        fs.appendFileSync(this.#_fileName + ".huff", fileEncoding);
        fs.appendFileSync(this.#_fileName + ".huff", "\n");

        console.log("Encoding fully finished!");

    }

    // this function is a private helper that counts the character frequency of the input file
    // and stores the data into a dictionary with the key being a character and the value being the frequency
    #_countCharFreq() {

        for (let char = 0; char < this.#_input.length; char++) {

            let key = new StringHash(this.#_input[char]);

            // add the character to dictionary for first time
            if (!this.#_freqDict.contains(key)) {

                // add it to unique character dictionary
                this.#_uniqueChar.push(this.#_input[char]);
                let frequency = 0;

                // calculate how many times the char appears inside the input file to find its frequency
                for (let j = 0; j < this.#_input.length; j++) {

                    if (this.#_input[j] == this.#_input[char])
                        frequency++;

                }

                this.#_freqDict.put(key, (frequency / this.#_input.length) );
            }
        }
    }
}

module.exports = HuffmanEncoder;