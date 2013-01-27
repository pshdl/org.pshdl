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

		public Node<T> reverseAddEdge(Node<T> node) {
			Edge<T> e = new Edge<T>(node, this);
			node.outEdges.add(e);
			inEdges.add(e);
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
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + ((from == null) ? 0 : from.hashCode());
			result = (prime * result) + ((to == null) ? 0 : to.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Edge<?> other = (Edge<?>) obj;
			if (from == null) {
				if (other.from != null)
					return false;
			} else if (!from.equals(other.from))
				return false;
			if (to == null) {
				if (other.to != null)
					return false;
			} else if (!to.equals(other.to))
				return false;
			return true;
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
		graph.sortNodes(allNodes);
	}

	public ArrayList<Node<T>> sortNodes(List<Node<T>> allNodes) {
		// L <- Empty list that will contain the sorted elements
		ArrayList<Node<T>> L = new ArrayList<Node<T>>();

		// S <- Set of all nodes with no incoming edges
		HashSet<Node<T>> S = new HashSet<Node<T>>();
		for (Node<T> n : allNodes) {
			if (n.inEdges.size() == 0) {
				S.add(n);
			}
		}

		// while S is non-empty do
		while (!S.isEmpty()) {
			// remove a node n from S
			Node<T> n = S.iterator().next();
			S.remove(n);

			// insert n into L
			L.add(n);

			// for each node m with an edge e from n to m do
			for (Iterator<Edge<T>> it = n.outEdges.iterator(); it.hasNext();) {
				// remove edge e from the graph
				Edge<T> e = it.next();
				Node<T> m = e.to;
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
		for (Node<T> n : allNodes) {
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
		return L;
	}
}