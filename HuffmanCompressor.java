import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanCompressor {

    /**
     * HashMap to map the frequencies with their binary encoding
     */
    private static HashMap<Character, String> encodingMap = new HashMap<>();

    /**
     * Main Method - Takes command line arguments and feeds them into the huffmanEncoder() method
     * @param args Command Line Arguments
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException{
        HuffmanCompressor h = new HuffmanCompressor();
        System.out.println(h.huffmanEncoder(args[0], args[1], args[2]));
    }

    /**
     * Takes inputs from the main method and uses helper methods to perform the entire Huffman Encoding operation
     * @param inputFileName File which will be converted into binary
     * @param encodingFileName File which will be used to create the encoding for the input file
     * @param outputFileName File to which the encoding will be printed
     * @return String which states whether or not the program succeeded
     */
    public static String huffmanEncoder(String inputFileName, String encodingFileName, String outputFileName){
        try {
            //generate nodes based on the encoding file
            ArrayList<HuffmanNode> originalList = generateNodes(encodingFileName);
            //compose a tree based on those nodes
            ArrayList<HuffmanNode> treeList = generateTree(originalList);
            //create an output document which lists the characters, their frequencies, and their encodings
            try {
                generateOutput(originalList, treeList, inputFileName, outputFileName);
            } catch (FileNotFoundException e){
                return "Input File Error";
            }
            return "OK";
        } catch (FileNotFoundException e){
            return "Encoding File Error";
        }
    }

    /**
     * Generates the nodes based on the characters and their frequencies
     * @param encodingFileName File which will be used to determine the encodings
     * @return ArrayList of HuffmanNodes which are in reverse sorted order with initialized fields
     * @throws FileNotFoundException
     */
    public static ArrayList<HuffmanNode> generateNodes(String encodingFileName) throws FileNotFoundException{
        //Initialize the scanner
        File f = new File(encodingFileName);
        Scanner fileScanner = new Scanner(f);
        fileScanner.useDelimiter("");
        ArrayList<String> characters = new ArrayList<>(30);
        ArrayList<Integer> frequencies = new ArrayList<>(30);
        //Read the file
        while(fileScanner.hasNext()) {
            String charToAdd = fileScanner.next();
            if(characters.contains(charToAdd) == false){
                characters.add(charToAdd);
                frequencies.add(0);
            }
            int index = characters.indexOf(charToAdd);
            frequencies.set(index, frequencies.get(index) + 1);
        }
        //Sort the array of frequencies, adjust the characters appropriately
        for(int i = 0; i < frequencies.size(); i++){
            for(int j = i; j < frequencies.size(); j++){
                if(frequencies.get(j) > frequencies.get(i)){
                    //adjust the frequencies
                    int temp = frequencies.get(i);
                    frequencies.set(i, frequencies.get(j));
                    frequencies.set(j, temp);
                    //adjust the characters
                    String temp2 = characters.get(i);
                    characters.set(i, characters.get(j));
                    characters.set(j, temp2);
                }
            }
        }
        //Create the Huffman nodes list
        ArrayList<HuffmanNode> huffmanNodes = new ArrayList<>();
        for(int i = 0; i < frequencies.size(); i++){
            int temp = frequencies.get(i);
            char character = characters.get(i).charAt(0);
            HuffmanNode newNode = new HuffmanNode();
            newNode.setFrequency(temp);
            newNode.setInChar(character);
            huffmanNodes.add(i, newNode);
        }
        return huffmanNodes;
    }

    /**
     * Merges HuffmanNodes
     * @param a HuffmanNode a
     * @param b HuffmanNode b
     * @return HuffmanNode with a and b as right and left children
     */
    public static HuffmanNode mergeNodes(HuffmanNode a, HuffmanNode b){
        HuffmanNode mergedNode = new HuffmanNode();
        mergedNode.setRight(a);
        mergedNode.setLeft(b);
        mergedNode.setFrequency(a.getFrequency() + b.getFrequency());
        mergedNode.setInChar(null);
        return mergedNode;
    }

    /**
     * Generates the binary tree based on an ArrayList of HuffmanNodes
     * @param fullList Sorted ArrayList of HuffmanNodes which is used to construct the tree
     * @return ArrayList of length 1 with the root HuffmanNode
     */
    public static ArrayList<HuffmanNode> generateTree(ArrayList<HuffmanNode> fullList){
        //Until the list only contains the root node, which has links to all of the children
        while(fullList.size() != 1) {
            //Sort the array of frequencies, adjust the characters appropriately
            for(int i = 0; i < fullList.size(); i++) {
                for (int j = i; j < fullList.size(); j++) {
                    if (fullList.get(j).getFrequency() > fullList.get(i).getFrequency()) {
                        //adjust the frequencies
                        int temp = fullList.get(i).getFrequency();
                        fullList.get(i).setFrequency(fullList.get(j).getFrequency());
                        fullList.get(j).setFrequency(temp);
                        //adjust the characters
                        Character temp2 = fullList.get(i).getInChar();
                        fullList.get(i).setInChar(fullList.get(j).getInChar());
                        fullList.get(j).setInChar(temp2);
                    }
                }
            }
            ArrayList<HuffmanNode> tempList = fullList;
            fullList = new ArrayList<>();
            int i = tempList.size() - 1;
            //Moving through the ArrayList, putting smaller element to left
            for (int j = tempList.size() - 2; j >= 0; j = j - 2) {
                HuffmanNode a;
                HuffmanNode b;
                if(tempList.get(i).getFrequency() > tempList.get(j).getFrequency()){
                    a = tempList.get(i);
                    b = tempList.get(j);
                }
                else {
                    b = tempList.get(i);
                    a = tempList.get(j);
                }
                HuffmanNode mergedNode = mergeNodes(a, b);
                fullList.add(0, mergedNode);
                i = i - 2;
            }
            if(tempList.size() % 2 == 1 && tempList.size() != 1) {
                fullList.add(0, tempList.get(0));
            }
        }
        return fullList;
    }

    /**
     * Looks for a character in a tree of Huffman Nodes, returning the encoding that was used to find the character
     * @param n HuffmanNode which is the tree root
     * @param search Character which is to be searched for
     * @param encoding String which perpetually changes based on the path taken to get the encoding
     * @return String of encoding
     */
    public static String createEncoding(HuffmanNode n, Character search, String encoding){
        if(n.getLeft() == null && n.getRight() == null){
            if(n.getInChar().equals(search)){
                return encoding;
            }
            else {
                return null;
            }
        } else {
            String leftResult;
            String rightResult;
            if((leftResult = createEncoding(n.getLeft(), search, encoding + "0")) != null) {
                return leftResult;
            } else if ((rightResult = createEncoding(n.getRight(), search, encoding + "1")) != null) {
                return rightResult;
            }
        }
        return null;
    }

    /**
     * Manages all of the file work - Writes and reads from specified files and generates a binary output
     * @param originalList ArrayList of sorted HuffmanNodes
     * @param treeList ArrayList of length 1 with root of HuffmanNode tree as only element
     * @param inputFileName File which will be converted
     * @param outputFileName File which will be written to
     * @throws FileNotFoundException
     */
    public static void generateOutput(ArrayList<HuffmanNode> originalList,
                                      ArrayList<HuffmanNode> treeList, String inputFileName,
                                      String outputFileName) throws FileNotFoundException {
        File tableFile = new File("tableFile.txt");
        ArrayList<String> encodings = new ArrayList<>();
        try {
            tableFile.createNewFile();
            FileWriter writer = new FileWriter(tableFile);
            for(int i = 0; i < originalList.size(); i++){
                //Write out the character
                writer.write(originalList.get(i).getInChar());
                writer.write(" : ");
                writer.write("" + originalList.get(i).getFrequency());
                writer.write(" : ");
                writer.write("" + createEncoding(treeList.get(0), originalList.get(i).getInChar(), ""));
                encodings.add(0, "" + createEncoding(treeList.get(0), originalList.get(i).getInChar(), ""));
                writer.write(System.getProperty("line.separator"));
                //Write to the HashMap the encoding and the corresponding character
                encodingMap.put(originalList.get(i).getInChar(), createEncoding(treeList.get(0), originalList.get(i).getInChar(), ""));
            }
            writer.close();
        } catch(IOException e){
            return;
        }
        File outputFile = new File(outputFileName);
        File inputFile = new File(inputFileName);
        try {
            outputFile.createNewFile();
            FileWriter writer = new FileWriter(outputFile);
            Scanner scanner = new Scanner(inputFile);
            scanner.useDelimiter("");
            int originalCharacterCount = 0;
            int encodedCharacterCount = 0;
            while(scanner.hasNext()){
                String next = scanner.next();
                char charToEncode = next.charAt(0);
                writer.write("" + encodingMap.get(charToEncode));
                originalCharacterCount += 8;
                encodedCharacterCount += (encodingMap.get(charToEncode).length());
            }
            //Writes all of the calculations to the output file
            writer.write(System.getProperty("line.separator"));
            writer.write("Original Bit Count: " + originalCharacterCount);
            writer.write(System.getProperty("line.separator"));
            writer.write("Encoded Bit Count: " + encodedCharacterCount);
            writer.write(System.getProperty("line.separator"));
            writer.write("This results in " + (originalCharacterCount - encodedCharacterCount) + " fewer bits");
            writer.write(System.getProperty("line.separator"));
            writer.write("... or " + (((double) (originalCharacterCount - encodedCharacterCount) / ((double) originalCharacterCount) * 100 + " % savings!")));
            writer.close();
        } catch(IOException e) {
            throw new FileNotFoundException();
        }
    }
}
