package derivator.tree.binary;

import derivator.tree.Node;
import derivator.tree.leaf.ConstantNode;
import derivator.tree.unary.MinusNode;

//const^const

public class ConstExpNode extends Node{

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
	public Node optimizeLevel1(){
		Node newTop = this, secondaryNode = null;

		this.setLeftChild(leftChild.optimizeLevel1());
		this.setRightChild(rightChild.optimizeLevel1());

		if(rightChild instanceof MinusNode){
			newTop = new DivisionNode();
			newTop.setLeftChild(new ConstantNode("1"));
			newTop.setRightChild(this);
			this.setRightChild(this.getRightChild().getLeftChild());
			secondaryNode = this;
		}

		return newTop;
	}

	/*@Override
	public Node optimize(){
		Node newTop = this, secondaryNode = null;

		this.setLeftChild(leftChild.optimize());
		this.setRightChild(rightChild.optimize());

		if(rightChild instanceof MinusNode){
			newTop = new DivisionNode();
			newTop.setLeftChild(new ConstantNode("1", false));
			newTop.setRightChild(this);
			this.setRightChild(this.getRightChild().getLeftChild());
			secondaryNode = this;
		}

		//fail, falls basis keine Konstante ist
		ConstantNode left = (ConstantNode) leftChild;
		ConstantNode right = (ConstantNode) rightChild;
		if(!left.isVariable() && !right.isVariable()){
			ConstantNode folded;
			double pow = Math.pow(left.getIntValue(), right.getIntValue());
			if(left.isInt() && right.isInt()){
				int pow1 = (int) pow;
				folded = new ConstantNode(Integer.toString(pow1), false);
			}else{
				folded = new ConstantNode(Double.toString(pow), false);
			}

			if(secondaryNode == null){
				newTop = folded;
			}else{
				newTop.setRightChild(folded);
			}
		}

		return newTop;
	}*/

	@Override
	public Node derivate(){
		return new ConstantNode("0");
	}
}