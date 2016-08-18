public class Toposort {
    private static List<Integer> toposort(ArrayList<Integer>[] adj) {
        boolean[] visited = new boolean[adj.length];
        List<Integer> used = new LinkedList<>();
        for (int i = 0; i < adj.length; i++) {
            if (!visited[i])
                dfs(adj, i, visited, used);
        }
        return used;
    }

    private static void dfs(ArrayList<Integer>[] adj, Integer currentVertex, boolean[] visited, List<Integer> used) {
        visited[currentVertex] = true;
        List<Integer> neighbours = adj[currentVertex];
        for (Integer neighbour : neighbours) {
            if (!visited[neighbour]) {
                dfs(adj, neighbour, visited, used);
            }
        }
        used.add(0, currentVertex);
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
        List<Integer> order = toposort(adj);
        for (int x : order) {
            System.out.print((x + 1) + " ");
        }
    }
}

