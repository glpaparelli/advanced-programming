# Python OOP
Python offer all the typical ingridients of the Object-Oriented Paradigm
- **encapsulation**: dividing the code into a public interface and a private implementation of that interface and supports for information hiding (some stuff is visible from the outside, some other is not)
- **inheritance**: the ability to create subclasses that contain specializations of their parent classes
- **polymorphism**: the ability to override methods of a class by extending it with a sublcass (inheritance) with more specific implementation

## Defining a Class 
A class is a blueprint for a new data type with specific internal attributes and internal function. 
To declare a class in python we have the following syntax:
```python
class className:
	# statement_1
	# statement_i
	# ...
```
where `statement_i` are assignments or function definitions

When we define a class a new namespace s created that contains all the names introduced in the statements of the class.
When the class definition is left a class object is created, bound to `className`, on which two operations are defined: attribute reference and class instantiation

## Creating a Class Instance
**A class instance introduces a new namespace nested in the class namespace**: by visibility rules all names of the class are visible (no qualified names needed)

If no constructor is present the syntax instantiation is `className()` and the new namespace is empty (no keyword `new` in python)

## Instance Methods
A class can define a set of instance methods, which are just functions. 
The first parameter `self` is implicit, just as in java there is always the implicit parameter `this`. 

```python
obj.methodName(arg_1, arg_2)
```
Here the parameter `self` is implicit. 

We can also pass explicitly the reference of the object on which we want to invoke the methdod with the following syntax
```python
className.methodName(obj, arg_1, arg_2)
```

Any function with at least one parameter defined in a class can be invoked on an specific instance of the class with the previous notation. 

## Constructors
A constructors is a special instance method with name `__init__`

```python
# here self is needed since it is a special instance method
def __init__(self, par_1, ..., par_n):
	# statement 1
	# statement 2
	# ...
```

- invocation: `obj = className(arg_1, arg_2, ...)`
- the first parameter `self` is bound to the new object
- the stamements typically initialize (thus create) "instance variables", i.e. names in the new object namespace: the body of the constructors adds new names to the empty namespace precedently bounded to self, these are the (new) fields of the object 

**Mind that we can have at most one constructor!**: *no overloading in python*
- no need to overload methods since we have optional parameters + `*args`, `**kwargs` 

## (Multiple) Inheritance
A class can be defined as a derived class:
```python
# single inheritance
class derived(base_class)
	# statements
```
`statements` are function definitions or assignment (that may override / obsucre inherited stuff) and they can access names in the namespace of the superclass
The namespace of `derived` is nested in the namespace of `base_class` and uses it as the next non-local scope to resolve names. 
All instance methods are automatically virtual: lookup starts from the instance (namespace) where they are invoked

**Python supports Multiple Inheritance**
```python
class derived(base_1, ...., base_n)
	# statements
```
the problem of the priorities of inheritance is solved by using the order on which superclasses are written

## Encapsulation
- private instances variables (not accessible except from inside an object) do not exist in python
- convention: a name prefixed with underscore is treated as non-public part of the API. It should be considered an implementation detail and subject to change without notice. This is pure pragmatics

### Name Mangling 
Sometimes class-private members are needed to avoid claches with names defined by subclasses. Limited support for such a machanism called **name mangling**
Any `name` with at least two leading underscored and at most one trailing underscore is replaced with `__class__name` whew `class` is the current class name
- **name mangling is a sintattical (kinda dumb) solution**

## Static Methods and Class Methods
Static methods are simple functions defined in a class with no `self` argument preceded by the `@staticmethod` decorator. 
`@staticmethods` decorator is not mandatory but allows you to invoke static methods even with dot notation. 

These methods are defined inside a class but they cannot access instance attributes and methods: no `self` reference.

**benefits of static methods:** they allow subclasses to customize the static methods with inheritance. Classes can inherit static methods without redefining them.

## Iterators
An iterator is an object which allows a programmer to traverse through all the elements of a collection (iterable object), regardless of its specific implemention. In python they are used implictly by the `for` construct. 

**Python iterator objects required to support two methods:**
- `__iter__`: returns the iterator object itslef. This is used in `for` and in `in` statements 
- `next`: it returns the next value from the iterator. If there is no items to return then it should raise a `StopIteration` exception

**Remember that an iterator object can be used only once**. It means that after it raises `StopIteration` once it will keep raising the same exception: wanna iterate again? use a new iterator. 

### Generators
Generators are a simple and powerful tool for creating iterators. They are written like regular functions but use the `yeld` statement whenever they want to return data.

Each tume the `next()` is called the generator resumes where it left-off (it remembers all the data values and which statement was last executed)

**Anything that can be done with generators can also be done with class based iterators, but not viceversa**
- class based iterators = iterator implemented as class

What makes generators so compact is that the `__iter__()` and `next()` methods are created automatically. 
In addition to that when a generators terminate they automatically raise `StopIteration`. 
In combination these features make it easy to create iterators with no more effort than writing a regular function

```python
# generator definition
def reverse(data): 
	for index in range(len(data)-1, -1, -1):
		yield data[index]
```
```python
>>> for char in reverse('golf'):
		print(char)
f 
l
o
g
```

## Miscellaneous
**duck typing:** do not infer the type of a function, just invoke it with any possible parameters and if it works then it is the right type. Python has type errors at runtime since there are no static type checks and that may cause functions to not work
**overloading**: simulated with scope nesting of subclasses
