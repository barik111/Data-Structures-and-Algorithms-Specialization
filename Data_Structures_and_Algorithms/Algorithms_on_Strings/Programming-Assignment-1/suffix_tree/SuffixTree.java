import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SuffixTree {
    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
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
            tree.add(newNode);
            currentNode.next[letterToIndex(text.charAt(initialStart))] = newNode.id;
            currentNode.haveNeighbours = true;
        }
        return tree;
    }

    // Build a suffix tree of the string text and return a list
    // with all of the labels of its edges (the corresponding 
    // substrings of the text) in any order.
    public List<String> computeSuffixTreeEdges(String text) {
        List<String> result = new ArrayList<String>();
        List<Node> tree = textToTree(text);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        while (!queue.isEmpty()) {
            Node currentNode = tree.get(queue.poll());
            if (currentNode.offset != -1) {
                result.add(text.substring(currentNode.start, currentNode.start + currentNode.offset + 1));
            }
            for (int i = 0; i < Node.Letters; i++) {
                if (currentNode.next[i] > 0) queue.add(currentNode.next[i]);
            }
        }
        return result;
    }


    static public void main(String[] args) throws IOException {
        new SuffixTree().run();
    }

    public void print(List<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        List<String> edges = computeSuffixTreeEdges(text);
        print(edges);
    }
}

class Node {
    public static final int Letters = 5;
    public static final int NA = -1;
    public int next[];
    int start;
    int offset;
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
}