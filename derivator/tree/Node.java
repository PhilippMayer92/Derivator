package derivator.tree;

import derivator.tree.leaf.ConstantNode;

public abstract class Node{
	protected Node parent;
	protected Node leftChild;
	protected Node rightChild;
	protected Tree tree;

	public void setTree(Tree tree){
		this.tree = tree;
		if(rightChild != null)
			rightChild.setTree(tree);
		if(leftChild != null)
			leftChild.setTree(tree);
	}

	private void setParent(Node parent){
		this.parent = parent;
	}

	public Node getParent(){
		return parent;
	}

	public void setLeftChild(Node child){
		this.leftChild = child;
		child.setParent(this);
		child.setTree(tree);
	}

	public Node getLeftChild(){
		return leftChild;
	}

	public void setRightChild(Node child){
		this.rightChild = child;
		child.setParent(this);
		child.setTree(tree);
	}

	public Node getRightChild(){
		return rightChild;
	}

	public boolean isLeaf(){
		return false;
	}

	public boolean isSubtreeConstant(){
		if(leftChild != null){
			return leftChild.isSubtreeConstant();
		}

		if(rightChild != null){
			return rightChild.isSubtreeConstant();
		}

		return true;
	}

	public abstract String toString();

	public Node optimize(){
		if(leftChild != null){
			this.setLeftChild(leftChild.optimize());
		}
		if(rightChild != null){
			this.setRightChild(rightChild.optimize());
		}
		return this;
	}

	public Node optimizeLevel0(){
		if(leftChild != null){
			this.setLeftChild(leftChild.optimizeLevel0());
		}
		if(rightChild != null){
			this.setRightChild(rightChild.optimizeLevel0());
		}
		return this;
	}

	public Node optimizeLevel1(){
		if(leftChild != null){
			this.setLeftChild(leftChild.optimizeLevel1());
		}
		if(rightChild != null){
			this.setRightChild(rightChild.optimizeLevel1());
		}
		return this;
	}

	public Node optimizeLevel2(){
		if(leftChild != null){
			this.setLeftChild(leftChild.optimizeLevel2());
		}
		if(rightChild != null){
			this.setRightChild(rightChild.optimizeLevel2());
		}
		return this;
	}

	public Node optimizeLevel3(){
		if(leftChild != null){
			this.setLeftChild(leftChild.optimizeLevel3());
		}
		if(rightChild != null){
			this.setRightChild(rightChild.optimizeLevel3());
		}
		return this;
	}

	public abstract Node derivate();

	public void free(){
		if(parent != null){
			if(parent.getLeftChild() == this) parent.setLeftChild(null);
			if(parent.getRightChild() == this) parent.setRightChild(null);
			parent = null;
		}

		if(leftChild != null){
			if(leftChild.getParent() == this) leftChild.setParent(null);
			leftChild = null;
		}

		if(rightChild != null){
			if(rightChild.getParent() == this) rightChild.setParent(null);
			rightChild = null;
		}
	}
}