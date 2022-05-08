# Python Introduction
**Python Main Features:**
- **dynamic typing**:  we don't declare the type of a variable, the type is inferred from the value assigned to to it. This is done uniquely at runtime
- **indentation instead of braces**
- **several sequence types**: we have several high level primitive types, such as 
	- lists (mutable), tuples (immutable), Strings, ...
	- dictionaries (hash maps): kinda associative arrays, impemeneted with hashing, to each key we can have an unique value (unless as value you use another collection)
- **powerful subscripting (slicing)**: slicing operator on sequence types, `t[1:4]` generates a new touple based on the elements of `t` and that contains the elements of index 1 to 4
- **object oriented**: simple object system, almost any entity is either an object or a class (eg: functions are objects)
- **higher-order functions**: support to HO functions through `@decorators`
- **flexible signatures**: support to optional function parameters and default parameter values
- **exception as in Java**
- **Iterators and Generators**: the `for` is possible only as enanched for (as `forEach` in Java using [[Java Iterators]])

**Pragmatics: Why Python**
- "pythonic" style is very concise: no explicit typing and high-level primitive types greatly simply the code
- powerful but unobtrusive object system: every value is an object
- powerful collection and iteration abstractions: dynamic typing makes [[Polymorphism]] easy, the type is inferred at runtime, basically anything is generic

Mind that *dynamic typing is both a good and a bad thing*. Variables come into exist ence when first assigned to. A variable can refer to an object of any type. In diffrenet blocks the same vairable can refer to different types! All types are (almost) treated in the same way. The main drawback is that *type errors are only caught at runtime*

## Useful Commands
- `help()`: enters python interactive help utility
- `help(arg)`: prints docs about `arg` (`arg` may be a function!)
- `type(arg)`: prints the type of `arg`
- `dir()`: returns the sorted list of strings containing all names defined in a module, a class or an object:
	- `dir(sys)` or `dir(moduleName)` returns all the names defined in the module name
	- `dir(className)` returns all the names defined in the class

## Import and Modules
Programs will often use classes & functions defined in another file. 
A **python module** is a single file with the name of the module and with the extension `.py`. 
Modules can contain many classes and functions, the access is through `import`

**Where does Python looks for modules files?**
- the list of directories where Python looks: `sys.path`
- when python starts up this variable is initialized from the `PYTHONPATH` environment variable
- to add a directory of your own: `sys.path.append('/my/new/path')`
	- operating system dependant

### Deining Modules
**Modules** are files containing definitions and statements. A module defines a *new namespace*
*Modules can be organized hierarchically in packages*
```python
#File fibo.py - Fibonacci numbers module

# write Fibonacci series up to n
def fib(n): 
	a, b = 0, 1
	while b < n: 
		print(b, end='')
		a, b = b, a+b
	print()

# return Fibonacci series up to n
def fib2(n): 
	result = []
	a, b = 0, 1
	while b < n:
		result.append(b)
		a, b = b, a+b
	return result
```

### Importing a Module 
```python
>>> import fibo # impotrs module from local file 
'fibo.py'

# we can also import functions from modules, that 
# functions are inglobated in the current namespace 
>>> from fibo import fib, fib2
# or, equally
>>> from fibo import *
```

## Python Basics
*A code sample:*
```python
x = 34 - 23
y = "Hello"
z = 3.45

# : starts a block, the body has to be indented
if z == 3.45 or y == "Hello": 
	x = x + 1
	y = y + "World" # string concat

print(x)
print(y)
```

**Enough to Understand the Code:**
- **indentation matters to the meaning of the code**
	- block structure indicated by indentation
- **the first assignment to a variable creates the variable**
	- variables types don't need to be declared
	- python figures out the variable types on its own 
- **assignmenet uses = and comparison uses \=\=**
- **for numbers +, -, \*, / % as ecpcted**
	- special use of `+` for string concatenation
	- special use of `%` for string formatting (as `printf` in C)
