DIGIT				=	1 | 2 | 3 | 4 | 5| 6 | 7 | 8 | 9 .

LETTER				=   a | b | .... | y | z | A | B | .... | Y | Z .

NUMBER				=   ( DIGIT { DIGIT | "0" } | "0" ) [ "." ( DIGIT | "0" ) { DIGIT | "0" }] .

VARIABLE			= 	LETTER .



FORMULAR			= 	EXPRESSION

EXPRESSION			= 	TERM { ( "+" | "-" ) TERM} .

TERM				= 	UNARY { ( "*" | "/" ) UNARY} .

UNARY				= 	[ "+" | "-"] ( FACTOR | "ln(" EXPRESSION ")" ) .

FACTOR				=	( VARIABLE | NUMBER | "(" EXPRESSION ")" ) [ "^" ( VARIABLE | NUMBER | "(" EXPRESSION ")" ) ] .