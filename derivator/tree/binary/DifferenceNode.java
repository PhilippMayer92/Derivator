package derivator.tree.binary;

import derivator.util.ConstantFolder;
import derivator.tree.Node;
import derivator.tree.unary.*;
import derivator.tree.leaf.*;

public class DifferenceNode extends Node{

	@Override
	public String toString(){
		if(parent == null) return leftChild.toString() + "-" + rightChild.toString();
		if(parent instanceof AdditionNode) return leftChild.toString() + "-" + rightChild.toString();
		return "(" + leftChild.toString() + "-" + rightChild.toString() + ")";
	}

	@Override
	public Node optimizeLevel0(){
		Node newTop = this;
		ConstantNode cn;
		
		this.setLeftChild(leftChild.optimizeLevel0());
		this.setRightChild(rightChild.optimizeLevel0());

		if(leftChild instanceof ConstantNode){
			cn = (ConstantNode) leftChild;
			if(cn.isZero()){
			 	newTop = new MinusNode();
			 	newTop.setLeftChild(rightChild);
			}
		}

		if(rightChild instanceof ConstantNode){
			cn = (ConstantNode) rightChild;
			if(cn.isZero()) newTop = leftChild;
		}

		return newTop;
	}

	@Override
	public Node optimizeLevel1(){
		Node newTop = this, secondaryNode = null;

		this.setLeftChild(leftChild.optimizeLevel1());
		this.setRightChild(rightChild.optimizeLevel1());

		if(leftChild instanceof MinusNode){
			if(rightChild instanceof MinusNode){
				newTop = new DifferenceNode();
				newTop.setLeftChild(rightChild.getLeftChild());
				newTop.setRightChild(leftChild.getLeftChild());
			}else{
				newTop = new MinusNode();
				secondaryNode = new AdditionNode();
				newTop.setLeftChild(secondaryNode);
				secondaryNode.setLeftChild(leftChild.getLeftChild());
				secondaryNode.setRightChild(rightChild);
			}	
		}else{
			if(rightChild instanceof MinusNode){
				newTop = new AdditionNode();
				newTop.setLeftChild(leftChild);
				newTop.setRightChild(rightChild.getLeftChild());
			}
		}

		return newTop;
	}

	@Override
	public Node optimizeLevel2(){
		Node newTop = this;
		ConstantFolder cf = new ConstantFolder();

		this.setLeftChild(leftChild.optimizeLevel2());
		this.setRightChild(rightChild.optimizeLevel2());

		if(leftChild instanceof ConstantNode && rightChild instanceof ConstantNode){
			ConstantNode left = (ConstantNode) leftChild;
			ConstantNode right = (ConstantNode) rightChild;
			return cf.constantFoldingDif(left, right);
		}else{
			ConstantNode left = null;
			ConstantNode right = null;
			MinusNode minus;
			Node parent;
			Node leftWithSign = cf.findConstantAdd(leftChild, true);
			Node rightWithSign = cf.findConstantAdd(rightChild, false);

			if(rightWithSign != null && leftWithSign != null){
				left = (ConstantNode) leftWithSign.getLeftChild();
				right = (ConstantNode) rightWithSign.getLeftChild();
				
				newTop = new AdditionNode();
				newTop.setRightChild(this);
				
				if(leftWithSign instanceof PlusNode){
					if(rightWithSign instanceof PlusNode)
						newTop.setLeftChild(cf.constantFoldingAdd(left, right));
					else{
						newTop.setLeftChild(cf.constantFoldingDif(left, right));
					}
				}else{
					if(rightWithSign instanceof PlusNode){
						newTop.setLeftChild(cf.constantFoldingDif(left, right));
					}else{
						minus = new MinusNode();
						minus.setLeftChild(cf.constantFoldingAdd(left, right));
						newTop.setLeftChild(minus);	
					}	
				}
				newTop = newTop.optimizeLevel0();
				newTop = newTop.optimizeLevel1();
			}
		}

		return newTop;
	}

	@Override
	public Node optimizeLevel3(){
		Node newTop = this;
		ConstantFolder cf = new ConstantFolder();

		this.setLeftChild(leftChild.optimizeLevel3());
		this.setRightChild(rightChild.optimizeLevel3());

		if(leftChild instanceof VariableNode && rightChild instanceof VariableNode){
			newTop = new ConstantNode("0");
		}else{
			ConstantNode left = null;
			ConstantNode right = null;
			MinusNode minus;
			Node secondaryNode = null;
			Node leftWithSign = cf.findVarAdd(leftChild, true);
			Node rightWithSign = cf.findVarAdd(rightChild, false);

			if(leftWithSign != null && rightWithSign != null){
				left = (ConstantNode) leftWithSign.getLeftChild();
				right = (ConstantNode) rightWithSign.getLeftChild();

				secondaryNode = new MultiplicationNode();
				secondaryNode.setRightChild(new VariableNode(tree.getVarName()));

				newTop = new AdditionNode();
				newTop.setRightChild(this);
				newTop.setLeftChild(secondaryNode);

				if(leftWithSign instanceof PlusNode){
					if(rightWithSign instanceof PlusNode)
						secondaryNode.setLeftChild(cf.constantFoldingAdd(left, right));
					else{
						secondaryNode.setLeftChild(cf.constantFoldingDif(left, right));
					}
				}else{
					if(rightWithSign instanceof PlusNode){
						secondaryNode.setLeftChild(cf.constantFoldingDif(right, left));
					}else{
						minus = new MinusNode();
						minus.setLeftChild(cf.constantFoldingAdd(left, right));
						secondaryNode.setLeftChild(minus);	
					}	
				}
				cf.zeroVarSubtree();
				newTop = newTop.optimizeLevel0();
				newTop = newTop.optimizeLevel1();
			}
		}
		return newTop;
	}

	@Override
	public Node derivate(){
		Node newNode;
		newNode = new DifferenceNode();
		newNode.setLeftChild(leftChild.derivate());
		newNode.setRightChild(rightChild.derivate());
		return newNode;
	}
}