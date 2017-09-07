package derivator.tree.unary;

import derivator.tree.Node;
import derivator.tree.binary.DivisionNode;

public class LnNode extends Node{

	@Override
	public String toString(){
		return "ln" + leftChild.toString();
	}

	@Override
	public Node derivate(){
		Node newNode = new DivisionNode();

		newNode.setLeftChild(leftChild.derivate());
		newNode.setRightChild(leftChild);

		return newNode;
	}
}