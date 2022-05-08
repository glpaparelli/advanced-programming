# Functions in Python
- functions are first-class objects: in python basically anything is an object (this allows higher order)
- all functions return some value (possibly `None`, aka `void`)
- function call creates a new [[Python Namespaces]]: a call introduce a new scope that it is eliminated when the function returns
- parameters are passed by object reference: parameters are passed as values but since they are objects the parameter passing is done through reference
- functions can have optional keyword arguments: default values and more
- functions can take a variable number of args and kwargs

## Function Definition
**positional/keyword/default parameters**
```python
def sum(n, m):
	""" adds to values """
	return n + m

# "classic" par passing
>>> sum(3, 6)
9

# keyword parameter passing
>>> sum(m=5, n=3)
8

# we can also specify the default par value
def sum(n, m = 5):
	""" adds two values or increment by 5 """
	return n + m

>>> sum(3)
8
```

**arbitrary number of parameters (varargs)**: pass an arbitrary number of parameter to a function
```python
# arguments are put in a tuple
def print_args(*items):
	print(type(items))
	return items

# the actual parameters are inserted in a tuple passed to the
# function as we can see from the printed class
>>> print_args(1, "hello", 4.5)
<class 'tuple'>
(1, "hello", 4.5)

# --------------------------------------------------- #

# also this version, where the args are inserted in a dictionary
def print_kwargs(**items):
	print(type(items))
	return items 

>>> print_kwargs(a=2, b=3, c=3)
<class 'dict'>
{'a' : 2, 'b' : 3, 'c' : 3}
```

**function documentation**
The comment after the functions header is bound to the `__doc__` special attribute
```python
def my_function():
	""" Summary Line: do something ... """
	# aka skip
	pass

print(my_function.__doc__)
# prints: "Summary Line: do something ..."
```
## Higher-Order
As said functions can be passed as argument and returned as result. 
There are the known combinators `map` and `filter` which are predefined. 

Heavy use of iterators, which supports laziness: often when you expect a list as a value of return python creates an iterator object on that list so that it can compute and return elements one by one, this makes infinite containers possible

Lambdas supported for use with combinators
`lambda arguments: expression`
Lambdas in python have more limitation than [[Java Lambdas]] because in the body we can have only one expression, not a block with more than one statement.
This do not affect the language since we can pass functions to combinators ...

- `map` or `filter` have the same semantics and usage as we have seen in [[Haskell HO Functions]]

*filter:*
```python
>>> print(filter.__doc__)
filter(function or None, iterable) --> filter object
Return an iterator yielding those items of iterable for which 
function(item) is true. If function is None return the items
that are true
```

*example:*
```python
filter(lambda x : x % 2 == 0, [1, 2, 3, 4, 5, 6])
```

### More Modules for Functional Programming
- **functools**: higher order functions and operations on callable objects
- **itertools**: functions creating iterators for efficient looping, inspired by construct form Haskell and others
	- `count(10)`
	- `cycle("ABCD)"`
	- ...

## [[Python Decorators]]