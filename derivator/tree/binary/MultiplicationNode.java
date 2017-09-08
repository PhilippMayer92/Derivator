package derivator.tree.binary;

import derivator.tree.Node;
import derivator.tree.unary.MinusNode;
import derivator.tree.leaf.ConstantNode;

public class MultiplicationNode extends Node{

	@Override
	public String toString(){
		if(parent == null) return leftChild.toString() + "*" + rightChild.toString();
		if(parent instanceof MultiplicationNode) return leftChild.toString() + "*" + rightChild.toString();
		return "(" + leftChild.toString() + "*" + rightChild.toString() + ")";
	}

	@Override
	public Node optimizeLevel0(){
		Node newTop = this;
		ConstantNode cn;

		this.setLeftChild(leftChild.optimizeLevel0());
		this.setRightChild(rightChild.optimizeLevel0());

		if(leftChild instanceof ConstantNode){
			cn = (ConstantNode) leftChild;
			if(cn.isZero()) newTop = new ConstantNode("0");
			if(cn.isOne()) newTop = rightChild;
		}

		if(rightChild instanceof ConstantNode){
			cn = (ConstantNode) rightChild;
			if(cn.isZero()) newTop = new ConstantNode("0");
			if(cn.isOne()) newTop = leftChild;
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
				newTop = this;
				this.setRightChild(rightChild.getLeftChild());
			}else{
				newTop = new MinusNode();
				newTop.setLeftChild(this);
				secondaryNode = this;
			}
			this.setLeftChild(leftChild.getLeftChild());
		}else{
			if(rightChild instanceof MinusNode){
				newTop = new MinusNode();
				newTop.setLeftChild(this);
				this.setRightChild(rightChild.getLeftChild());
				secondaryNode = this;
			}
		}

