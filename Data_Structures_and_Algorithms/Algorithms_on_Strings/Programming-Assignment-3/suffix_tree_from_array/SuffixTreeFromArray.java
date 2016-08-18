import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SuffixTreeFromArray {
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

    // Data structure to store edges of a suffix tree.
    public class Edge {
        // The ending node of this edge.
        int node;
        // Starting position of the substring of the text 
        // corresponding to the label of this edge.
        int start;
        // Position right after the end of the substring of the text 
        // corresponding to the label of this edge.
        int end;

        Edge(int node, int start, int end) {
            this.node = node;
            this.start = start;
            this.end = end;
        }
    }

    class TreeNode {
        public int id;
        public int parent;
        public Map<Character, Integer> children;
        public int depth;
        public int start;
        public int end;

        public TreeNode(int id, int parent, Map<Character, Integer> children, int depth, int start, int end) {
            this.id = id;
            this.parent = parent;
            this.children = children;
            this.depth = depth;
            this.start = start;
            this.end = end;
        }
    }

    public void createNewLeaf(List<TreeNode> tree, TreeNode parent, String text, int suffix) {
        TreeNode leaf = new TreeNode(tree.size(), parent.id, new HashMap<>(), text.length() - suffix, suffix + parent.depth, text.length() - 1);
        tree.add(leaf);
        parent.children.put(text.charAt(leaf.start), leaf.id);
    }

    public TreeNode breakEdge(List<TreeNode> tree, TreeNode node, String text, int start, int offset) {
        Character startChar = text.charAt(start);
        Character midChar = text.charAt(start + offset);
        TreeNode midNode = new TreeNode(tree.size(), node.id, new TreeMap<>(), node.depth + offset, start, start + offset - 1);
        tree.add(midNode);
        tree.get(node.children.get(startChar)).start += offset;
        midNode.children.put(midChar, node.children.get(startChar));
        tree.get(node.children.get(startChar)).parent = midNode.id;
        node.children.put(startChar, midNode.id);
        return midNode;
    }

    // Build suffix tree of the string text given its suffix array suffix_array
    // and LCP array lcp_array. Return the tree as a mapping from a node ID
    // to the list of all outgoing edges of the corresponding node. The edges in the
    // list must be sorted in the ascending order by the first character of the edge label.
    // Root must have node ID = 0, and all other node IDs must be different
    // nonnegative integers.
    //
    // For example, if text = "ACACAA$", an edge with label "$" from root to a node with ID 1
    // must be represented by new Edge(1, 6, 7). This edge must be present in the list tree.get(0)
    // (corresponding to the root node), and it should be the first edge in the list
    // (because it has the smallest first character of all edges outgoing from the root).
    Map<Integer, List<Edge>> SuffixTreeFromSuffixArray(int[] suffixArray, int[] lcpArray, final String text) {
        Map<Integer, List<Edge>> tree = new HashMap<>();
        List<TreeNode> nodeTree = new ArrayList<>();
        TreeNode root = new TreeNode(nodeTree.size(), -1, new TreeMap<>(), 0, -1, -1);
        nodeTree.add(root);
        int lcpPrev = 0;
        TreeNode currentNode = root;

        for (int i = 0; i < text.length(); i++) {
            int suffix = suffixArray[i];
            while (currentNode.depth > lcpPrev) {
                currentNode = nodeTree.get(currentNode.parent);
            }
            if (currentNode.depth == lcpPrev) {
                createNewLeaf(nodeTree, currentNode, text, suffix);
            } else {
                int edgeStart = suffixArray[i - 1] + currentNode.depth;
                int offset = lcpPrev - currentNode.depth;
                TreeNode midNode = breakEdge(nodeTree, currentNode, text, edgeStart, offset);
                createNewLeaf(nodeTree, midNode, text, suffix);
                currentNode = midNode;
            }

            if (i < text.length() - 1) {
                lcpPrev = lcpArray[i];
            }
        }
        for (int i = 0; i < nodeTree.size(); i++) {
            TreeNode current = nodeTree.get(i);
            if (!current.children.isEmpty()) {
                List<Edge> neighbours = new ArrayList<>();
                for (Character c : current.children.keySet()) {
                    TreeNode child = nodeTree.get(current.children.get(c));
                    neighbours.add(new Edge(child.id, child.start, child.end + 1));
                }
                tree.put(current.id, neighbours);
            }
        }

        return tree;
    }


    static public void main(String[] args) throws IOException {
        new SuffixTreeFromArray().run();
    }

    public void print(ArrayList<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffixArray = new int[text.length()];
        for (int i = 0; i < suffixArray.length; ++i) {
            suffixArray[i] = scanner.nextInt();
        }
        int[] lcpArray = new int[text.length() - 1];
        for (int i = 0; i + 1 < text.length(); ++i) {
            lcpArray[i] = scanner.nextInt();
        }
        System.out.println(text);
        // Build the suffix tree and get a mapping from 
        // suffix tree node ID to the list of outgoing Edges.
        Map<Integer, List<Edge>> suffixTree = SuffixTreeFromSuffixArray(suffixArray, lcpArray, text);
        ArrayList<String> result = new ArrayList<String>();
        // Output the edges of the suffix tree in the required order.
        // Note that we use here the contract that the root of the tree
        // will have node ID = 0 and that each vector of outgoing edges
        // will be sorted by the first character of the corresponding edge label.
        //
        // The following code avoids recursion to avoid stack overflow issues.
        // It uses two stacks to convert recursive function to a while loop.
        // This code is an equivalent of 
        //
        //    OutputEdges(tree, 0);
        //
        // for the following _recursive_ function OutputEdges:
        //
        // public void OutputEdges(Map<Integer, List<Edge>> tree, int nodeId) {
        //     List<Edge> edges = tree.get(nodeId);
        //     for (Edge edge : edges) {
        //         System.out.println(edge.start + " " + edge.end);
        //         OutputEdges(tree, edge.node);
        //     }
        // }
        //
        int[] nodeStack = new int[text.length()];
        int[] edgeIndexStack = new int[text.length()];
        nodeStack[0] = 0;
        edgeIndexStack[0] = 0;
        int stackSize = 1;
        while (stackSize > 0) {
            int node = nodeStack[stackSize - 1];
            int edgeIndex = edgeIndexStack[stackSize - 1];
            stackSize -= 1;
            if (suffixTree.get(node) == null) {
                continue;
            }
            if (edgeIndex + 1 < suffixTree.get(node).size()) {
                nodeStack[stackSize] = node;
                edgeIndexStack[stackSize] = edgeIndex + 1;
                stackSize += 1;
            }
            result.add(suffixTree.get(node).get(edgeIndex).start + " " + suffixTree.get(node).get(edgeIndex).end);
            nodeStack[stackSize] = suffixTree.get(node).get(edgeIndex).node;
            edgeIndexStack[stackSize] = 0;
            stackSize += 1;
        }
        print(result);
    }
}
