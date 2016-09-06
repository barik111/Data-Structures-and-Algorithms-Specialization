import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class StockCharts {
    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new StockCharts().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        int[][] stockData = readData();
        int result = minCharts(stockData);
        writeResponse(result);
        out.close();
    }

    int[][] readData() throws IOException {
        int numStocks = in.nextInt();
        int numPoints = in.nextInt();
        int[][] stockData = new int[numStocks][numPoints];
        for (int i = 0; i < numStocks; ++i) {
            for (int j = 0; j < numPoints; ++j) {
                stockData[i][j] = in.nextInt();
            }
        }
        return stockData;
    }

    private int minCharts(int[][] stockData) {
        // Replace this incorrect greedy algorithm with an
        // algorithm that correctly finds the minimum number
        // of charts on which we can put all the stock data
        // without intersections of graphs on one chart.

        int numStocks = stockData.length;
        int numPoint = stockData[0].length;
        boolean[][] compareStocks = new boolean[numStocks][numStocks];
        for (int i = 0; i < numStocks; ++i) {
            for (int j = 0; j < numStocks; ++j) {
                if (i == j) continue;
                compareStocks[i][j] = true;
                for (int k = 0; k < numPoint; ++k) {
                    compareStocks[i][j] &= stockData[i][k] < stockData[j][k];
                    if (!compareStocks[i][j]) break;
                }
            }
        }

        int[][] bipartiteMatching = new int[2][numStocks];
        for (int[] arr : bipartiteMatching) {
            Arrays.fill(arr, -1);
        }

        int path = 0;
        for (int i = 0; i < numStocks; ++i)
            if (dfs(i, new boolean[numStocks], bipartiteMatching, compareStocks))
                ++path;

        return numStocks - path;
    }

    private boolean dfs(int i, boolean[] visited, int[][] bipartiteMatching, boolean[][] compareStocks) {
        if (i == -1) {
            return true;
        }
        if (visited[i]) {
            return false;
        }
        visited[i] = true;
        for (int j = 0; j < compareStocks.length; ++j) {
            if (compareStocks[i][j] && dfs(bipartiteMatching[1][j], visited, bipartiteMatching, compareStocks)) {
                bipartiteMatching[0][i] = j;
                bipartiteMatching[1][j] = i;
                return true;
            }
        }
        return false;
    }


    private void writeResponse(int result) {
        out.println(result);
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
