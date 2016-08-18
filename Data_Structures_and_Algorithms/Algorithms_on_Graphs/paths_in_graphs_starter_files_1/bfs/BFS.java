public class BFS {
    private static int distance(ArrayList<Integer>[] adj, int s, int t) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        int[] level = new int[adj.length];
        for (int i = 0; i<level.length; i++) {
            level[i] = Integer.MAX_VALUE;
        }
        level[s] = 0;
        while (!queue.isEmpty()) {
            Integer currentVertex = queue.poll();
            List<Integer> neighbours = adj[currentVertex];
            for (Integer neighbour : neighbours) {
                if(level[neighbour] == Integer.MAX_VALUE){
                    queue.add(neighbour);
                    level[neighbour] = level[currentVertex] + 1;
                }
            }
        }
        return level[t] == Integer.MAX_VALUE?-1:level[t];
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
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(distance(adj, x, y));
    }
}

