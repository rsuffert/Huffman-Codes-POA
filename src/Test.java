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
        HuffmanEncoder.Node root = HuffmanEncoder.generateTree(c);
        
        HuffmanEncoder.printTree(root);
        System.out.println("Max. bit length: " + HuffmanEncoder.getMaxBitLength(root));
        String encodingE = HuffmanEncoder.getEncoding(root, 'e');
        System.out.println("Encoding(e): " + encodingE);
        System.out.println("|Encoding(e)|: " + HuffmanEncoder.getBitLength(root, 'e'));
        System.out.println("Decode(Encoding(e)): " + HuffmanEncoder.decodeChar(root, encodingE));
        String encoding = HuffmanEncoder.encode(root, "abef");
        System.out.println("Encode(abef): "+ encoding);
        System.out.println("Decode(Encode(abef)): " + HuffmanEncoder.decode(root, encoding));
    }
}
