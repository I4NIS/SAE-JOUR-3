package fr.but.info.sae122.seance3.model;

import java.util.NoSuchElementException;

public class AugmentingPath extends Path {
	private final Graph residual;

	public AugmentingPath(Graph graph, String source, String sink) throws NoSuchElementException {
		residual = ResidualGraph.createFromGraph(graph);
		BreadthFirstIterator iterator = new BreadthFirstIterator(residual, source);
		PathElement path = getPath(iterator, sink);
		setFlow(path.getMaxFlow());
		computePath(path);
	}

	public Graph getResidualGraph() {
		return residual;
	}

	private void computePath(PathElement last) {
		PathElement element = last;
		addLastEdge(element.getEdge());
		while (element.getParent() != null) {
			element = element.getParent();
			addFirstEdge(element.getEdge());
		}
	}

	private PathElement getPath(BreadthFirstIterator iterator, String sink) {
		while (iterator.hasNext()) {
			PathElement current = iterator.next();
			if (current.getEdge().getToNode().equals(sink))
				return current;
		}
		throw new NoSuchElementException("No more augmenting path!");
	}
}
