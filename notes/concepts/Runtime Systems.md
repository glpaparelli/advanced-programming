# Runtime Systems
Every programming language defines an **execution model**: "how" the program is executed (examples of execution models are turing machines, lambda calculus, stack machines (as the [[Java Virtual Machine]]), and many more)

A **runtime system** implements (part of) such execution model, providing *support* to the the abstract machine during the execution of the programs. 
Mind that **runtime support** is needed both by interpreted and compiled programs, even if typically less by the latter

***The runtime system provide runtime support***

The **runtime system** can be made of
- code of the exeucting program generated by the compiler
- code running in other threads/processes during program execution (in Java for example we have the garbace collector, ...)
- language libraries (external functionalities of our abstract machine)
- operating systems functionalities (system calls are part of the RT system)
- the interpreter / virtual machine itself 

The **runtime support** is needed for
- **memory management**
	- stack management: push/pop of activation records
	- heap management: allocation, garbage collection
- **input/output**
	- interface to file systems or network sockets or whatever
- **interaction with the** **runtime envorioment** (all those data needed by the program in execution that are not part of the program itself, as for OS buffers, external files, people via keyboard, ...)
- **parallel execution** via threads/tasks/processes
- **dynamic type checking and dynamic binding**
- ...
- **debugging**: to debug you need the current values of var, breakpoints and other stuff that are available only at runtime
- **code optimization**: JIT and such
- **verification and monitoring** of properties that we want to enforce


