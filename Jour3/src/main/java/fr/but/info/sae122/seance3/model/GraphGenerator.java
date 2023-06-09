package fr.but.info.sae122.seance3.model;

import java.util.Random;

/** Tool class to build standard graphs */
public class GraphGenerator {
	/**
	 * Builds a linear graph with the requested number of nodes. Each node is linked
	 * to the next one but the last.
	 * 
	 * @param nbNodes the number of nodes.
	 * @return the new graph
	 * @throws IllegalArgumentException if nbNodes is not at least 1
	 */

	static Random r = new Random();

	public static Graph createLinear(int nbNodes) {
		if (nbNodes < 1)
			throw new IllegalArgumentException("A graph should have at least one node!");
		Graph graph = new Graph();
		graph.addNode("N0");
		for (int i = 1; i < nbNodes; ++i) {
			int randomCapacity = r.nextInt(10);
			graph.addNode("N" + i);
			graph.addEdge("N" + (i - 1), "N" + i, randomCapacity);
		}
		return graph;
	}

	/**
	 * Builds a circular graph with the requested number of nodes. Each node is
	 * linked to the next one and the last to the first.
	 * 
	 * @param nbNodes the number of nodes.
	 * @return the new graph
	 * @throws IllegalArgumentException if nbNodes is not at least 1
	 */
	public static Graph createCircular(int nbNodes) {
		Graph graph = createLinear(nbNodes);
		int randomCapacity = r.nextInt(10);
		graph.addEdge("N" + (nbNodes - 1), "N0", randomCapacity);
		return graph;
	}

	/**
	 * Builds a triangular graph with the requested number of nodes. Each node is
	 * linked to all the following nodes.
	 * 
	 * @param nbNodes the number of nodes.
	 * @return the new graph
	 */
	public static Graph createTriangular(int nbNodes) {
		Graph graph = new Graph();
		for (int i = 0; i < nbNodes; ++i) {
			graph.addNode("N" + i);
			for (int j = 0; j < i; j++) {
				int randomCapacity = r.nextInt(10);
				graph.addEdge("N" + j, "N" + i, randomCapacity);
			}
		}
		return graph;
	}

	/**
	 * Builds a fully-connected graph with the requested number of nodes. Each node
	 * is linked to all the other nodes.
	 * 
	 * @param nbNodes the number of nodes.
	 * @return the new graph
	 */
	public static Graph createFull(int nbNodes) {
		Graph graph = new Graph();
		for (int i = 0; i < nbNodes; ++i) {
			graph.addNode("N" + i);
		}
		for (int i = 0; i < nbNodes; ++i)
			for (int j = 0; j < nbNodes; j++)
				if (i != j) {
					int randomCapacity = r.nextInt(10);
					graph.addEdge("N" + j, "N" + i, randomCapacity);
				}
		return graph;
	}

	/**
	 * Builds a fully-connected graph with the requested number of nodes. Each node
	 * is linked to all the other nodes with a given probability.
	 * 
	 * @param nbNodes         the number of nodes.
	 * @param edgeProbability the probability of each edge to be present.
	 * @return the new graph
	 */
	public static Graph createRandom(int nbNodes, double edgeProbability) {
		Graph graph = new Graph();
		for (int i = 0; i < nbNodes; ++i) {
			graph.addNode("N" + i);
		}
		for (int i = 0; i < nbNodes; ++i)
			for (int j = 0; j < nbNodes; j++)
				if (i != j && Math.random() < edgeProbability) {
					int randomCapacity = r.nextInt(10);
					graph.addEdge("N" + j, "N" + i, randomCapacity);
				}
		return graph;
	}
}