		return newTop;
	}

	/*@Override
	public Node optimize(){
		Node newTop = this, secondaryNode = null;

		this.setLeftChild(leftChild.optimize());
		this.setRightChild(rightChild.optimize());

		if(leftChild instanceof MinusNode){
			if(rightChild instanceof MinusNode){
				newTop = this;
				this.setRightChild(rightChild.getLeftChild());
			}else{
				newTop = new MinusNode();
				newTop.setLeftChild(this);
				secondaryNode = this;
			}
			this.setLeftChild(leftChild.getLeftChild());
		}else{
			if(rightChild instanceof MinusNode){
				newTop = new MinusNode();
				newTop.setLeftChild(this);
				this.setRightChild(rightChild.getLeftChild());
				secondaryNode = this;
			}
		}

		//Constant Folding
		if(leftChild instanceof ConstantNode){
			ConstantNode left = (ConstantNode) leftChild;
			Node folded;
			//Both Nodes are constants
			if(rightChild instanceof ConstantNode){
				ConstantNode right = (ConstantNode) rightChild;
				//Number * Number
				if(!left.isVariable() && !right.isVariable()){
					if(left.isInt() && right.isInt()){
						int product = left.getIntValue() * right.getIntValue();
						folded = new ConstantNode(Integer.toString(product), false);
					}else{
						double product = left.getDoubleValue() * right.getDoubleValue();
						folded = new ConstantNode(Double.toString(product), false);
					}

					if(secondaryNode == null){
						newTop = folded;
					}else{
						newTop.setLeftChild(folded);
					}
				}else{
					folded = this;
					if(!left.isVariable() || !right.isVariable()){
						double value;
						if(!left.isVariable()) value = left.getDoubleValue();
						else value = right.getDoubleValue();
						if(value == 0){
							folded = new ConstantNode("0", false);
						}else{
							if(value == 1){
								folded = rightChild;
							}
						}
					}else{
						if(left.getName().equals(right.getName())){
							folded = new ConstExpNode();
							folded.setLeftChild(left);
							folded.setRightChild(new ConstantNode("2", false));
						}
					}

					if(secondaryNode == null){
						newTop = folded;
					}else{
						newTop.setLeftChild(folded);
					}
				}
			//Only left Node is constant
			}else{
				if(!left.isVariable()){
					double value = left.getDoubleValue();
					if(value == 0){
						folded = new ConstantNode("0", false);
					}else{
						if(value == 1){
							folded = rightChild;
						}else{
							folded = this;
						}
					}

					if(secondaryNode == null){
						newTop = folded;
					}else{
						newTop.setLeftChild(folded);
					}
				}
			}
		}else{
			if(rightChild instanceof ConstantNode){
				ConstantNode right = (ConstantNode) rightChild;
				if(!right.isVariable()){
					double value = right.getDoubleValue();
					Node folded;
					if(value == 0){
						folded = new ConstantNode("0", false);
					}else{
						if(value == 1){
							folded = leftChild;
						}else{
							folded = this;
						}
					}

					if(secondaryNode == null){
						newTop = folded;
					}else{
						newTop.setLeftChild(folded);
					}
				}
			}
		}

		if(leftChild instanceof VariableNode && rightChild instanceof VariableNode){
			Node folded;
			folded = new PolynomialNode();
			folded.setLeftChild(new VariableNode(tree.getVarName()));
			folded.setRightChild(new ConstantNode("2", false));
			
			if(secondaryNode == null){
				newTop = folded;
			}else{
				newTop.setLeftChild(folded);
			}
		}

		if(leftChild instanceof PolynomialNode && rightChild instanceof VariableNode){
			Node folded = this;

			if(leftChild.getLeftChild() instanceof VariableNode){
				folded = new PolynomialNode();
				folded.setLeftChild(new VariableNode(tree.getVarName()));
				folded.setRightChild(new AdditionNode());
				folded.getRightChild().setLeftChild(leftChild.getRightChild());
				folded.getRightChild().setRightChild(new ConstantNode("1", false));
				folded.setRightChild(folded.getRightChild().optimize());
			}

			if(secondaryNode == null){
				newTop = folded;
			}else{
				newTop.setLeftChild(folded);
			}
		}

		if(leftChild instanceof VariableNode && rightChild instanceof PolynomialNode){
			Node folded = this;

			if(rightChild.getLeftChild() instanceof VariableNode){
				folded = new PolynomialNode();
				folded.setLeftChild(new VariableNode(tree.getVarName()));
				folded.setRightChild(new AdditionNode());
				folded.getRightChild().setLeftChild(rightChild.getRightChild());
				folded.getRightChild().setRightChild(new ConstantNode("1", false));
				folded.setRightChild(folded.getRightChild().optimize());
			}

			if(secondaryNode == null){
				newTop = folded;
			}else{
				newTop.setLeftChild(folded);
			}
		}

		if(leftChild instanceof PolynomialNode && rightChild instanceof PolynomialNode){
			Node folded = this;

			if(rightChild.getLeftChild() instanceof VariableNode && leftChild.getLeftChild() instanceof VariableNode){
				folded = new PolynomialNode();
				folded.setLeftChild(new VariableNode(tree.getVarName()));
				folded.setRightChild(new AdditionNode());
				folded.getRightChild().setLeftChild(leftChild.getRightChild());
				folded.getRightChild().setRightChild(rightChild.getRightChild());
				folded.setRightChild(folded.getRightChild().optimize());
			}

			if(secondaryNode == null){
				newTop = folded;
			}else{
				newTop.setLeftChild(folded);
			}
		}

		return newTop;
	}*/

	@Override
	public Node derivate(){
		Node newNode, left, right;
		newNode = new AdditionNode();
		left = new MultiplicationNode();
		left.setLeftChild(leftChild);
		left.setRightChild(rightChild.derivate());
		right = new MultiplicationNode();
		right.setLeftChild(leftChild.derivate());
		right.setRightChild(rightChild);
		newNode.setRightChild(right);
		newNode.setLeftChild(left);
		return newNode;
	}
}