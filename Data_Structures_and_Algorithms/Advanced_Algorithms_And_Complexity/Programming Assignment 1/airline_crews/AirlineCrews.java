import java.io.*;
import java.util.*;

public class AirlineCrews {
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new AirlineCrews().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        boolean[][] bipartiteGraph = readData();
        int[] matching = findMatching(bipartiteGraph);
        writeResponse(matching);
        out.close();
    }

    boolean[][] readData() throws IOException {
        int numLeft = in.nextInt();
        int numRight = in.nextInt();
        boolean[][] adjMatrix = new boolean[numLeft][numRight];
        for (int i = 0; i < numLeft; ++i)
            for (int j = 0; j < numRight; ++j)
                adjMatrix[i][j] = (in.nextInt() == 1);
        return adjMatrix;
    }

    private int[] findMatching(boolean[][] bipartiteGraph) {
        // Replace this code with an algorithm that finds the maximum
        // matching correctly in all cases.
        int numLeft = bipartiteGraph.length;
        int numRight = bipartiteGraph[0].length;

        int[] matchingLeft = new int[numLeft];
        int[] matchingRight = new int[numRight];
        Arrays.fill(matchingLeft, -1);
        Arrays.fill(matchingRight, -1);

        for (int left = 0; left < numLeft; left++)
            dfs(left, new boolean[numLeft], matchingLeft, matchingRight, bipartiteGraph);

        return matchingLeft;
    }

    static boolean dfs(int left, boolean[] visited, int[] matchingLeft, int[] matchingRight, boolean[][] graph) {
        if (left == -1) {
            return true;
        }
        if (visited[left]) {
            return false;
        }
        visited[left] = true;
        for (int right = 0; right < matchingRight.length; ++right) {
            if (graph[left][right] && dfs(matchingRight[right], visited, matchingLeft, matchingRight, graph)) {
                matchingLeft[left] = right;
                matchingRight[right] = left;
                return true;
            }
        }
        return false;
    }


    private void writeResponse(int[] matching) {
        for (int i = 0; i < matching.length; ++i) {
            if (i > 0) {
                out.print(" ");
            }
            if (matching[i] == -1) {
                out.print("-1");
            } else {
                out.print(matching[i] + 1);
            }
        }
        out.println();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}



