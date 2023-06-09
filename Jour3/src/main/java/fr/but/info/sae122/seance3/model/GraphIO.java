package fr.but.info.sae122.seance3.model;

import java.io.*;
import java.util.Scanner;

/** Tool class for reading and writing graphs to/from disk. */
public class GraphIO {
	/**
	 * Reads a graph from the disk.
	 * 
	 * @param from the stream to read from
	 * @return the read graph
	 * @throws IOException if something goes wrong with I/O
	 */
	public static Graph read(InputStream from) throws IOException {
		var graph = new Graph();
		BufferedReader reader = new BufferedReader(new InputStreamReader(from));
		String line;
		do {
			line = reader.readLine();
		} while (line.isBlank());
		readNodes(graph, line);
		while (line != null) {
			do {
				line = reader.readLine();
			} while (line != null && line.isBlank());
			if (line != null)
				readEdges(graph, line);
		}
		return graph;
	}

	/**
	 * Parse a (first) line from the file and adds to the graph.
	 * 
	 * @param graph the graph to add nodes to
	 * @param line  the (nodes) line to parse
	 */
	private static void readNodes(Graph graph, String line) {
		var lineScanner = new Scanner(line);
		lineScanner.useDelimiter(" *; *");
		while (lineScanner.hasNext()) {
			graph.addNode(lineScanner.next());
		}
	}

	/**
	 * Parse a (not first) line from the file and adds to the graph.
	 * 
	 * @param graph the graph to add edges to
	 * @param line  the (edges) line to parse
	 */
	private static void readEdges(Graph graph, String line) {
		var lineScanner = new Scanner(line);
		lineScanner.useDelimiter(" *; *");
		var from = lineScanner.next();
		while (lineScanner.hasNext()) {
			var to = lineScanner.next();
			var flow = lineScanner.nextInt();
			var capacity = lineScanner.nextInt();
			graph.addEdge(from, to, capacity).setFlow(flow);
		}
	}

	/**
	 * Writes a graph to the disk.
	 * 
	 * @param graph the graph to write
	 * @param to    the stream to write to
	 */
	public static void write(Graph graph, OutputStream to) {
		try (PrintWriter writer = new PrintWriter(to)) {
			writer.println(String.join(";", graph.getNodes()));
			for (String node : graph.getNodes()) {
				writer.printf("%s", node);
				for (Edge e : graph.getEdgesFrom(node)) {
					writer.printf(";%s;%d;%d", e.getToNode(), e.getFlow(), e.getCapacity());
				}
				writer.println();
			}
		}
	}

}
