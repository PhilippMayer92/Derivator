package derivator.tree.leaf;

import derivator.tree.Node;

public class ConstantNode extends Node{
	private boolean isInt;
	private int intValue;
	private double doubleValue;

	public ConstantNode(String value){
		super();
		try{
			intValue = Integer.parseInt(value);
			isInt = true;
		}catch(NumberFormatException e){	
			intValue = 0;
			isInt = false;
		}
		doubleValue = Double.parseDouble(value);
	}

	public ConstantNode copy(){
		if(isInt){
			return new ConstantNode(Integer.toString(intValue));
		}else{
			return new ConstantNode(Double.toString(doubleValue));
		}		
	}

	public boolean isInt(){
		return isInt;
	}

	public void setZero(){
		isInt = true;
		intValue = 0;
		doubleValue = 0.0;
	}

	public boolean isZero(){
		if(isInt){
			if(intValue == 0) return true;
			else return false;
		}else{
			if(doubleValue == 0.0) return true;
			else return false;
		}
	}

	public boolean isOne(){
		if(isInt){
			if(intValue == 1) return true;
			else return false;
		}else{
			if(doubleValue == 1.0) return true;
			else return false;
		}
	}

	public int getIntValue(){
		return intValue;
	}

	public double getDoubleValue(){
		return doubleValue;
	}

	@Override
	public String toString(){
		if(isInt)
			return Integer.toString(intValue);
		else
			return Double.toString(doubleValue);
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