- **logical operators are words: `and`, `or`, `not`**
- **simple printing can be done with `print()`**

### Basic Datatypes
- **Integers (default for numbers)**
	- `z = 5 // 2`: the answer is 2, integer division
- **Floats**
	- `x = 3.456`
	- `k = 5 / 2`: the answer is 2.5
- **Strings**
	- can use "" or '' to specify: "abc" is the same as 'abc'
	- unmatched can occour within the string: "matt's"
	- use triple double-quotes for multi-line strings or strings than contain both ' and '' inside of them
		- """a'b"c"""

### Whitespaces
- use a newline to end a line of code
- use `\` when you must go to the next line prematurely
- no use of braces. indentation makes blocks
- often a colon `:` appears at the start of a new block

### Comments 
- the comment start with `#`
- can include a "documentation string" a the first line of any new function or class that you define
	- it's good style to use it (they can even be statically used by tools, compilers, ...)
```python
def my_function(x, y):
"""this is the docstring, ..."
#function code
```

### Assignment
- Binding a variable in python means setting a name to hold a reference to some object
	- assignment creates references, not copies
- a variable is created the first time it appears on the left side of an assignment expression
	- `x = 3`
- an object is deleted (by the garbage collector) once it becomes unreachable
- names in python do not have an intrinsic type, objects have types 
	- python determines the type of the reference automatically based on what data it assigned to it

It is possible to do multiple assignment in one line, mind that the assignments are done in parallel
```python
x, y = 2, 3
```

### Sequence Types
**Tuples**
A simple immutable ordered sequence of items
- immutable: a touple can't be modified once it is created
- items can be of mixed types, including collection types
```python
# example of tuple
tpl = (23, "abc", 4.5, (2, 4). "def")
```

**Strings**
- strings are immutable: trying to change the value of a string gives an error
- strings are not lists or touples (eg. in Haskell strings are list of chars)
```python
# example of string
str = "Hello World"
```

**Lists**
- *mutable* ordered sequence of items of mixed types
- can have non omogeneous elements
```python
# example of list
ls = ["abc", 34, 4.34, 23]
```

We can access individual memembers of a tuple/list/string using square bracket array notation
```python
>>> str = "hello"
>>> str[1]
'e'
```

Lists are mutable, we can change lists in place: no other list is generated in memory. 

**There are several operations list exclusive**
```python
#append adds at the end
>>> li = [1, 11, 3, 4, 5]
>>> li.append('a')
>>> li
[1, 11, 3, 4, 5, 'a']

# insert specify the index
>>> li.insert(2, 'i')
>>> li
[1, 11, 'i', 3, 4, 5, 'a']

# the extend method
>>> li
[1, 11, 'i', 3, 4, 5, 'a']
>>> li.extend([9, 8, 7])
>>> li
[1, 2, i, 3, 4, 5, 'a', 9, 8, 7]

# extend vs +
# + creates a fresh new list (with a new memory reference)
# extend takes a list as argument, operates in place

>>> li = ['a', 'b', 'c', 'b']
# index of first occurrence
>>> li.index('b')
1

# count number of occurrences
>>> li.count('b')
2

# remove first occurrence
>>> li.remove("b")
>>> li
['a', 'c', 'b']

# reverse the list in place
>>> li = [5, 2, 6, 8]
>>> li.reverse()
>>> li
[8, 6, 2, 5]

# sort the list in place
>>> li.sort()
>>> li
[2, 5, 6, 8]
# we can pass a comparator
li.sort(some_function)
```

*Lists slower but more powerful than tuples*
- lists can be modified and they have lots of handy operations we can perform on them
- tuples are immutable and have fewer features

To convert between tuples and lists we have the `list()` and `tuple()` functions
```python
ls = list(tpl)
tpl = tuple(ls)
```

