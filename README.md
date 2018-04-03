# HuffmanEncoder
Huffman Encoder Project
@thomaspttn, tjpatton1@gmail.com

# Intro
This idea was loosely based around the idea of creating a Java Huffman Encoder. Huffman encoding works by assigning variable length binary codes to letters based on the frequency of their appearances in the text, and then outputing a binary stream of the file. This program doesn't actually output a binary stream but instead a representation of what the binary would be in a notepad file.

# Program
The code takes 3 command line arguments: The input file, the encoding file, and the output file. The most important file is the encoding file, as it is the file which the binary encodings are based off of. It counts the frequencies of the characters in the binary encoding file, sorts them into an array, and then constructs a Huffman tree for each character based off of the frequencies. Once it has the tree constructed, it determines the binary encoding for each character based on its position in the tree, and then constructs a hash table based on the values. It then reads the input file characters and outputs the binary representation to the output file. I also chose to have the program construct a TableFile.txt which stores all of the characters, their frequencies, and their binary representation.

# Other
The only issues with this program that I have found deals with character representations. Some of the ASCII values for things like tabs are messed up with the dictionary file.This is an issue that could be revised, but wasn't required for the span of this project.


