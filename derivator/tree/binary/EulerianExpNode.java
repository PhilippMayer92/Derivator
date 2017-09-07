package derivator.tree.binary;

import derivator.tree.Node;

//e^f(x)

public class EulerianExpNode extends Node{
	
	@Override
	public String toString(){
		return "e^" + rightChild.toString();
	}

	@Override
	public Node derivate(){
		Node newNode = new MultiplicationNode();

		newNode.setRightChild(this);
		newNode.setLeftChild(rightChild.derivate());

		return newNode;
	}
}