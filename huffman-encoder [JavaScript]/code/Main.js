//-----------------------------------------
// NAME		: Arshpreet Buttar
// COURSE		: COMP 2150
//
// REMARKS: This program implements a huffman encoder of an input file
// using huffman trees and also extensively uses the dictionary ADT built using
// hashtable. The program takes in a hardcoded input file name and reads its content
// and outputs a file that includes the encoding with the same name as the input file
// with the .huff extension
//-----------------------------------------

"use strict";
let HuffmanEncoder = require("./HuffmanEncoder.js");

function main() {

    // change "hamlet.txt" with the input file name of your choice if needed
    let encoder = new HuffmanEncoder("official-input-hamlet.txt");
    encoder.encode();

}

main();