**Negative Indices**
```python
>>> t = (23, "abc", 4.5, (2, 3). "def")

# positive index: count from the left starting with zero
>>> t[1]
"abc"

# negative counting: count from right, starting with -1 
>>> t[-3]
4.5
```

#### Slicing
```python
>>> t = (23, "abc", 4.5, (2, 3). "def")
```

**Slicing** consists in returning a copy of a subset. 
It returns a copy of the container with a subset of the original members. Start copying at the first index and stop copying before the second index.

```python
>>> t[1:4]
("abc", 4.5, (2,3))

# can also use negative indices
>>> t[1:-1]
("abc", 4.5, (2,3))

# also an optional argument to set jumps (2)
>>> t[1:-1:2]
("abc", (2,3))

# omit the fisrt index to make a copy from the beginning
>>> t[:2]
(23, "abc")

# omit the second argument to make a copy untill the end
>>> t[2:]
(4.5, (2,3), "def")

# make a copy of an entire sequence
>>> t[:]
t = (23, "abc", 4.5, (2, 3). "def")
```

Remember that **slicing makes a new copy**
```python
# 2 names refer to one reference
# changing one affects both
>>> list2 = list1

# two independent copies, two references
>>> list2 = list1[:]
```

### The 'in' and '+' Operators
**in**: boolean test whether a values inside a collection (aka container in python)
```python
>>> t = [1, 2, 3, 5]
>>> 3 in t
True

# works on strings: checks for substrings 
>>> a = "abcde"
>>> 'c' in a
True
```
Mind that the `in` keyword is also used in the syntax of for loops and list comprehensions, be careful. 

**+**: produces a new tuple/list/string whose value is the concatenation of its arguments. 
In python concatenation works not only on strings
```python
# concatenation of tuples
>>> (1, 2, 3) + (4, 5, 6)
(1, 2, 3, 4, 5, 6)

# concatenation of lists
>>> [1, 2, 3] + [4, 5, 6]
[1, 2, 3, 4, 5, 6]

# concatenation of strings
>>> "hello" + "world"
"Hello World"
```

### Sets
The empty set is defined with `set()`, indexing is not supported and we can have mixed types, and obviously duplicates are removed. Obviously the `in` operator works on sets. 
```python
>>> a = set("abracadabra")
>>> b = set("alacazam")
>>> a
{"a", "r", "b", "c", "d"}

# we have the classic set operations

# letters in a but not in b
>>> a - b

# letters in a or b or both
>>> a | b

### letters in both a and b
>>> a & b

### letters in a or in b but not in borth
>>> a ^ b
```

### Dictionaries: A Mapping Collection Type
**Dictionaries store a mapping between a set of keys and a set of values**
- **keys** can be any of immutable hashable type: they cannot contain mutable components and they have to be unique
- **values** can be any type
Values and keys can be of different types in a single dictionary. 

```python
>>> d = {"user":"bozo", "pswd": 1234}
>>> d["user"]
"bozo"
>>> d["pswd"]
1234

>>> d["bozo"]
>>> error
```

- dictionaries work by hashing
- assigning to an existing key changes the value
- assigning to a non-existing key adds a new pair
- dictionaries are unordered: new entry might appear anywhere in the output

```python
>>> d = {"user":"bozo", "p":1234, "i":34}

# remove, mind that del is a function
# mind that del also works on lists
>>> del d["user"]
>>> d 
{"p": 1234, "i":34}

# remove all
>>> d.clear()
>>> d
{}
```

There are several accessor methods
```python
>>> d = {"user":"bozo", "p":1234, "i":34}

# list of current keys
>>> list(d.keys())
["user", "p", "i"]

# list of current values
>>> list(d.values())
["bozo", 1234, 34]

# list of item tuples
>>> list(d.items())
[("user", "bozo"), ("p", 1234), ("i", 34)]

# when accessing a dictionary as a list, the keys are returned
>>> list(d)
["user", "p", "i"]
```

