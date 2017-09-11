package derivator.tree.unary;

import derivator.tree.Node;
import derivator.tree.binary.DivisionNode;
import derivator.tree.leaf.ConstantNode;
import derivator.tree.unary.MinusNode;

public class LnNode extends Node{

	@Override
	public String toString(){
		return "ln" + leftChild.toString();
	}

	@Override
	public Node optimizeLevel0(){
		Node newTop = this;

		this.setLeftChild(leftChild.optimizeLevel0());

		if(leftChild instanceof ConstantNode){
			ConstantNode cn = (ConstantNode) leftChild;
			if(cn.isOne()) newTop = new ConstantNode("0");
			if(cn.isZero()) throw new ArithmeticException("natural logarithm is undefined for operands smaller or equal zero");
		}

		if(leftChild instanceof MinusNode){
			if(leftChild.getLeftChild() instanceof ConstantNode) throw new ArithmeticException("natural logarithm is undefined for operands smaller or equal zero");
		}

		return newTop;
	}

	@Override
	public Node derivate(){
		Node newTop = new DivisionNode();

		newTop.setLeftChild(leftChild.derivate());
		newTop.setRightChild(leftChild);

		return newTop;
	}
}