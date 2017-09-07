package derivator.util;

import java.io.*;

public class Scanner{
	private String formula;
	private int length;
	private int index;
	private char var;


	public Scanner(String formula, char var){
		this.formula = formula;
		this.var = var;
		length = formula.length();
		index = 0;
	}

	public int getIndex(){
		return index;
	}

	public Symbol getSymbol() throws ScanningException{
		char actualChar;
		Symbol sym = null;

		if(!(index < length)){
			return new Symbol(Symbol.EOF, null);
		}

		actualChar = formula.charAt(index);
		while(actualChar == ' ' && index < length){
			index++;
			actualChar = formula.charAt(index);
		}

		if(!(index < length)){
			return new Symbol(Symbol.EOF, null);
		}

		sym = isNumber();
		if(sym != null) return sym;

		index++;
		if(Character.isLetter(actualChar)){
			if(actualChar == var){
				sym = new Symbol(Symbol.VARIABLE, null);
			} else {
				sym = new Symbol(Symbol.CONSTANTVAR, Character.toString(actualChar));
			}

			if(actualChar == 'l'){
				actualChar = formula.charAt(index);
				if(actualChar == 'n'){
					index++;
					sym = new Symbol(Symbol.LN, null);
				}
			}
			
			return sym;
		}

		if(actualChar == '+'){
			return new Symbol(Symbol.PLUS, null);
		}

		if(actualChar == '-'){
			return new Symbol(Symbol.MINUS, null);
		}

		if(actualChar == '*'){
			return new Symbol(Symbol.ASTERISK, null);
		}

		if(actualChar == '/'){
			return new Symbol(Symbol.SLASH, null);
		}

		if(actualChar == '^'){
			return new Symbol(Symbol.ZIRKUMFLEX, null);
		}

		if(actualChar == '('){
			return new Symbol(Symbol.LPARENTHESIS, null);
		}

		if(actualChar == ')'){
			return new Symbol(Symbol.RPARENTHESIS, null);
		}

		throw new ScanningException("Unknown character \'" + actualChar + "\' found");
	}

	private Symbol isNumber() throws ScanningException{
		int startIndex = index;
		char actualChar = formula.charAt(index);
		Symbol out;
		boolean reachedEnd = false;

		if(!Character.isDigit(actualChar)) return null;
		else{
			if(actualChar != '0'){
				while(Character.isDigit(actualChar)){
					if(!(index < length)){
						reachedEnd = true;
						break;
					}
					actualChar = formula.charAt(index);
					index++;
				}
			}else{
				index = index + 2;
			}

			if(index < length){
				actualChar = formula.charAt(index - 1);
				if(actualChar == '.'){
					
					actualChar = formula.charAt(index);
					index++;
					if(!Character.isDigit(actualChar)) throw new ScanningException("Expected digit after character '.' ");
					while(Character.isDigit(actualChar)){
						if(!(index < length)){
							reachedEnd = true;
							break;
						}
						actualChar = formula.charAt(index);
						index++;
					}
				}
			}

			if(!reachedEnd) index--;
			out = new Symbol(Symbol.CONSTANT, formula.substring(startIndex, index)); 

			return out;
		}
	}	
}