import java.util.List;
import java.util.ArrayList;

public class Test {
        public static void main(String[] args) {
        List<HuffmanCharacter> c = new ArrayList<>();
        c.add(new HuffmanCharacter('a', 45));
        c.add(new HuffmanCharacter('b', 13));
        c.add(new HuffmanCharacter('c', 12));
        c.add(new HuffmanCharacter('d', 16));
        c.add(new HuffmanCharacter('e', 9));
        c.add(new HuffmanCharacter('f', 5));
        HuffmanEncoder.Node root = HuffmanEncoder.encode(c);
        HuffmanEncoder.printTree(root);
        System.out.println("Max. bits length: " + HuffmanEncoder.getMaxBitLength(root));
        System.out.println("Encoding(e): " + HuffmanEncoder.getEncoding(root, 'e'));
        System.out.println("|Encoding(e)|: " + HuffmanEncoder.getBitLength(root, 'e'));
    }
}
