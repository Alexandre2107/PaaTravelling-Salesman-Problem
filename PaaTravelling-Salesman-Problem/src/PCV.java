import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Grafo {
    private int numVertices;
    private int[][] matrizAdj;

    public Grafo(int numVertices) {
        this.numVertices = numVertices;
        this.matrizAdj = new int[numVertices][numVertices];
    }

    public void adicionarAresta(int origem, int destino, int peso) {
        matrizAdj[origem][destino] = peso;
        matrizAdj[destino][origem] = peso; // Para um grafo não direcionado
    }

    public int[][] getMatrizAdj() {
        return matrizAdj;
    }

    public int getNumVertices() {
        return numVertices;
    }
}

public class PCV {
    public static void main(String[] args) {
        try {
            Grafo grafo = lerGrafo("D:\\PaaProj3\\PaaTravelling-Salesman-Problem\\src\\data\\Teste.txt");

            // Algoritmo Ótimo (Tentativa e Erro)
            PCVAlgoritmos.Resultado resultadoTentativaErro = PCVAlgoritmos.tentativaErro(grafo);
            System.out.println("Algoritmo Ótimo (Tentativa e Erro):");
            System.out.println("Custo mínimo: " + resultadoTentativaErro.custo);
            System.out.println("Caminho: " + resultadoTentativaErro.caminho);

            // Heurística (Algoritmo Genético)
            PCVAlgoritmos.Resultado resultadoGenetico = PCVAlgoritmos.algoritmoGenetico(grafo, 1000, 50);
            System.out.println("\nHeurística (Algoritmo Genético):");
            System.out.println("Custo mínimo: " + resultadoGenetico.custo);
            System.out.println("Caminho: " + resultadoGenetico.caminho);
        } catch (IOException e) {
            System.err.println("Erro na leitura do arquivo: " + e.getMessage());
        }
    }

    private static Grafo lerGrafo(String nomeArquivo) throws IOException {
        BufferedReader leitor = new BufferedReader(new FileReader(nomeArquivo));
        int numVertices = Integer.parseInt(leitor.readLine());
        Grafo grafo = new Grafo(numVertices);

        String linha;
        while ((linha = leitor.readLine()) != null) {
            String[] partes = linha.split(" ");
            int origem = Integer.parseInt(partes[0]);
            for (int i = 1; i < partes.length; i++) {
                String[] arestaInfo = partes[i].split("-");
                int destino = Integer.parseInt(arestaInfo[0]);
                int peso = Integer.parseInt(arestaInfo[1].split(";")[0]);
                grafo.adicionarAresta(origem, destino, peso);
            }
        }

        leitor.close();
        return grafo;
    }

}

