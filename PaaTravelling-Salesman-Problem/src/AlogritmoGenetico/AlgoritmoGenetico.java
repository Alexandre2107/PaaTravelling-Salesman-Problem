package AlogritmoGenetico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AlgoritmoGenetico {
    private static final int POPULATION_SIZE = 1000;
    private static final double MUTATION_RATE = 0.1;
    private static final int NUM_GENERATIONS = 2000;

    private static int n;
    private static int[][] graph;

    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        List<String> inputLines = fileManager.stringReader("PaaTravelling-Salesman-Problem\\src\\data\\teste3.txt");
        n = Integer.parseInt(inputLines.get(0));
        int vetorSolucao[] = new int[n];
        boolean vetorDisponiveis[] = new boolean[n];
        Arrays.fill(vetorDisponiveis, true);

        vetorDisponiveis[0] = false;
        vetorSolucao[0] = 0;
        graph = new int[n][n];

        for (int i = 1; i < inputLines.size(); i++) {
            String[] tokens = inputLines.get(i).split("\\s+");
            int origem = Integer.parseInt(tokens[0]);
            for (int j = 1; j < tokens.length; j++) {
                String[] aresta = tokens[j].split("-");
                int destino = Integer.parseInt(aresta[0]);
                int peso = Integer.parseInt(aresta[1].replace(";", ""));
                if (destino < n) {
                    graph[origem][destino] = peso;
                }
            }
        }
        ArrayList<ArrayList<Integer>> population = initializePopulation();

        for (int generation = 0; generation < NUM_GENERATIONS; generation++) {
            evaluatePopulation(population);
            ArrayList<ArrayList<Integer>> parents = selectParents(population);
            ArrayList<ArrayList<Integer>> offspring = recombine(parents);
            mutate(offspring);
            evaluatePopulation(offspring);
            population = selectNextGeneration(population, offspring);
        }
        ArrayList<Integer> bestSolution = findBestSolution(population);
        int bestCost = evaluateSolution(bestSolution);

        System.out.println("Custo: " + bestCost);
        System.out.println("Caminho: " + bestSolution);
    }

    private static ArrayList<ArrayList<Integer>> initializePopulation() {
        ArrayList<ArrayList<Integer>> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            ArrayList<Integer> candidate = generateRandomPermutation();
            population.add(candidate);
        }
        return population;
    }

    private static ArrayList<Integer> generateRandomPermutation() {
        ArrayList<Integer> permutation = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            permutation.add(i);
        }
        Collections.shuffle(permutation);
        return permutation;
    }

    private static void evaluatePopulation(ArrayList<ArrayList<Integer>> population) {
        for (ArrayList<Integer> candidate : population) {
            int cost = evaluateSolution(candidate);
        }
    }

    private static int evaluateSolution(ArrayList<Integer> solution) {
        int cost = 0;
        for (int i = 0; i < solution.size() - 1; i++) {
            cost += graph[solution.get(i)][solution.get(i + 1)];
        }
        cost += graph[solution.get(solution.size() - 1)][solution.get(0)];
        return cost;
    }

    private static ArrayList<ArrayList<Integer>> selectParents(ArrayList<ArrayList<Integer>> population) {
        ArrayList<ArrayList<Integer>> parents = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < population.size(); i++) {
            int idx1 = random.nextInt(population.size());
            int idx2 = random.nextInt(population.size());

            ArrayList<Integer> parent1 = population.get(idx1);
            ArrayList<Integer> parent2 = population.get(idx2);
            if (evaluateSolution(parent1) < evaluateSolution(parent2)) {
                parents.add(parent1);
            } else {
                parents.add(parent2);
            }
        }
        return parents;
    }

    private static ArrayList<ArrayList<Integer>> recombine(ArrayList<ArrayList<Integer>> parents) {
        ArrayList<ArrayList<Integer>> offspring = new ArrayList<>();

        for (int i = 0; i < parents.size(); i += 2) {
            ArrayList<Integer> parent1 = parents.get(i);
            ArrayList<Integer> parent2 = parents.get(i + 1);

            int cuttingPoint = n / 2;

            ArrayList<Integer> child1 = new ArrayList<>(parent1.subList(0, cuttingPoint));
            ArrayList<Integer> child2 = new ArrayList<>(parent2.subList(0, cuttingPoint));

            for (int j = 0; j < parent2.size(); j++) {
                if (!child1.contains(parent2.get(j))) {
                    child1.add(parent2.get(j));
                }
                if (!child2.contains(parent1.get(j))) {
                    child2.add(parent1.get(j));
                }
            }

            offspring.add(child1);
            offspring.add(child2);
        }

        return offspring;
    }

    private static void mutate(ArrayList<ArrayList<Integer>> offspring) {
        Random random = new Random();
        for (ArrayList<Integer> candidate : offspring) {
            if (random.nextDouble() < MUTATION_RATE) {
                int mutationType = random.nextInt(3);
                switch (mutationType) {
                    case 0:
                        mutateInsertion(candidate);
                        System.out.println("Mutação por Inserção");
                        break;
                    case 1:
                        mutateSwap(candidate);
                        System.out.println("Mutação por Swap");
                        break;
                    case 2:
                        mutateInversion(candidate);
                        System.out.println("Mutação por Inversão");
                        break;
                }
            }
        }
    }

    private static void mutateInsertion(ArrayList<Integer> candidate) {
        Random random = new Random();
        int index1 = random.nextInt(candidate.size());
        int index2;
        do {
            index2 = random.nextInt(candidate.size());
        } while (index1 == index2);

        int city = candidate.remove(index1);
        candidate.add(index2, city);
    }

    private static void mutateSwap(ArrayList<Integer> candidate) {
        Random random = new Random();
        int index1 = random.nextInt(candidate.size());
        int index2;
        do {
            index2 = random.nextInt(candidate.size());
        } while (index1 == index2);

        Collections.swap(candidate, index1, index2);
    }

    private static void mutateInversion(ArrayList<Integer> candidate) {
        Random random = new Random();
        int index1 = random.nextInt(candidate.size());
        int index2;
        do {
            index2 = random.nextInt(candidate.size());
        } while (index1 == index2);

        if (index1 > index2) {
            int temp = index1;
            index1 = index2;
            index2 = temp;
        }
        Collections.reverse(candidate.subList(index1, index2 + 1));
    }

    private static ArrayList<ArrayList<Integer>> selectNextGeneration(
        ArrayList<ArrayList<Integer>> population, ArrayList<ArrayList<Integer>> offspring) {
        population.addAll(offspring);
        Collections.sort(population, (a, b) -> Integer.compare(evaluateSolution(a), evaluateSolution(b)));
        return new ArrayList<>(population.subList(0, POPULATION_SIZE));
    }

    private static ArrayList<Integer> findBestSolution(ArrayList<ArrayList<Integer>> population) {
        int bestCost = Integer.MAX_VALUE;
        ArrayList<Integer> bestSolution = null;

        for (ArrayList<Integer> solution : population) {
            int cost = evaluateSolution(solution);
            if (cost < bestCost) {
                bestCost = cost;
                bestSolution = new ArrayList<>(solution);
            }
        }
        return bestSolution;
    }
}
