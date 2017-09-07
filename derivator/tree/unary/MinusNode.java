package derivator.tree.unary;

import derivator.tree.Node;
import derivator.tree.binary.DifferenceNode;

public class MinusNode extends Node{

	@Override
	public String toString(){
		return "(-" + leftChild.toString() + ")";
	}

	@Override
	public Node optimizeLevel1(){
		Node newTop = this;

		this.setLeftChild(leftChild.optimizeLevel1());

		if(leftChild instanceof MinusNode){
			newTop = leftChild.getLeftChild();
		}

		if(leftChild instanceof DifferenceNode){
			newTop = new DifferenceNode();
			newTop.setLeftChild(leftChild.getRightChild());
			newTop.setRightChild(leftChild.getLeftChild());
		}

		return newTop;
	}

	@Override
	public Node derivate(){
		Node newNode = new MinusNode();
		newNode.setLeftChild(leftChild.derivate());
		return newNode;
	}
}