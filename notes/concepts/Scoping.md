# Scoping as Fast as Possible
*A very fast and dumb reminder of scoping*

### Static Scoping
The compiler associates the corrisponding declaration with each utilization of the variable. 
We take the first variable that we find going upwards in the static blocks of the program, starting from the block that uses the variable

**Java uses Static Scoping**

### Dynamic Scoping
When we access the variable $x$  we retrieve at runtime the declaration of $x$ in the most recent activation record. 
This means that, unless the variable is delcared in the local block (in which case the bahaviour is the same as static scoping) we can't know statically which variable will be taken into account: maybe we will use an $x$ that is declared later in the code but that code was already in execution and that delcaration is the most recent one (the most recent activation record). 

With dynamic scoping we have dynamic type checking since the compiler can't know which variable we are referring at static time. 

**Python uses dynamic scoping** [[Python Scoping]]