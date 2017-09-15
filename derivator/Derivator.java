package derivator;

import derivator.util.*;
import derivator.tree.Tree;

public class Derivator{
	public static void main(String[] args) throws ScanningException, ParsingException{
		String formula;
		char var;
		int numbDerivation;
		int oLevel;
		Scanner scanner;
		Tree formulaTree;

		if(args.length != 4){
			throw new IllegalArgumentException("usage: <formula> <variable name> <number of derivation> <optimization level (0 - 1)>");
		}

		formula = args[0];
		var = args[1].charAt(0);
		if(args[1].length() != 1 || !Character.isLetter(var)){
			throw new IllegalArgumentException("variable name has to be only one letter");
		}
		if(var == 'e'){
			throw new IllegalArgumentException("variable name 'e' is reserved for the eulerian number");
		}

		numbDerivation = Integer.parseInt(args[2]);
		oLevel = Integer.parseInt(args[3]);

		scanner = new Scanner(formula, var);

		
		formulaTree = Parser.parse(scanner, var);
		optimization(oLevel, formulaTree);
		
		while(numbDerivation>0){
			formulaTree = formulaTree.derivate();
			optimization(oLevel, formulaTree);
			numbDerivation--;
		}
		
	}

	private static void optimization(int level, Tree unoptimized){
		System.out.println("Vor Level 1: " + unoptimized.toString());
		if(level > 0){
			unoptimized.optimizeLevel1();
			System.out.println("Nach Level 1: " + unoptimized.toString());
		}
		
		if(level > 1){
			unoptimized.optimizeLevel2();
			System.out.println("Nach Level 2: " + unoptimized.toString());
		}
		
		if(level > 2){
			unoptimized.optimizeLevel3();
			System.out.println("Nach Level 3: " + unoptimized.toString());
		}
		unoptimized.optimizeLevel0();
		System.out.println("Nach Level 0: " + unoptimized.toString());
		System.out.println();
	}
}