package derivator.tree;

public class Tree{
	private Node root;
	private char varName;

	public Tree(Node root, char varName){
		this.root = root;
		this.varName = varName;
		root.setTree(this);
	}

	public char getVarName(){
		return varName;
	}

	public void print(){
		String tree = root.toString();
		System.out.println(tree);
	}

	@Override
	public String toString(){
		return root.toString();
	}

	/*public void optimize(){
		root = root.optimize();
	}*/

	//neutrale Elemente beseitigen
	public void optimizeLevel0(){
		root = root.optimizeLevel0();
		root.setTree(this);
	}

	//Vorzeichen nach oben bringen
	public void optimizeLevel1(){
		root = root.optimizeLevel1();
		root.setTree(this);
	}

	//(kommutatives) Constant Folding
	public void optimizeLevel2(){
		root = root.optimizeLevel2();
		root.setTree(this);
	}

	//(kommutatives) Constant Folding fuer Variablen
	public void optimizeLevel3(){
		root = root.optimizeLevel3();
		root.setTree(this);
	}

	public Tree derivate(){
		Node newRoot = root.derivate();
		return new Tree(newRoot, varName);
	}
}