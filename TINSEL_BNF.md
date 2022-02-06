# TINSEL Definition in Backus–Naur form (BNF)
# TINSEL v1.0

## Program
```
<program> ::= <prog header> [ <var declarations> ] [ <fun declarations> ] <main block> <prog end>`
<program header> ::= program <identifier>
<variable declaration> ::= var <identifier> [ = <value> ]
<function declaration> ::= fun <identifier> ( ) <block>
<main block> ::= main <block>
<program end> ::= endprogram
```

## Statements
```
<block> ::= { <statement> * }
<statement> ::= <block> | <if> | <while> | <repeat> | <for> | <break> | <continue>
                <return> | <read> | <print> | <assignment> | <function_call> | null [ ; ]
<if> ::= if ( <b-expression> ) <block> [ else <block> ]
<while> ::= while ( <b-expression> ) <block>
<repeat> ::= repeat <block> until ( <b-expression> )
<for> ::= for ( <identifier> = <expression> [ down ] to <expression> [ step <expression> ] ) <block>
<break> ::= break
<continue> ::= contnue
<return> ::= return <b-expression>
<assignment> ::= <identifier> = <b-expression>
<funcrion_call> ::= <function_name> ( )
<read> :: = read <identifier> [ , <identifier> ] *
<print> ::= print <b-expression> [ , <b-expression> ] *
```

## Control structures

```
<if> ::= if ( <b-expression> ) <block> [ else <block> ]
<while> ::= while ( <b-expression> ) <block>
<repeat> ::= repeat <block> until ( <b-expression> )
<for> ::= for ( <identifier> = <b-expression> to <b-expression> ) <block>
```

## Expressions
```
<b-expression> ::= <b-term> [ <orop> <b-term> ] *
<b-term> ::= <not-factor> [ <andop> <not-factor> ] *
<not-factor> ::= [ <notop> ] <b-factor>
<b-factor> ::= <b-literal> | <b-variable> | <relation>
<relation> ::= <expression> [ <relop> <expression ]
<expression> ::= <term> [ <addop> <term> ] *
<term> ::= <signed factor> [ <mulop> factor ] *
<signed factor> ::= [ <addop> ] <factor>
<factor> ::= <integer> | <variable> | <function_name> ( ) | ( <expression> )
```
Note: any Boolean expression can also be Integer expression and vice versa (same as C).
The compiler will start looking for a Boolean expression and may "settle" for a numeric one as it goes down the list.
**Important:** *Boolean expressions do not allow parentheses* - this is what makes this specific logic work (Chapter 6)

## String expressions
```
<s-expression> ::= <s-term> [ + <s-term> ]
<s-term> ::= <string-literal> | <string-variable>
<string-literal> ::= " [ <alpha-character> * ] "
```

## Operators
```
<orop> ::= '||' | '^^'
<andop> ::= '&&'
<notop> ::= '!'
<relop> ::= '==' | '!=' | '>' | '<' | '<=' | '>='
<addop> ::= '+' | '-'
<mulop> ::= '*' | '/'
```

## Comments
```
<block-comment> ::= /* <comment> */ | /*-- <comment> */
<inline-comment> ::= // <comment> <newline> | //-- <comment> <newline>
```