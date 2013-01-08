package de.tuhh.ict.pshdl.model.utils;

import java.util.*;

public class Graph<T> {

	public static class Node<T> {
		public final T object;
		public final HashSet<Edge<T>> inEdges;
		public final HashSet<Edge<T>> outEdges;

		public Node(T object) {
			this.object = object;
			inEdges = new HashSet<Edge<T>>();
			outEdges = new HashSet<Edge<T>>();
		}

		public Node<T> addEdge(Node<T> node) {
			Edge<T> e = new Edge<T>(this, node);
			outEdges.add(e);
			node.inEdges.add(e);
			return this;
		}

		@Override
		public String toString() {
			return object.toString();
		}
	}

	public static class Edge<T> {
		public final Node<T> from;
		public final Node<T> to;

		public Edge(Node<T> from, Node<T> to) {
			this.from = from;
			this.to = to;
		}

		@Override
		public boolean equals(Object obj) {
			Edge<?> e = (Edge<?>) obj;
			return (e.from == from) && (e.to == to);
		}
	}

	public Node<T> createNode(T object) {
		return new Node<T>(object);
	}

	public static void main(String[] args) {
		Graph<String> graph = new Graph<String>();
		Node<String> seven = graph.createNode("7");
		Node<String> five = graph.createNode("5");
		Node<String> three = graph.createNode("3");
		Node<String> eleven = graph.createNode("11");
		Node<String> eight = graph.createNode("8");
		Node<String> two = graph.createNode("2");
		Node<String> nine = graph.createNode("9");
		Node<String> ten = graph.createNode("10");
		seven.addEdge(eleven).addEdge(eight);
		five.addEdge(eleven);
		three.addEdge(eight).addEdge(ten);
		eleven.addEdge(two).addEdge(nine).addEdge(ten);
		eight.addEdge(nine).addEdge(ten);

		List<Node<String>> allNodes = Arrays.asList(seven, five, three, eleven, eight, two, nine, ten);
		// L <- Empty list that will contain the sorted elements
		ArrayList<Node<String>> L = new ArrayList<Node<String>>();

		// S <- Set of all nodes with no incoming edges
		HashSet<Node<String>> S = new HashSet<Node<String>>();
		for (Node<String> n : allNodes) {
			if (n.inEdges.size() == 0) {
				S.add(n);
			}
		}

		// while S is non-empty do
		while (!S.isEmpty()) {
			// remove a node n from S
			Node<String> n = S.iterator().next();
			S.remove(n);

			// insert n into L
			L.add(n);

			// for each node m with an edge e from n to m do
			for (Iterator<Edge<String>> it = n.outEdges.iterator(); it.hasNext();) {
				// remove edge e from the graph
				Edge<String> e = it.next();
				Node<String> m = e.to;
				it.remove();// Remove edge from n
				m.inEdges.remove(e);// Remove edge from m

				// if m has no other incoming edges then insert m into S
				if (m.inEdges.isEmpty()) {
					S.add(m);
				}
			}
		}
		// Check to see if all edges are removed
		boolean cycle = false;
		for (Node<String> n : allNodes) {
			if (!n.inEdges.isEmpty()) {
				cycle = true;
				break;
			}
		}
		if (cycle) {
			System.out.println("Cycle present, topological sort not possible");
		} else {
			System.out.println("Topological Sort: " + Arrays.toString(L.toArray()));
		}
	}
}