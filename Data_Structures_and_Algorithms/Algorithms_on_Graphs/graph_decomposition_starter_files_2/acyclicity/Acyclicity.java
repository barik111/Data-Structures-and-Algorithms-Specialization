public class Acyclicity {
    private static Queue<Integer> queue = new LinkedList<>();

    private static int acyclic(ArrayList<Integer>[] adj) {

        for (int i = 0; i < adj.length; i++) {
            queue.add(i);
        }
        boolean[] visited = new boolean[adj.length];
        int result = 0;
        List<Integer> deleted = new ArrayList<>();
        while (!queue.isEmpty()) {
            result = Math.max(result, findCircle(adj, queue.poll(), visited, deleted));
        }
        return result;
    }

    private static int findCircle(ArrayList<Integer>[] adj, Integer currentVertex, boolean[] visited, List<Integer> deleted) {
        visited[currentVertex] = true;
        List<Integer> neighbours = adj[currentVertex];
        neighbours.removeAll(deleted);
        for (Integer neighbour : neighbours) {
            if (!deleted.contains(neighbour)) {
                if (!visited[neighbour]) {
                    findCircle(adj, neighbour, visited, deleted);
                } else {
                    return 1;
                }
            }
        }
        neighbours.removeAll(deleted);
        if (neighbours.isEmpty()) {
            deleted.add(currentVertex);
            queue.remove(currentVertex);
        }

        return 0;
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
        System.out.println(acyclic(adj));
    }
}

