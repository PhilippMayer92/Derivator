package derivator.tree.binary;

import derivator.tree.Node;
import derivator.tree.unary.LnNode;

//f(x)^g(x)

public class VarExpNode extends Node{
	
	@Override
	public String toString(){
		return leftChild.toString() + "^" + rightChild.toString();
	}

	@Override
	public Node derivate(){
		Node topNode, sum, prod, ln, quotient;

		topNode = new MultiplicationNode();
		topNode.setRightChild(this);

		sum = new AdditionNode();

		prod = new MultiplicationNode();
		prod.setLeftChild(rightChild.derivate());
		ln = new LnNode();
		ln.setLeftChild(leftChild);
		prod.setRightChild(ln);
		sum.setLeftChild(prod);

		prod = new MultiplicationNode();
		prod.setLeftChild(rightChild);
		quotient = new DivisionNode();
		quotient.setLeftChild(leftChild.derivate());
		quotient.setRightChild(leftChild);
		prod.setRightChild(quotient);
		sum.setRightChild(prod);

		topNode.setLeftChild(sum);
		return topNode;
	}
}