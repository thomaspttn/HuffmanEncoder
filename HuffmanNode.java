public class HuffmanNode {
    private Character inChar;
    private int frequency;
    private HuffmanNode left;
    private HuffmanNode right;

    public Character getInChar() {
        return inChar;
    }

    public void setInChar(Character c){
        this.inChar = c;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int f){
        this.frequency = f;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanNode n){
        this.left = n;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public void setRight(HuffmanNode n){
        this.right = n;
    }
}
