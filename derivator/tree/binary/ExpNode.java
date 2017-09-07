package derivator.tree.binary;

import derivator.tree.Node;
import derivator.tree.unary.LnNode;

//const^f(x)

public class ExpNode extends Node{

	@Override
	public String toString(){
		return leftChild.toString() + "^" + rightChild.toString();
	}

	@Override
	public Node derivate(){
		Node topNode, secondaryNode, ln;

		topNode = new MultiplicationNode();
		secondaryNode = new MultiplicationNode();

		ln = new LnNode();
		ln.setLeftChild(leftChild);

		topNode.setLeftChild(ln);
		topNode.setRightChild(secondaryNode);

		secondaryNode.setLeftChild(rightChild.derivate());
		secondaryNode.setRightChild(this);

		return topNode;
	}
}