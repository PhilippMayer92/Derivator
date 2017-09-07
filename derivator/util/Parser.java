package derivator.util;

import derivator.tree.*;
import derivator.tree.leaf.*;
import derivator.tree.unary.*;
import derivator.tree.binary.*;


public class Parser{
	private static Symbol symbol;
	private static Scanner scanner;
	private static char var;
	
	public static Tree parse(Scanner scanner, char var) throws ParsingException, ScanningException{
		Parser.scanner = scanner;
		Parser.var = var;
		symbol = scanner.getSymbol();
		Node root = gr_formula();
		Tree function = new Tree(root, var);
		return function;
	}

	private static Node gr_formula() throws ParsingException, ScanningException{
		Node n = gr_expression();
		if(symbol.getType() != Symbol.EOF) unexpectedSymbol();
		return n;
	}

	private static Node gr_expression() throws ParsingException, ScanningException{
		Node leftArg, rightArg;
		boolean operatorSwitch = false; //has to be toggled with each '-'
		Node topNode = null, openNode = null, newNode = null;
		Symbol operator;

		leftArg = gr_term();

		while(symbol.isPlusOrMinus()){
			operator = symbol;

			symbol = scanner.getSymbol();

			rightArg = gr_term();

			if(operatorSwitch){
				if(operator.isMinus()){
					newNode = new AdditionNode();
					operatorSwitch = !operatorSwitch;
				}else{
					newNode = new DifferenceNode();
				}
			}else{
				if(operator.isMinus()){
					newNode = new DifferenceNode();
					operatorSwitch = !operatorSwitch;
				}else{
					newNode = new AdditionNode();
				}
			}

			newNode.setLeftChild(leftArg);
			leftArg = rightArg;

			if(topNode == null){
				topNode = newNode;
			}else{
				openNode.setRightChild(newNode);
			}
			openNode = newNode;
		}

		if(topNode == null) topNode = leftArg;
		else openNode.setRightChild(leftArg);

		return topNode;
	}

	private static Node gr_term() throws ParsingException, ScanningException{
		Node leftArg, rightArg;
		boolean operatorSwitch = false; //has to be toggled with each '/'
		Node topNode = null, openNode = null, newNode = null;
		Symbol operator;

		leftArg = gr_unary();

		while(symbol.isMultOrDiv()){
			operator = symbol;

			symbol = scanner.getSymbol();

			rightArg = gr_unary();

			if(operatorSwitch){
				if(operator.isDiv()){
					newNode = new MultiplicationNode();
					operatorSwitch = !operatorSwitch;
				}else{
					newNode = new DivisionNode();
				}
			}else{
				if(operator.isDiv()){
					newNode = new DivisionNode();
					operatorSwitch = !operatorSwitch;
				}else{
					newNode = new MultiplicationNode();
				}
			}

			newNode.setLeftChild(leftArg);
			leftArg = rightArg;

			if(topNode == null){
				topNode = newNode;
			}else{
				openNode.setRightChild(newNode);
			}
			openNode = newNode;
		}

		if(topNode == null) topNode = leftArg;
		else openNode.setRightChild(leftArg);

		return topNode;
	}

	private static Node gr_unary() throws ParsingException, ScanningException{
		boolean sign = false;
		Node node = null, childNode = null, arg = null;

		if(symbol.isPlusOrMinus()){
			
			if(symbol.isMinus()){
				node = new MinusNode();
			}else{
				node = new PlusNode();
			}
			sign = true;
			symbol = scanner.getSymbol();
		}

		if(symbol.getType() == Symbol.LN){
			symbol = scanner.getSymbol();

			if(symbol.getType() != Symbol.LPARENTHESIS)
				unexpectedSymbol();

			symbol = scanner.getSymbol();

			arg = gr_expression();

			if(symbol.getType() != Symbol.RPARENTHESIS)
				missingRParenthesis();

			symbol = scanner.getSymbol();

			childNode = new LnNode();

			childNode.setLeftChild(arg);

		}else{
			childNode = gr_factor();
		}
			
		if(sign){
			node.setLeftChild(childNode);
			return node;
		}else{
			return childNode;
		}
		
	}

	private static Node gr_factor() throws ParsingException, ScanningException{
		Node base = null, exponent = null, power = null;
		Symbol baseType, exponentType;
		int type;
		boolean baseConstant, exponentConstant;

		baseType = symbol;
		type = symbol.getType();
		switch(type){
			case Symbol.VARIABLE:
				base = new VariableNode(var);
				break;
			case Symbol.CONSTANTVAR:
				base = new VarConstNode(symbol.getValue());
				break;
			case Symbol.CONSTANT:
				base = new ConstantNode(symbol.getValue());
				break;
			case Symbol.LPARENTHESIS:
				symbol = scanner.getSymbol();
				base = gr_expression();
				if(symbol.getType() != Symbol.RPARENTHESIS) missingRParenthesis();
				break;
			default:
				unexpectedSymbol();
		}

		symbol = scanner.getSymbol();
		
		if(symbol.getType() != Symbol.ZIRKUMFLEX) return base;
		
		symbol = scanner.getSymbol();

		exponentType = symbol;
		type = symbol.getType();
		switch(type){
			case Symbol.VARIABLE:
				exponent = new VariableNode(var);
				break;
			case Symbol.CONSTANTVAR:
				exponent = new VarConstNode(symbol.getValue());
				break;
			case Symbol.CONSTANT:
				exponent = new ConstantNode(symbol.getValue());
				break;
			case Symbol.LPARENTHESIS:
				symbol = scanner.getSymbol();
				exponent = gr_expression();
				if(symbol.getType() != Symbol.RPARENTHESIS) missingRParenthesis();
				break;
			default:
				unexpectedSymbol();
		}

		baseConstant = base.isSubtreeConstant();
		exponentConstant = exponent.isSubtreeConstant();
		if(baseConstant){
			if(base instanceof VarConstNode){
				VarConstNode cn = (VarConstNode) base;
				String name = cn.getName();
				if(name.equals("e")){
					power = new EulerianExpNode();
				}else{
					if(exponentConstant){
						power = new ConstExpNode();
					}else{
						power = new ExpNode();
					}
				}
			}else{
				if(exponentConstant){
					power = new ConstExpNode();
				}else{
					power = new ExpNode();
				}
			}
		}else{
			if(exponentConstant){
				power = new PolynomialNode();
			}else{
				power = new VarExpNode();
			}
		}
		
		power.setLeftChild(base);
		power.setRightChild(exponent);

		symbol = scanner.getSymbol();

		return power;
	}

	private static void checkpoint(int nr){
		System.out.println("checkpoint " + nr + " reached");
	}

	private static void missingRParenthesis() throws ParsingException{
		throw new ParsingException("Missing ')' at index " + scanner.getIndex());
	}

	private static void unexpectedSymbol() throws ParsingException{
		throw new ParsingException("Unexpected symbol " + symbol.toString() + " found around index " + (scanner.getIndex() - 1));
	}
}