import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class PCVAlgoritmos {
    static class Resultado {
        int custo;
        List<Integer> caminho;

        public Resultado(int custo, List<Integer> caminho) {
            this.custo = custo;
            this.caminho = caminho;
        }
    }

    // Algoritmo Ótimo (Tentativa e Erro)
    public static Resultado tentativaErro(Grafo grafo) {
        int numVertices = grafo.getNumVertices();
        int[] vertices = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            vertices[i] = i;
        }

        List<List<Integer>> permutacoes = permutacoes(vertices);

        int menorCusto = Integer.MAX_VALUE;
        List<Integer> melhorCaminho = new ArrayList<>();

        for (List<Integer> permutacao : permutacoes) {
            int custoAtual = calcularCusto(grafo, permutacao);
            if (custoAtual < menorCusto) {
                menorCusto = custoAtual;
                melhorCaminho = new ArrayList<>(permutacao);
            }
        }

        return new Resultado(menorCusto, melhorCaminho);
    }

    // Algoritmo Genético
    public static Resultado algoritmoGenetico(Grafo grafo, int geracoes, int tamanhoPopulacao) {
        int numVertices = grafo.getNumVertices();
        List<Integer> melhorCaminho = new ArrayList<>();
        int menorCusto = Integer.MAX_VALUE;

        // Geração inicial aleatória
        for (int i = 0; i < tamanhoPopulacao; i++) {
            List<Integer> caminhoAleatorio = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4)); // Adapte conforme necessário
            Collections.shuffle(caminhoAleatorio);
            int custoAtual = calcularCusto(grafo, caminhoAleatorio);

            if (custoAtual < menorCusto) {
                menorCusto = custoAtual;
                melhorCaminho = new ArrayList<>(caminhoAleatorio);
            }
        }

        for (int geracao = 0; geracao < geracoes; geracao++) {
            // Implemente a lógica de cruzamento e mutação aqui
        }

        return new Resultado(menorCusto, melhorCaminho);
    }

    private static int calcularCusto(Grafo grafo, List<Integer> caminho) {
        int custo = 0;
        for (int i = 0; i < caminho.size() - 1; i++) {
            custo += grafo.getMatrizAdj()[caminho.get(i)][caminho.get(i + 1)];
        }
        custo += grafo.getMatrizAdj()[caminho.get(caminho.size() - 1)][caminho.get(0)]; // Voltar à origem
        return custo;
    }

    private static List<List<Integer>> permutacoes(int[] elementos) {
        List<List<Integer>> permutacoes = new ArrayList<>();
        permutacoesRecursivo(elementos, 0, permutacoes);
        return permutacoes;
    }

    private static void permutacoesRecursivo(int[] elementos, int indice, List<List<Integer>> permutacoes) {
        if (indice == elementos.length - 1) {
            List<Integer> permutacao = new ArrayList<>();
            for (int elemento : elementos) {
                permutacao.add(elemento);
            }
            permutacoes.add(permutacao);
        } else {
            for (int i = indice; i < elementos.length; i++) {
                trocarElementos(elementos, indice, i);
                permutacoesRecursivo(elementos, indice + 1, permutacoes);
                trocarElementos(elementos, indice, i); // Desfaz a troca para explorar outras opções
            }
        }
    }

    private static void trocarElementos(int[] elementos, int i, int j) {
        int temp = elementos[i];
        elementos[i] = elementos[j];
        elementos[j] = temp;
    }
}
