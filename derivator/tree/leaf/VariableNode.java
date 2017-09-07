package derivator.tree.leaf;

import derivator.tree.Node;

public class VariableNode extends Node{
	private char name;

	public VariableNode(char name){
		super();
		this.name = name;
	}

	@Override
	public String toString(){
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
}