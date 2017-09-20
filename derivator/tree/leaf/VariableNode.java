package derivator.tree.leaf;

import derivator.tree.Node;
import derivator.tree.unary.LnNode;

public class VariableNode extends Node{
	private char name;

	public VariableNode(char name){
		super();
		this.name = name;
	}

	@Override
	public String toString(){
		if(parent instanceof LnNode) return "(" + Character.toString(name) + ")";
		return Character.toString(name);
	}

	@Override
	public boolean isLeaf(){
		return true;
	}

	@Override
	public boolean isSubtreeConstant(){
		return false;
	}

	@Override
	public Node derivate(){
		return new ConstantNode("1");
	}

	public void setZero(){
		if(parent.getLeftChild() == this) parent.setLeftChild(new ConstantNode("0"));
		else parent.setRightChild(new ConstantNode("0"));
	}
}