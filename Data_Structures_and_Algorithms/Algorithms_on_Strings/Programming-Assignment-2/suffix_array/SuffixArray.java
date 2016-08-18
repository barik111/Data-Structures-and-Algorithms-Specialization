import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SuffixArray {
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

    public class Suffix implements Comparable {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Object o) {
            Suffix other = (Suffix) o;
            return suffix.compareTo(other.suffix);
        }
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {

        List<Node> tree = textToTree(text);
        TreeMap<String, Integer> sortSuffix = new TreeMap<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        while (!queue.isEmpty()) {
            Node currentNode = tree.get(queue.poll());
            if (currentNode.haveNeighbours) {
                queue.addAll(currentNode.getNeighbours());
            } else {
                sortSuffix.put(text.substring(currentNode.generalStart, currentNode.start + currentNode.offset + 1), currentNode.generalStart);
            }
        }
        int[] result = new int[sortSuffix.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = sortSuffix.pollFirstEntry().getValue();
        }
        return result;
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

    static public void main(String[] args) throws IOException {
        new SuffixArray().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] SuffixArray = computeSuffixArray(text);
        print(SuffixArray);
    }
}

class Node {
    public static final int Letters = 5;
    public static final int NA = -1;
    public int next[];
    int start;
    int offset;
    int generalStart;
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
        for (int i = 0; i < next.length; i++) {
            if (next[i] > 0) result.add(next[i]);
        }
        return result;
    }
}