public class StronglyConnected {

    private static int numberOfStronglyConnectedComponents(ArrayList<Integer>[] adj) {
        boolean[] visited = new boolean[adj.length];
        List<Integer>[] reversAdj = reversAdjList(adj);
        List<Integer> postOrder = new ArrayList<>();
        int count = 0;
        Queue<Integer> queue;

        for (int i = 0; i < adj.length; i++) {
            dfs(reversAdj, visited, postOrder, i);
        }

        Collections.reverse(postOrder);
        queue = new LinkedList<>(postOrder);
        visited = new boolean[adj.length];

        while (!queue.isEmpty()) {
            int j = queue.poll();
            if (!visited[j]) {
                explore(adj, j, visited);
                count++;
            }
        }
        return count;
    }

    private static void explore(List<Integer>[] adj, int i, boolean[] visited) {
        List<Integer> neighbours = adj[i];
        visited[i] = true;

        for (Integer neighbour : neighbours) {
            if (!visited[neighbour]) {
                explore(adj, neighbour, visited);
            }
        }
    }

    private static void dfs(List<Integer>[] adj, boolean[] visited, List<Integer> postOrder, int i) {
        if (!visited[i]) {
            List<Integer> neighbours = adj[i];
            visited[i] = true;
            for (Integer neighbour : neighbours) {
                dfs(adj, visited, postOrder, neighbour);
            }
            postOrder.add(i);
        }

    }

    public static List<Integer>[] reversAdjList(List<Integer>[] adj) {
        ArrayList<Integer>[] reversAdj = (ArrayList<Integer>[]) new ArrayList[adj.length];
        for (int i = 0; i < adj.length; i++) {
            reversAdj[i] = new ArrayList<>();
        }
        for (int i = 0; i < adj.length; i++) {
            List<Integer> neighbour = adj[i];
            for (int j = 0; j < neighbour.size(); j++) {
                reversAdj[neighbour.get(j)].add(i);
            }
        }

        return reversAdj;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
        }
        System.out.println(numberOfStronglyConnectedComponents(adj));
    }
}

