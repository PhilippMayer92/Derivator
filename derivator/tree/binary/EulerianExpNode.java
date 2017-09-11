package derivator.tree.binary;

import derivator.tree.Node;
import derivator.tree.leaf.ConstantNode;

//e^f(x)

public class EulerianExpNode extends Node{
	
	@Override
	public String toString(){
		return "e^" + rightChild.toString();
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
		Node newNode = new MultiplicationNode();

		newNode.setRightChild(this);
		newNode.setLeftChild(rightChild.derivate());

		return newNode;
	}
}