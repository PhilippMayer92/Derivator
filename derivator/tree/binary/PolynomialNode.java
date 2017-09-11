package derivator.tree.binary;

import derivator.tree.Node;
import derivator.tree.leaf.ConstantNode;

//f(x)^const

public class PolynomialNode extends Node{
	
	@Override
	public String toString(){
		return leftChild.toString() + "^" + rightChild.toString();
	}

	@Override
	public Node optimizeLevel0(){
		Node newTop = this;

		this.setLeftChild(leftChild.optimizeLevel0());
		this.setRightChild(rightChild.optimizeLevel0());

		if(rightChild instanceof ConstantNode){
			ConstantNode cn = (ConstantNode) rightChild;
			if(cn.isZero()) newTop = new ConstantNode("1");
			if(cn.isOne()) newTop = leftChild;
		}

		return newTop;
	}

	@Override
	public Node derivate(){
		Node topNode, newNode, exponent, product;
		product = new MultiplicationNode();

		newNode = new PolynomialNode();
		newNode.setLeftChild(leftChild);
		exponent = new DifferenceNode();
		exponent.setLeftChild(rightChild);
		exponent.setRightChild(new ConstantNode("1"));
		newNode.setRightChild(exponent);

		product.setRightChild(newNode);
		product.setLeftChild(leftChild.derivate());

		topNode = new MultiplicationNode();
		topNode.setRightChild(product);
		topNode.setLeftChild(rightChild);

		return topNode;
	}
}