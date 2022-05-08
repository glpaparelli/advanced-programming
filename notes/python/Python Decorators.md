# Python Decorators
**A decorator is any callable Python object that is used to modify a function, method or class defintion.** 
We only see decorators for functions.

To the decorator is passed the original object being defined and returns a modified object, which is then bound to the name in the definition. 
This means that in the next invocation the object will have a "new semantic". 

Function decorators exploit Python [[Python Functions#Higher-Order]]
- passing functions as argument
- nested definition of functions
- returning functions

Decorators are widely used in python (system) programming. 

## Intuition
The basic idea is to wrap a function:
```python
# function "funct" as argument, 
# the function we want to decorate
def my_decorator(func): 
	# defines an inner function
	def wrapper(): 
		print("something happens before the function")
		# invoke the passed function
		func() 
		print("something happens after the function")
	# return the inner function
	return wrapper
```

*example:*
```python
def say_hello():
	print("hello!")

# sort of reassining the name say_hello
say_hello = my_decorator(say_hello)
```
```python
# the decorator will be invoked
>>> say_hello()
"something happens before the function"
"hello!"
"something happens after the function"
```

**Syntatic Sugar**: in the previous example (definiton of `say_hello()`) we typed `sat_hello` three times (definition, name to assign, parameter). 
This is kinda heavy to write. 
As an alternative we can use the following equivalent syntax
```python
# decorator is defined as shown before
@my_decorator
def say_hello(): 
	print("hello!")
```
- mind that the syntax is similar to the [[Java Annotations]] but they are totally different things.

**Observation:** after annotating a function, unless you have made an alias, you loose the reference to the original function!

### The do_twice Decorator
```python
def do_twice(func):
	def wrapper_do_twice():
		func()
		func()
	return wrapper_do_twice

@do_twice
def say_hello():
	print("hello")
```
```python
>>> say_hello()
"hello"
"hello"
```

The problem in this case is that this do not work with parameters!
```python
@do_twice
def echo(str)
	print(str)
```
```python
>>> echo("hi")
TypeErr: wrapper_do_twice() takes 0 pos args but 1 was given

>>> echo()
TyoeErr: echo() missing 1 required positional argument 'str'
```

#### do_twice for Functions with Parameters
Decorators for functions with parameters can be defined exploing `*args` and `**kwargs`
```python
def do_twice(func):
	def wrapper_do_twice(*args, **kwargs):
		func(*args, **kwargs)
		func(*args, **kwargs)
	return wrapper_do_twice
```

Now `do_twice` can be used on any function, wheater is has parameters or not
```python
@do_twice
def echo(str):
	print(str)
```
```python
>>> echo("hi")
"hi"
"hi"
```

## General Structure of a Decorator
Besides passing arguments the wrapper also forwards the result of thedecorated function. 
Support for introspection redefining `__name__` and `__doc__`

```python
import functools
def decorator(func):
	# supports introspection: preserve func
	@functools.wraps(func)
	def wrapper_decorator(*args, **kwargs):
		# do something before
		value = func(*args, **kwargs)
		# do something after
		return value
	return wrapper_decorator
```

## Measuring Running Time
```python
import functools
import time

def timer(func):
	""" print the runtime of the decorated function """
	@functools.wraps(func)
	def wrapper_timer(*args, **kwargs):
		t_start = time.perf_counter()
		value = func(*args, **kwargs)
		t_end = time.perf_counter()

		runtime = t_end - t_start
		print("{func.__name__!r} in {runtime:.4f} secs")
		return value
	return wrapper_timer
```

## Other uses of Decorators
- **debugging**: prints argument list and result of calls of decorated functions
- **registering plugin**: adds a reference to the decorated function without changing it
- `@staticmethod` and `@classmethod` make a function invocable on the class name or an object of the class