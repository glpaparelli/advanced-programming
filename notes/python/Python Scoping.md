# Python Scoping
**A scope is a textual region of a python program where a [[Python Namespaces]] is directly accessible**, i.e. reference to a name attempts to find the name in the namespace. 

Scopes are determined staticallt but they are used dynamically e.g. with the declaration of a function, class or instance of a class.

**During execution at least three namespaces are directly accessible**, searched in the following order:
- the scope containing the local names
- the scopes of any enclosing functions, containing non-local but also non-global names
	- in a function that is declared in the body of another function we can see all the variables of the outer function
	- *observation 1:* the vars of the inner function obscure the outer variables because there is an ordering of accessing the scopes
	- *observation 2:* the function is declared in the body of an outer function != invoked. The scope is determined at static time used at runtime
- the next-to-last scope containing the current module's global names
- the outermost scope is the namespace containing built-in names 

**Assignments to names go in the local scope**: we know that there is a not explicit declaration, a name is added in a namespace at assignment time. 
In particular the assignment adds a new name in the local scope.
*observation 1*: if we access a name by reading we search upwards in the scopes starting by the nearest (local) scope
*observation 2*: if we assign a value to a name and that name is already used in an enclosing scope **there is no update** in the enclosing scope, a new name is created in the local scope

**Non-local variables can be accessed using nonlocal or global scopes**

### Scoping Drawback
In many languages scopes give you power to write readable and simple code. 
In python you only get two single scopes: global and function. Just handling these scopes is painfull

*example 1:*
```python
def test():
	for a in range(5):
		b = a % 3
		print(b)
	print(b) # A
```
the `A` statement is legit since `b` is declared in the scope of `test`, the `for` do not introduce a new scope! This in a language like Java would give error since `b` would be unknown

*example 2:*
```python
def test(x):
	print(x) # A
	for(x in range(5)):
		print(x) # B
	print(x) # C
```
```python
>>> test("hello")
```
- `A` prints "hello"
- `B` prints 1, 2, 3, 4 since the binded value is modified at every iteration
- `C` prints 4, the latest assignment to the name `x`, again: blocks do not define a new scope!

In Java we would have 
- hello
- 1,2,3,4
- hello

## Closures
Python supports closures: even if the scope of the outer function is reclaimed on the return the non-local variables referred to by the nested function are saved in its attribute `__closure__`