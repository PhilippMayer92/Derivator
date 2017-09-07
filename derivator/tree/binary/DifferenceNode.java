package derivator.tree.binary;

import derivator.util.ConstantFolder;
import derivator.tree.Node;
import derivator.tree.unary.*;
import derivator.tree.leaf.*;

public class DifferenceNode extends Node{

	@Override
	public String toString(){
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
			Node leftWithSign = cf.findConstant(leftChild, true);
			Node rightWithSign = cf.findConstant(rightChild, false);

			if(rightWithSign != null && leftWithSign != null){
				left = (ConstantNode) leftWithSign.getLeftChild();
				System.out.println("left: "+leftWithSign.toString());
				right = (ConstantNode) rightWithSign.getLeftChild();
				System.out.println("right: "+rightWithSign.toString());
				
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

	/*@Override
	public Node optimize(){
		Node newTop = this, secondaryNode = null, operationNode = this;

		this.setLeftChild(leftChild.optimize());
		this.setRightChild(rightChild.optimize());

		if(leftChild instanceof MinusNode){
			if(rightChild instanceof MinusNode){
				newTop = new MinusNode();
				secondaryNode = new DifferenceNode();
				operationNode = secondaryNode;
				newTop.setLeftChild(secondaryNode);
				secondaryNode.setLeftChild(leftChild.getLeftChild());
				secondaryNode.setRightChild(rightChild.getLeftChild());
			}else{
				newTop = new MinusNode();
				secondaryNode = new AdditionNode();
				operationNode = secondaryNode;
				newTop.setLeftChild(secondaryNode);
				secondaryNode.setLeftChild(leftChild.getLeftChild());
				secondaryNode.setRightChild(rightChild);
			}	
		}else{
			if(rightChild instanceof MinusNode){
				newTop = new AdditionNode();
				operationNode = newTop;
				newTop.setLeftChild(leftChild);
				newTop.setRightChild(rightChild.getLeftChild());
			}
		}

		if(operationNode instanceof DifferenceNode){
			Node folded = operationNode;
			if(operationNode.getLeftChild() instanceof ConstantNode && operationNode.getRightChild() instanceof ConstantNode){
				ConstantNode left = (ConstantNode) operationNode.getLeftChild();
				ConstantNode right = (ConstantNode) operationNode.getRightChild();
				Node minus;
				if(!left.isVariable() && !right.isVariable()){
					boolean neg = false;
					if(left.isInt() && right.isInt()){
						int dif = left.getIntValue() - right.getIntValue();
						if(dif < 0){
							neg = true;
							dif = - dif;
						}
						folded = new ConstantNode(Integer.toString(dif), false);
					}else{
						double dif = left.getDoubleValue() - right.getDoubleValue();
						if(dif < 0){
							neg = true;
							dif = - dif;
						}
						folded = new ConstantNode(Double.toString(dif), false);
					}
					
					if(neg){
						minus = new MinusNode();
						minus.setLeftChild(folded);
					}else{
						minus = folded;
					}

					if(secondaryNode == null){
						newTop = minus;
					}else{
						newTop.setLeftChild(minus);
					}
				}else{
					if(!left.isVariable() && left.getDoubleValue() == 0){
						folded = new MinusNode();
						folded.setLeftChild(right);
					}

					if(!right.isVariable() && right.getDoubleValue() == 0){
						folded = left;
					}

					if(right.isVariable() && left.isVariable()){
						if(right.getName().equals(left.getName())){
							folded = new ConstantNode("0", false);
						}
					}

					if(secondaryNode == null){
						newTop = folded;
					}else{
						newTop.setLeftChild(folded);
					}
				}
			}else{
				if(operationNode.getRightChild() instanceof VariableNode && operationNode.getLeftChild() instanceof ConstantNode){
					ConstantNode left = (ConstantNode) operationNode.getLeftChild();
					if(!left.isVariable() && left.getDoubleValue() == 0){
						folded = new MinusNode();
						folded.setLeftChild(new VariableNode(tree.getVarName()));
					}
				}

				if(operationNode.getLeftChild() instanceof VariableNode && operationNode.getRightChild() instanceof ConstantNode){
					ConstantNode right = (ConstantNode) operationNode.getRightChild();
					if(!right.isVariable() && right.getDoubleValue() == 0){
						folded = new VariableNode(tree.getVarName());
					}
				}

				if(operationNode.getRightChild() instanceof VariableNode && operationNode.getLeftChild() instanceof VariableNode){
					folded = new ConstantNode("0", false);
				}

				if(secondaryNode == null){
					newTop = folded;
				}else{
					newTop.setLeftChild(folded);
				}
			}
		}else{
			operationNode = operationNode.optimize();
			if(secondaryNode == null){
				newTop = operationNode;
			}else{
				newTop.setLeftChild(operationNode);
			}
		}

		return newTop;
	}*/

	@Override
	public Node derivate(){
		Node newNode;
		newNode = new DifferenceNode();
		newNode.setLeftChild(leftChild.derivate());
		newNode.setRightChild(rightChild.derivate());
		return newNode;
	}
}