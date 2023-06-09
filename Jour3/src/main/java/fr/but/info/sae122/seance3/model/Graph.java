package fr.but.info.sae122.seance3.model;

import java.util.*;

public class Graph {
	private final HashSet<String> nodes;
	private final HashSet<Edge> edges;

	private final Map<String, List<Edge>> mapFrom;
	private final Map<String, List<Edge>> mapTo;

	public Graph() {
		nodes = new HashSet<>();
		edges = new HashSet<>();
		mapFrom = new HashMap<>();
		mapTo = new HashMap<>();
	}

	public void addNode(String name) {
		if (nodes.contains(name))
			throw new IllegalArgumentException("Node " + name + " already exists !");
		nodes.add(name);
		mapFrom.put(name, new ArrayList<>());
		mapTo.put(name, new ArrayList<>());
	}

	public Edge addEdge(String from, String to, int capacity) {
		if (from.equals(to))
			throw new IllegalArgumentException("Autolink are forbidden !");
		Edge edge = new Edge(from, to, capacity);
		edges.add(edge);
		mapFrom.get(from).add(edge);
		mapTo.get(to).add(edge);
		return edge;
	}

	public Collection<String> getNodes() {
		return Collections.unmodifiableSet(nodes);
	}

	public Collection<Edge> getEdges() {
		return Collections.unmodifiableSet(edges);
	}

	public Collection<Edge> getEdgesFrom(String node) {
		List<Edge> res = mapFrom.get(node);
		if (res != null)
			return Collections.unmodifiableList(res);
		else
			throw new IllegalArgumentException("No such node !");
	}

	public Collection<Edge> getEdgesTo(String node) {
		List<Edge> res = mapTo.get(node);
		if (res != null)
			return Collections.unmodifiableList(res);
		else
			throw new IllegalArgumentException("No such node !");
	}

	public Edge getEdge(String fromNode, String toNode) {
		Collection<Edge> edgesFrom = getEdgesFrom(fromNode);
		Edge res = edgesFrom.stream().filter(e -> e.getToNode().equals(toNode)).findAny().orElse(null);
		if (res != null)
			return res;
		else
			throw new IllegalArgumentException("No such edge !");
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder("Graph:\n");
		for (var node : getNodes()) {
			res.append("- Node ");
			res.append(node);
			res.append(":\n");
			for (var edge : getEdgesFrom(node)) {
				res.append("  > ");
				res.append(edge.toString());
				res.append("\n");
			}
		}
		return res.toString();
	}

	public void removeEdge(String fromNode, String toNode) {
		mapFrom.get(fromNode).stream().filter(e -> e.getToNode().equals(toNode)).findFirst().ifPresent(e -> {
			mapFrom.get(fromNode).remove(e);
			mapTo.get(toNode).remove(e);
			edges.remove(e);
		});
	}
}