#### Using Dictionaries
Write a program to compute the frequency of the words of a string read from the input. The output should print the words in increasing alphanumerical order:
```python
freq = {}
# input return an input file
line = input()

# line.split() split by default " "
for word in line.split():
	# freq initially is an empty dictionary where the key
	# will be the words and the values will be the frequencies
	freq[word] = freq.get(word, 0) + 1

# order keys alphanumerical
words = list(freq.keys())
words.sort()

for w in words:
	print ("%s:%d" % (w, freq[w]))
```

### Boolean Expressions
`True` and `False` are constants.
Other values are treated as equivalent to either `True` or `False` when used in conditionals
- **false**: zero, `None`, empty containers
- **true**: non-zero numbers, non empty objects

Classic comparison operartors: `==`, `!=`, `<`, ...
- `X == Y`
	- X and Y have the same value (like `equals` in Java)
- `X is Y`
	- X and Y refer to the exact same object (like Java `==`)

### Conditional Expressions
inline if: `x = true_value if condition else false_value`
In python the conditional is lazy:
- first python evaluates the condition
- if `True` then `true_value` is evaluated and returned
- if `False` then `false_value` is evaluated and returned


### Control Flow
`if` and `while` as expected. 
- `break`: can be used inside the loop to leave the while loop entirely
- `continue`: can be inside a loop to stop processing the current iteration of the loop and immediately go on the next one

`assert` as expected, make sure that something is true during the course of a program: if the condition is false the program throws an exception

#### For Loops
The **for each** is python's only form of for loop.
A for loop steps through each of the items in a collection type, or any other type of object which is "iterable"
```
for <item> in <collection>:
	<statement>
```
- `<collection>` is a list or a tuple then the loop steps through each element of the sequence
- if `<collection>` is a string then the loop steps through each character of the string
- `<item>` can be more complex than a single variable name: if the elements of `<collection>` are themselves collections, then `<item>` can match the structure of the elements

We often want to write a loop where the variables ranges over some sequence of numbers. The function `range()` returns a list of n numbers from 0 up to n but not included in the passed number
- `range(5)` returns `[0, 1, 2, 3, 4]`

So we can write:
```python
for x in range(5)
	print(x)

# also the variant range(start, stop[, step])
```

`range()` should not be abused. 
Do not use `range()` to iterate over a sequence solely to have the index and elements available and elements available at the same time:
```python
# dont
for i in range(len(myList)):
	print(i, myList[i])

# instead do:
# (enumerate return a list of pair)
for (i, item) in enumerate(myList): 
	print(i, item)
```


### List Comprehensions
A powerful feature of the python language: generate a new list by applying a function to every member of an original list. 

`[<expression> for <name> in <list>]`

```python
>>> li = [3, 6, 2, 7]
>>> [elem * 2 for elem in li]
[6, 12, 4, 14]
```
- `<expression>` is some calculatio or operation acting upon the variable `<name>`
- for each member of the `<list>`, thelist comprehension
	- set `<name>` equal to that member and 
	- calculates a new value using `<expression>`
- it then collects these new values into a list which is the return value of the list comprehension

If the elements of `<list>` are other collections then `<name>` can be replaced by a collection of names that match the "shape" of the `<list>` members
```python
>>> li = [('a', 1), ('b', 2), ('c', 7)]

# basically pattern matching
>>> [n * 3 for (x, n) in li]
[3, 6, 21]
```

It is also possible to use nested list comprehension

#### Filtered List Comprehension
`[<expression> for <name> in <list> if <filter>]`
- `<filter>` determines whether `<expression>` is performed on each member of the `<list>`
- when processing each element of `<list>` first check if it satisfies the `<filter>` condition
- if the `<filter>` condition returns false that element is omitted from the `<list>` before the list comprehension is evaluated

```python
>>> li = [3, 6, 2, 7, 1, 9]
>>> [elem * 2 for elem in li if elem > 4]
[12, 14, 18]
```

