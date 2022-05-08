# Python Namespaces
**A namespace is a mapping from names to objects**: typically implemented as a dictionary. 
*namespaces are not dictionaries, they are implmented as dictionaries*

*Examples of namespaces*
- **builtins**: pre-defined fucntions, expection names, ...
	- created at interpreter's start-up
- **global names of a module**:
	- created when the module definition is read (read aka import of a module)
- **local names of a fucntion invocation**: every invocation creates a namespace that contains all the top level names used in the function. 
	- created at invocation, deleted at return
- **class and objects** defines new namespaces [[Python OOP]]

The name `x` of a module `m` is an attribute of `m`, accessible with the qualified name `m.x`
