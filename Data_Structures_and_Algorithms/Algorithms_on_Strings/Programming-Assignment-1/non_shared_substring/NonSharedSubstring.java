import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class NonSharedSubstring implements Runnable {
    String solve(String p, String q) {
        String text = p + "#" + q + "$";
        List<Node> tree = textToTree(text);
        List<String> sortSuffix = new ArrayList<>();
        setSide(tree, sortSuffix, "", tree.get(0), text, p.length());
        return sortSuffix.get(0);
    }

    public int setSide(List<Node> tree, List<String> suffixes, String suffix, Node root, String text, int half) {
        if (!root.haveNeighbours) {
            root.side = root.start < half + 1 ? 1 : 0;
        } else {
            for (Integer neighbour : root.getNeighbours()) {
                root.side *= setSide(tree, suffixes, suffix + text.substring(root.start, root.start + root.offset + 1), tree.get(neighbour), text, half);
            }
        }
        if (root.side == 1) {
            root.suffix = suffix;
            if (root.start < half) {
                root.suffix += text.charAt(root.start);
                if (suffixes.isEmpty() || suffixes.get(0).length() > root.suffix.length())
                    suffixes.add(0, root.suffix);
            }
        }
        return root.side;
    }

    int letterToIndex(char letter) {
        switch (letter) {
            case 'A':
                return 0;
            case 'C':
                return 1;
            case 'G':
                return 2;
            case 'T':
                return 3;
            case '$':
                return 4;
            case '#':
                return 5;
            default:
                assert (false);
                return Node.NA;
        }
    }

    List<Node> textToTree(String text) {
        List<Node> tree = new ArrayList<>();
        int count = 0;
        tree.add(new Node(0, -1, count++));
        int length = text.length();

        for (int j = 0; j < length; j++) {
            int initialStart = length - 1 - j;
            int initialOffset = j;
            Node currentNode = tree.get(0);
            while (currentNode.next[letterToIndex(text.charAt(initialStart))] > 0) {
                currentNode = tree.get(currentNode.next[letterToIndex(text.charAt(initialStart))]);
                int currentStart = currentNode.start;
                int currentOffset = currentNode.offset;
                int removeIndex = 1;
                for (int i = 1; i < currentOffset + 1; i++) {
                    if (text.charAt(currentStart + i) != text.charAt(initialStart + i)) {
                        break;
                    }
                    removeIndex++;
                }

                if (currentOffset + 1 - removeIndex > 0) {
                    Node newNode = new Node(currentStart + removeIndex, currentOffset - removeIndex, count++);
                    newNode.generalStart = currentNode.generalStart;
                    currentNode.start = initialStart;
                    currentNode.offset = removeIndex - 1;
                    tree.add(newNode);
                    if (currentNode.haveNeighbours) {
                        newNode.next = Arrays.copyOf(currentNode.next, currentNode.next.length);
                        newNode.haveNeighbours = true;
                        currentNode.initNext();
                    }
                    currentNode.next[letterToIndex(text.charAt(newNode.start))] = newNode.id;
                    currentNode.haveNeighbours = true;
                }
                initialStart += removeIndex;
                initialOffset -= removeIndex;
            }
            Node newNode = new Node(initialStart, initialOffset, count++);
            newNode.generalStart = length - 1 - j;
            tree.add(newNode);
            currentNode.next[letterToIndex(text.charAt(initialStart))] = newNode.id;
            currentNode.haveNeighbours = true;
        }
        return tree;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String p = in.readLine();
            String q = in.readLine();

            String ans = solve(p, q);

            System.out.println(ans);
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new Thread(new NonSharedSubstring()).start();
    }
}


class Node {
    public static final int Letters = 6;
    public static final int NA = -1;
    public int next[];
    int start;
    int offset;
    int generalStart;
    int side = 1;
    String suffix = "";
    public int id;
    public boolean haveNeighbours;

    Node() {
        next = new int[Letters];
        Arrays.fill(next, NA);
    }


    Node(int start, int offset, int id) {
        this();
        this.start = start;
        this.offset = offset;
        this.id = id;
    }

    public void initNext() {
        Arrays.fill(next, NA);
        haveNeighbours = false;
    }

    List<Integer> getNeighbours() {
        List<Integer> result = new ArrayList<>();
        for (int aNext : next) {
            if (aNext > 0) result.add(aNext);
        }
        return result;
    }
}