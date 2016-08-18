public class Bipartite {
    private static int bipartite(ArrayList<Integer>[] adj) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(0);
        int[] color = new int[adj.length];
        color[0] = 1;
        while (!queue.isEmpty()) {
            Integer currentVertex = queue.poll();
            List<Integer> neighbours = adj[currentVertex];
            for (Integer neighbour : neighbours) {
                if (color[neighbour] == 0) {
                    queue.add(neighbour);
                    color[neighbour] = color[currentVertex] == 1 ? 2 : 1;
                } else {
                    int expectedColor = color[currentVertex] == 1 ? 2 : 1;
                    if(color[neighbour] != expectedColor) return 0;
                }
            }
        }
        return 1;
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
            adj[y - 1].add(x - 1);
        }
        System.out.println(bipartite(adj));
    }
}

