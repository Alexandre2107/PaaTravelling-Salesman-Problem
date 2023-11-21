import java.util.Arrays;
import java.util.List;

public class CaixeiroViajante {

    static int minCost = Integer.MAX_VALUE;
    static int[] minPath;

    public static void main(String[] args) {
        FileManager fileManager = new FileManager();

        // Lê o arquivo de entrada
        List<String> inputLines = fileManager.stringReader("PaaTravelling-Salesman-Problem\\src\\data\\teste2.txt");

        // Número de vértices
        int n = Integer.parseInt(inputLines.get(0));

        // Inicializa as estruturas de dados
        int vetorSolucao[] = new int[n];
        boolean vetorDisponiveis[] = new boolean[n];
        Arrays.fill(vetorDisponiveis, true);

        vetorDisponiveis[0] = false;
        vetorSolucao[0] = 0;
        int[][] grafo = new int[n][n];

        for (int i = 1; i < inputLines.size(); i++) {
            String[] tokens = inputLines.get(i).split("\\s+");
            int origem = Integer.parseInt(tokens[0]);
            for (int j = 1; j < tokens.length; j++) {
                String[] aresta = tokens[j].split("-");
                int destino = Integer.parseInt(aresta[0]);
                int peso = Integer.parseInt(aresta[1].replace(";", ""));
                if (destino < grafo.length) {
                    grafo[origem][destino] = peso;
                }
            }
        }
        int[] path = new int[n];
        boolean[] visited = new boolean[n];
        path[0] = 0;
        visited[0] = true;
        backtracking(1, n, grafo, path, visited, 0);

        System.out.println("Tamanho do Grafo: "+ grafo.length);
        System.out.println(minCost);
        System.out.println(Arrays.toString(minPath));
    }

    public static void backtracking(int pos, int n, int[][] graph, int[] path, boolean[] visited, int currentCost) {
        if (pos == n) {
            int cost = currentCost + graph[path[n - 1]][path[0]];
            if (cost < minCost) {
                minCost = cost;
                minPath = Arrays.copyOf(path, path.length);
            }
        } else {
            for (int i = 0; i < n; i++) {
                if (!visited[i] && graph[path[pos - 1]][i] != 0) {
                    path[pos] = i;
                    visited[i] = true;
                    int newCost = currentCost + graph[path[pos - 1]][i];
                    if (newCost < minCost) {
                        backtracking(pos + 1, n, graph, path, visited, newCost);
                    }
                    visited[i] = false;
                }
            }
        }
    }

    public static int calculateCost(int[] path, int n, int[][] graph) {
        int cost = 0;
        for (int i = 0; i < n - 1; i++) {
            cost += graph[path[i]][path[i + 1]];
        }
        cost += graph[path[n - 1]][path[0]];
        return cost;
    }
}