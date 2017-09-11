package derivator.tree.binary;

import derivator.tree.Node;
import derivator.tree.unary.MinusNode;
import derivator.tree.leaf.ConstantNode;

public class DivisionNode extends Node{

	@Override
	public String toString(){
		if(parent == null) return leftChild.toString() + "/" + rightChild.toString();
		if(parent instanceof MultiplicationNode) return leftChild.toString() + "/" + rightChild.toString();
		return "(" + leftChild.toString() + "/" + rightChild.toString() + ")";
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
		}

		if(rightChild instanceof ConstantNode){
			cn = (ConstantNode) rightChild;
			if(cn.isZero()) throw new ArithmeticException("Division by zero");
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
		Node folded = this;
		if(leftChild instanceof ConstantNode){
			ConstantNode left = (ConstantNode) leftChild;
			//Both Nodes are constants
			if(rightChild instanceof ConstantNode){
				ConstantNode right = (ConstantNode) rightChild;
				//Number / Number
				if(!left.isVariable() && !right.isVariable()){
					double div;
					int div1;
					String div2;
					if(right.getDoubleValue() == 0) throw new ArithmeticException("Division by zero");
					div = left.getDoubleValue() / right.getDoubleValue();
					div1 = (int) div;
					if((double) div1 == div){
						div2 = Integer.toString(div1);
					}else{
						div2 = Double.toString(div);
					}

					folded = new ConstantNode(div2, false);
				}else{
					if(!left.isVariable()){
						double value = left.getDoubleValue();
						if(value == 0){
							folded = new ConstantNode("0", false);
						}	
					}

					if(left.isVariable() && right.isVariable()){
						if(left.getName().equals(right.getName())){
							folded = new ConstantNode("1", false);
						}
					}
				}
			//Only left Node is constant
			}else{
				if(!left.isVariable()){
					double value = left.getDoubleValue();
					if(value == 0){
						folded = new ConstantNode("0", false);
					}
				}
			}
		}else{
			if(rightChild instanceof ConstantNode){
				ConstantNode right = (ConstantNode) rightChild;
				if(!right.isVariable()){
					double value = right.getDoubleValue();
					if(value == 0){
						throw new ArithmeticException("Division by zero");
					}
					if(value == 1){
						folded = leftChild;
					}				
				}	
			}else{
				if(leftChild instanceof VariableNode && rightChild instanceof VariableNode){
					folded = new ConstantNode("1", false);
				}
			}
		}

		if(secondaryNode == null){
			newTop = folded;
		}else{
			newTop.setLeftChild(folded);
		}

		return newTop;
	}*/

	@Override
	public Node derivate(){
		Node newNode, left, right, denominator, numerator;
		newNode = new DivisionNode();

		numerator = new DifferenceNode();
		left = new MultiplicationNode();
		left.setLeftChild(leftChild.derivate());
		left.setRightChild(rightChild);
		right = new MultiplicationNode();
		right.setLeftChild(leftChild);
		right.setRightChild(rightChild.derivate());
		numerator.setRightChild(right);
		numerator.setLeftChild(left);

		denominator = new PolynomialNode();
		denominator.setLeftChild(rightChild);
		denominator.setRightChild(new ConstantNode("2"));

		newNode.setLeftChild(numerator);
		newNode.setRightChild(denominator);
		return newNode;
	}
}