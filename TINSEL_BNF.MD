# TINSEL Definition in Backus–Naur form (BNF)

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
<block> ::= { [ <statement> ] * }
<statement> ::= <block> | <if> | <while> | <repeat> | <for> | <break> | <return> | <assignment>
<if> ::= if ( <b-expression> ) <block> [ else <block> ]
<while> ::= while ( <b-expression> ) <block>
<repeat> ::= repeat <block> until ( <b-expression> )
<break> ::= break
<return> ::= return <b-expression>
<assignment> ::= <identifier> = <b-expression>
```

## control structures

```
<if> ::= if ( <b-expression> ) <block> [ else <block> ]
<while> ::= while ( <b-expression> ) <block>
<repeat> ::= repeat <block> until ( <b-expression> )
<for> ::= ( <identifier> = <b-expression> to <b-expression> ) <block>
```

## Expressions
```
<b-expression> ::= <b-term> [ <orop> <b-term> ] *
<b-term> ::= <not-factor> [ AND <not-factor> ] *
<not-factor> ::= [ NOT ] <b-factor>
<b-factor> ::= <b-literal> | <b-variable> | <relation>
<relation> ::= <expression> [ <relop> <expression ]
<expression> ::= <term> [ <addop> <term> ] *
<term> ::= <signed factor> [ <mulop> factor ] *
<signed factor>::= [ <addop> ] <factor>
<factor> ::= <integer> | <variable> | ( <expression> )
```
Note: any Boolean expression can also be Integer expression and vice versa (same as C).
The compiler will start looking for a Boolean expression and may "settle" for a numeric one as it goes down the list.