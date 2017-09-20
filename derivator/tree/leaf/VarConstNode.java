package derivator.tree.leaf;

import derivator.tree.Node;
import derivator.tree.unary.LnNode;

public class VarConstNode extends Node{
	private final String name;

	public VarConstNode(String name){
		super();
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	@Override
	public String toString(){
		if(parent instanceof LnNode) return "(" + name + ")";
		return name;
	}

	@Override
	public boolean isLeaf(){
		return true;
	}

	@Override
	public Node derivate(){
		return new ConstantNode("0");
	}
}