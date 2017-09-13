package derivator.util;

import derivator.tree.Node;
import derivator.tree.leaf.*;
import derivator.tree.unary.*;
import derivator.tree.binary.*;

public class ConstantFolder{
	private ConstantNode leftConst, rightConst;
	private Node leftVar, rightVar;

	public ConstantFolder(){
		leftConst = null;
		rightConst = null;
		leftVar = null;
		rightVar = null;
	}

	public Node constantFoldingAdd(ConstantNode left, ConstantNode right){
		Node folded;
		if(left.isInt() && right.isInt()){
			int sum = left.getIntValue() + right.getIntValue();
			folded = new ConstantNode(Integer.toString(sum));
		}else{
			double sum = left.getDoubleValue() + right.getDoubleValue();
			folded = new ConstantNode(Double.toString(sum));
		}

		if(leftConst != null) leftConst.setZero();
		if(rightConst != null) rightConst.setZero();

		return folded;
	}

	public Node constantFoldingDif(ConstantNode left, ConstantNode right){
		Node folded, minus;
		boolean neg = false;
		if(left.isInt() && right.isInt()){
			int dif = left.getIntValue() - right.getIntValue();
			if(dif < 0){
				neg = true;
				dif = - dif;
			}
			folded = new ConstantNode(Integer.toString(dif));
		}else{
			double dif = left.getDoubleValue() - right.getDoubleValue();
			if(dif < 0){
				neg = true;
				dif = - dif;
			}
			folded = new ConstantNode(Double.toString(dif));
		}
					
		if(neg){
			minus = new MinusNode();
			minus.setLeftChild(folded);
		}else{
			minus = folded;
		}

		if(leftConst != null) leftConst.setZero();
		if(rightConst != null) rightConst.setZero();

		return minus;
	}

	public Node findConstantAdd(Node root, boolean plus){
		Node rightC, leftC, top = null;

		if(!(root instanceof AdditionNode || root instanceof DifferenceNode || root instanceof ConstantNode)) return null;

		if(root instanceof ConstantNode){
			ConstantNode rt = (ConstantNode) root;
			if(plus) top = new PlusNode();
			else top = new MinusNode();
			top.setLeftChild(rt.copy());
			if(leftConst == null) leftConst = rt;
			else rightConst = rt;
			return top;
		}

		leftC = root.getLeftChild();
		rightC = root.getRightChild();

		if(leftC instanceof ConstantNode){
			if(leftConst == null) leftConst = (ConstantNode) leftC;
			else rightConst = (ConstantNode) leftC;

			if(plus)
				top = new PlusNode();
			else
				top = new MinusNode();
			top.setLeftChild(leftC);
			return top;
		}

		if(rightC instanceof ConstantNode){
			if(leftConst == null) leftConst = (ConstantNode) rightC;
			else rightConst = (ConstantNode) rightC;

			if(root instanceof DifferenceNode) plus = !plus;
			if(plus)
				top = new PlusNode();
			else
				top = new MinusNode();
			top.setLeftChild(rightC);
			return top;
		}

		if(root instanceof AdditionNode){
			if(leftC instanceof AdditionNode || leftC instanceof DifferenceNode){
				top = findConstantAdd(leftC, plus);
				if(top != null) return top;
			}

			if(rightC instanceof AdditionNode || rightC instanceof DifferenceNode){
				top = findConstantAdd(rightC, plus);
				if(top != null) return top;
			}
		}else{
			if(leftC instanceof AdditionNode || leftC instanceof DifferenceNode){
				top = findConstantAdd(leftC, plus);
				if(top != null) return top;
			}

			if(rightC instanceof AdditionNode || rightC instanceof DifferenceNode){
				top = findConstantAdd(rightC, !plus);
				if(top != null) return top;
			}
		}
		
		return null;
	}

	public Node findVarAdd(Node root, boolean plus){
		Node top, leftC, rightC;

		if(!(root instanceof AdditionNode || root instanceof DifferenceNode || root instanceof VariableNode || root instanceof MultiplicationNode)) return null;

		if(root instanceof VariableNode){
			if(leftVar == null) leftVar = root;
			else rightVar = root;
			if(plus) top = new PlusNode();
			else top = new MinusNode();
			top.setLeftChild(new ConstantNode("1"));
			return top;
		}

		leftC = root.getLeftChild();
		rightC = root.getRightChild();

		if(root instanceof MultiplicationNode){
			if(leftC instanceof VariableNode){
				if(rightC instanceof ConstantNode){
					if(leftVar == null) leftVar = root;
					else rightVar = root;
					if(plus) top = new PlusNode();
					else top = new MinusNode();
					top.setLeftChild(rightC);
					return top;
				}
			}
			if(rightC instanceof VariableNode){
				if(leftC instanceof ConstantNode){
					if(leftVar == null) leftVar = root;
					else rightVar = root;
					if(plus) top = new PlusNode();
					else top = new MinusNode();
					top.setLeftChild(leftC);
					return top;
				}
			}
		}

		if(leftC instanceof VariableNode){
			if(leftVar == null) leftVar = leftC;
			else rightVar = leftC;

			if(plus) top = new PlusNode();
			else top = new MinusNode();
			top.setLeftChild(new ConstantNode("1"));
			return top;
		}

		if(rightC instanceof VariableNode){
			if(leftVar == null) leftVar = leftC;
			else rightVar = leftC;

			if(root instanceof DifferenceNode) plus = !plus;

			if(plus) top = new PlusNode();
			else top = new MinusNode();
			top.setLeftChild(new ConstantNode("1"));
			return top;
		}

		if(root instanceof AdditionNode){
			if(leftC instanceof AdditionNode || leftC instanceof DifferenceNode){
				top = findConstantAdd(leftC, plus);
				if(top != null) return top;
			}

			if(rightC instanceof AdditionNode || rightC instanceof DifferenceNode){
				top = findConstantAdd(rightC, plus);
				if(top != null) return top;
			}
		}else{
			if(leftC instanceof AdditionNode || leftC instanceof DifferenceNode){
				top = findConstantAdd(leftC, plus);
				if(top != null) return top;
			}

			if(rightC instanceof AdditionNode || rightC instanceof DifferenceNode){
				top = findConstantAdd(rightC, !plus);
				if(top != null) return top;
			}
		}
		
		return null;
	}
}