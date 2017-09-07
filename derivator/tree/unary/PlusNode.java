package derivator.tree.unary;

import derivator.tree.Node;

public class PlusNode extends Node{

	@Override
	public String toString(){
		return "(+" + leftChild.toString() + ")";
	}

	@Override
	public Node optimizeLevel1(){
		this.setLeftChild(leftChild.optimizeLevel1());

		return leftChild;
	}

	@Override
	public Node derivate(){
		Node newNode = new PlusNode();
		newNode.setLeftChild(leftChild.derivate());
		return newNode;
	}
}