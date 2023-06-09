package fr.but.info.sae122.seance3.model;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class MaxFlowWithoutResidualGraph extends MaxFlow {

	public MaxFlowWithoutResidualGraph(Graph graph, String source, String sink) {
		super(graph, source, sink);
	}

	@Override
	public int computeMaxFlow() {
		try {
			while (true) {
				Path path = getAugmentingPath();
				increaseFlow(path);
			}
		} catch (NoSuchElementException ex) {
			// no more augmenting path, flow is maximal
			return getSourceFlow();
		}
	}

	public Path getAugmentingPath() {
		HashSet<String> visited = new HashSet<>();
		Path res = new Path();
		ArrayDeque<PathElement> queue = new ArrayDeque<>();
		var graph = getGraph();
		var sink = getSink();
		String source = getSource();
		for (Edge edge : graph.getEdgesFrom(source)) {
			if (edge.getFlow() < edge.getCapacity())
				queue.add(new PathElement(null, edge));
		}
		for (Edge edge : graph.getEdgesTo(source)) {
			if (edge.getFlow() > 0)
				queue.add(new PathElement(null, new Edge(edge.getToNode(), edge.getFromNode(), edge.getFlow())));
		}
		visited.add(source);
		while (!queue.isEmpty()) {
			PathElement current = queue.removeFirst();
			String toNode = current.getEdge().getToNode();
			if (visited.contains(toNode))
				continue;
			else
				visited.add(toNode);
			if (toNode.equals(sink)) {
				res.setFlow(current.getMaxFlow());
				while (current != null) {
					res.addFirstEdge(current.getEdge());
					current = current.getParent();
				}
				return res;
			}
			for (Edge edge : graph.getEdgesFrom(toNode)) {
				if (edge.getFlow() < edge.getCapacity())
					queue.add(new PathElement(current, edge));
			}
			for (Edge edge : graph.getEdgesTo(toNode)) {
				if (edge.getFlow() > 0)
					queue.add(new PathElement(current, new Edge(edge.getToNode(), edge.getFromNode(), edge.getFlow())));
			}
		} // while ! queue.isEmpty()
		throw new NoSuchElementException("No more augmenting path");
	}
}
