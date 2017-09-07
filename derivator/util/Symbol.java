package derivator.util;

public class Symbol{
	public static final int VARIABLE 		= 0;
	public static final int CONSTANTVAR		= 1;
	public static final int CONSTANT 		= 2;
	public static final int PLUS 			= 3;
	public static final int MINUS 			= 4;
	public static final int ASTERISK 		= 5;
	public static final int SLASH 			= 6;
	public static final int ZIRKUMFLEX		= 7; //^
	public static final int LPARENTHESIS 	= 8; //(
	public static final int RPARENTHESIS 	= 9; //)
	public static final int LN 				= 10; // ln
	
	public static final int EOF				= 1000;


	public final int type;
	public final String value;

	public Symbol(int type, String value){
		this.type = type;
		this.value = value;
	}

	public int getType(){
		return type;
	}

	public String getValue(){
		return value;
	}

	public boolean isPlusOrMinus(){
		if(type == PLUS || type == MINUS) return true;
		else return false;
	}

	public boolean isMinus(){
		if(type == MINUS) return true;
		else return false;
	}

	public boolean isMultOrDiv(){
		if(type == ASTERISK || type == SLASH) return true;
		else return false;
	}

	public boolean isDiv(){
		if(type == SLASH) return true;
		else return false;
	}

	public String toString(){
		switch(type){
			case VARIABLE:
				return value + " (variable)";
			case CONSTANTVAR:
				return value + " (constant variable)";
			case CONSTANT:
				return value + " (numerical constant)";
			case PLUS:
				return "+";
			case MINUS:
				return "-";
			case ASTERISK:
				return "*";
			case SLASH:
				return "/";
			case ZIRKUMFLEX:
				return "^";
			case LPARENTHESIS:
				return "(";
			case RPARENTHESIS:
				return ")";
			default:
				return null;
		}
	}
}