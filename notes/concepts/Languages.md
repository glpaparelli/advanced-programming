# Languages, Abstract Machines and Compilation Schemes
#### Definition of Programming Languages
A programming language is defined by three components
- **Syntax:** the syntax is concerned with the form of programs: how expression, commands and other constructs must be arranged to make a well-formed program
- **Semantics:** the semantic is concerned with the meaning of (well-formed) programs: how a program may be expected to behave when executed. Mind that a program that is not well-formed (with wrong syntax) has no meaning. The semantic can be defined
	- informally through natural languages (which may leave ambiguities)
	- formally with logical or matemathical language (often on subsets of the languages since it is very complicate)
		- denotational 
		- operational
		- axiomatic
- **Pragmaitcs:** the pragmatic is concerned with the way in which the programming language is intended to be used in practice

#### Programming Paradigms 
A *paradigm* is a style of programming, charcterized by a particular selection of key concepts and abstractions 
- **imperative programming:** variables, commands, procedures, ...
- **object-oriented programming:** objects, classes, methods, ...
- **concurrent programming:** processes, communication, ...
- **functional programming:** values, expressions, functions, higher order, ...
- **logic programming:** assertions, relations, ...

Mind that classificating the languages by paradigm can be misleading. Paradigms don't create partitions of languages

#### General Structure of the Interpreter
*The classic fetch-decode-execute loop*
1. start
2. fetch next instruction
3. decode: read opcode and do things based on the semantics of the opcode
4. fetch operands (if any)
5. choose 
	1. execute $op_1$
	2. execute $op_2$
	3. ...
	4. execute $halt$: stop the execution
6. store the result
7. go back to **2**

#### Implementation of a Programming Language
Every language $L$ implictly defines an *Abstract Machine* $M_L$ that have $L$ as machine language. 
Implementing $M_L$ on an existing host machine $M_O$ (by compilation, interpretation or both) makes the programs written in $L$ executable

*Given a programming language $L$ an abstract machine $M_L$ for $L$ is a collection of data structures and algorithms which can perform the strorage and execution of programs written in $L$*

##### Structure of an Abstract Machine
- **Memory**
	- **Programs:** a program is a specific kind of data in the memory
	- **Data:** the data of the program (inputs, working data, ...)
- **Interpreter**
	- Operations and Data Structures for
		- **Primitive data processing:** executes easy primitive operations
		- **Sequence control:** handles the execution flow (returns, conditional jumps, ...)
		- **Data Transfer Control:** handles parameters passing and values returns
		- **Memory Management:** how and where to store data 

Viceversa each abstract machine $M$ defines a language $L_M$ including all programs which are executed by the interpreter of $M$. 
Components of $M$ corresponds to components of $L_M$
- **Primitive Data Processing** -> **Primitive Data Types**
	- given the primitive data operations of $M$ we derive the primitive types of $L$
- **Sequence Controls** -> **Control Structures**
	- given the sequence controls we derive which branch constructors $L$ supports
- ...

*An example of abstract machine that we will see later on is the [[Java Virtual Machine]]

##### Implementing an Abstract Machine
Each AM can be implemented in hardware or firmware but for high level languages this is not convenient
- an istruction in an high level programming language translate to hundreds of instruction in assembler for example
- build a processor that implements the [[Java Virtual Machine]] (the abstract machine of Java) is an economic and ingegneristic nightmare (you'd have to change the hardware on every Java update)

Tipically an abstract machine $M$ is implemented over a host machine $M_O$ which we assumed as already implemented.
The components of $M$ are realized using data structures and algorithms implemented in the machine language $M_O$ .

There are two main cases:
- the interpreter of $M$ coincides with the interpreter of $M_O$
	- $M$ is an extension of $M_O$
	- other compontes of the machines may differ
- the interpreter of $M$ is different from the interpreter of $M_O$
	- $M$ is interpreted over $M_O$
	- other componetns of the machines may coincide 

##### Pure Interpretation 
$L$ is a high level programming language 
$M_L$ is the abstract machine for $L$
$L_O$ is the machine language of $M_O$ 
$M_O$ is the host machine (which is implemented and executable)

- program written in $L$ + input data
	- interpreter of $L$ written in $L_O$ executed on $M_O$ 
		- output

*$M_L$ is interpreted over $M_O$ :* this solution is not very efficient since there can't be code optimizations (the code is read only when executed, everything is at runtime)

##### Pure Compilation 
$L$ is a high level programming language 
$L_O$ is the machine language of $M_O$ 
$M_O$ is the host machine (which is implemented and executable)

Programs written in $L$ are translated into equivalent programs written in $L_O$
The abstract machine $M_L$ that $L$ implicitly defines is not implemented at all

- program written in $L$ + input data
	- compiler from $L$ to $L_O$ (mind that the compiler is a program that may be written in a third language $L_A$ executable on an abstract machine $M_A$, this is called cross-compilation)
		- program written in $L_O$ executed on $M_O$ 
			- output

*$L$ is compiled to $L_O$:* the execution is very efficient but we produce a lot of code, both $M_A$ and $M_O$ have to be implemented and at some point we have two equivalent code to store (the program written in $L$ and the translated version in $L_O$). 
Futhermore an instruction of $L$ could be implemented with hundreds of instructions of $L_O$ 

With pure compilation is possible to do static optimizations of the code, all the possible decisions that can be taken at compile time are taken at compile time. 

##### Compilation vs Interpretation 
Compilers can fix decisions that can be taken at compile time to avoid to generate code that makes the same decisions at runtime
- **type checking**: static type checking alleviates the necessary runtime type checking
- **static allocation:** at execution time it is already known where the vars are allocated
- **static linking:** handle the libraries way more efficiently
- **code optimization:** dead code elimination, ...

*Generally Compilation leads to better performance while Interpetation facilitates interactive debugging and testing*

##### Compilation + Interpretation
A two step process where the code is first compiled into an intermediate language and then the intermediate language is interpreted. 
A notable example of this schema is Java and the [[Java Virtual Machine]]

- program written in $L$ (Java) + input
	- compiler from $L$ to an intermediate language $L_I$  (bytecode) where the compiler can be written in any language $L_A$ and it is executed on an abstract machine $M_A$ 
		- program written in $L_I$ (bytecode) is interpreted by the $L_I$ interpreter (JVM) written in $L_O$ and executed on $M_O$ 
			- output

This mixed schema with intermediate abstract machine have notable advantages
- **portability:** compile the Java source, distribute the bytecode and execute it on any platform equipped with the [[Java Virtual Machine]]
- **interoperability:** for a new language $L$ just provide a compiler that translate to bytecode and then let it run on the [[Java Virtual Machine]] exploiting all the libraries&co
