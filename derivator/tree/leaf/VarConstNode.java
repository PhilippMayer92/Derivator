package derivator.tree.leaf;

import derivator.tree.Node;

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