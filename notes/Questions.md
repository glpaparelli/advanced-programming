## Questions
***BASED ON PREVIOUS EXAMS***

#### What are the problems between arrays and generics in Java? 
**references:** [[Java Generics#Type Erasure]]

Generics are transformed to `Object` or the nearest bound at runtime. This is to guarantee backward compatibility, which is key for Java. 
This means that any information about types that we have at static time is lost at runtime. 
#### What are the problems between arrays and generics in Java? 
**references:** [[Java Generics#Problems with Array Covariance]]

A Java Golden Rule: for each reference variable the dynamic type must be equal to the static type or a subtype of the static type. 

In Java generic arrays are not supported since the dynamic type of an array is knwon only at runtime, but at runtime generics are translated to objects, and this would result in non detectable errors: we could assign insert a string into an array of integers without anyone noticing it, and that would break java strong type safety. 

#### Is Python more OO or more functional, according to your opinion?
Python is an Object-Oriented programming language that have been contaminated with some usefull aspects typical of the functional programming paradigm.
This answer has many reasons
- it do not use immutable data very much while in the FP paradigm one of the key aspects is the non-mutable state
- the execution model is basically imperative
- limited support for parallel programming
- almost everything has a side effect
- flow control is not done through function calls
- it do not encourage recursion
- ...

#### Explain Covariance and Contravariance in a Language with Universal Polymorphism.  In what cases their use is safe?
**references:** [[Java Generics#Invariance Covariance and Controvariance]], [[Polymorphism]]

Language with Universal Polymorphism: **Java**
Given a type `A` and its subtype `B`, we can have three form or relationship between `Class<A>` and `Class<B>`
- *invariance*: `Class<A>` and `Class<B>` are not related at all
- *covariance*: `Class<B>` is a subtype of `Class<B>`
- *contravariance*: `Class<A>` is a subtype of `Class<B>`

Mind that, as expected, if `A` extends `B` and they are generic classes then for each type `C` we have that `A<C>` is a subtype of `B<C>`, e.g. `ArrayList<Integer>` is a subtype of `List<Integer>`

Both covariance and contravariance can break the substituition princpile, but sometimes they would be safe. 
Specifically covariance is safe if the type is read-only and contravariance is safe if the type is write-only

#### Explain Inversion of Control and Dependecy Injection
**references:** [[Frameworks#Inversion of Control]], [[Frameworks#Dependency Injection]]

Inversion of Control is a concept that arise naturally when we speak about frameworks. 
In frameworks you specialize or implement some specific parts of the code, the parts that are 
needed for your specific application, while the framework handles the "classic" behavior that your application share with many others. 

Unlike libraries, where is your code to call code already implemented, with frameworks is your code to be invoked by code already written by someone else. 
There is an inversion of control since it is not the code you write that set the flow of the program, the framework will be in execution and it will call your specific code portions for some of your specific needs, then the control will go back to the framework itself. 

Now, with inversion of control we can mean more than just the control flow. An example of inversion of control is the concept of dependency injection, where the control over dependencies is inverted. 
Deoendencies are passed through constructors/setters and the objects will rely on them in an implicit way, which greatly reduces the coupling. 

#### What is Lazy Evaluation in Haskell
**references:** [[Haskell Introduction]], [[Haskell List Comprehension]], [[Haskell DataTypes]]

Lazy Evaluation is a concept related to haskell and more in general to the functional programming (and to the computational model the functional programming "implements", the lambda-calculus). 

Basically it means that expressions are evaluated "as late as possible", when they are actually needed. 

This applies to:
- bindings: the assignment is not evaluated, the name is bounded to an expression that will be evaluated when the name is used for the first time
- parameters: the parameters of a function are not evaluated until the body of the function actually uses them
- ...

One of the main advantages is that this makes possible to define infinite and recursive data structures. 

#### What are Java Streams?
**references:** [[Java Streams]]

Java Streams are one of the functional programming contaminations to Java. A Stream is a sequence of elements supporting sequential and parallel aggregate operations. The sequence is potentially infinite since streams are seek lazyness. 

#### Examples of Lambdas in context differents from Stream
**references:** [[Java Lambdas]]

Lambdas can be used in various places and for many things. An example that do not exploit streams is the following. 
```java
List<Integer> intSeq = Arrays.asList(1, 2, 3);
intSeq.forEach(x -> System.out.println(x));
```

#### What are Functional Interfaces? 
**references:** [[Java Lambdas#Functional Interface]]

Functional Interfaces are interfaces with a single abstract methods (that will be implemented by the body of the lambda expression)

#### How the Java Compiler manages Lambdas?
**references:** [[Java Lambdas#Implementation of Lambdas]]

The compiler first convetrs a lambda expression into a function compiling the body of the expression then generates the code to call the compiled expression where needed. 

As design decision the lambdas are instances of functional interfaces, the functional interface is used as target type of the lambda expression. 

*A lambda expression is interpreted as an instance of an anonymous inner classe that implements the functional interface: the lambda expression provide the implementation to the only abstract method of the interface*

#### Differences between a Component, a Package and a Class
**references:** [[Software Components]]

- *component:* it a complied conglomerate of classes or packages or other components combined. A component is a part of a system
- *class:* a class is a file which represent a single aspect of the current code, it is part of the code
- *package:* a package is a set of classes and or subpackages, it is part of the current program and its use is to reuse code

#### Talk about the lifecycle of a SW Component
**references:** [[Software Components#Forms of a Software Component]]

A software component has different stages that start at design time and ends 
at execution:
- *component specification:* the specification describes the behavior a set of components object and defines a unit of implementation. 
- *component interface:* the interface is the definition of the behaviorus that can be offered by a component object
- *component implementation:* the implementation is the realization of the compoenent specifcation. It is indipendently deployable
- *installed component:* it is a copy of the implementation that has been registered in the runtime envoiroment of the program and it is ready to be deployed/instantiated
- *component object:*: the component object is an instance of an installed compoenent, this is a runtime entity with its own data and unique identity

#### How can we interact with Java Beans
**references:** [[Java Beans]]

We mainly interact with beans visually through a builder tool, in our case the NetBeans Builder Tool. 
The tool interacts with Beans through naming conventions and introspection. 

#### What kind of Properties a Bean can have
**references:** [[Java Beans#Bound Properties]], [[Java Beans#Constrained Properties]]

There are three kind of properties, simple properties, *bounded* and *constrained* (or both). 

When a bounded property is modified an event is fired to everyone that had shown an interest. 
A constrained property is a property whose modification can be vetoed by someone. If the veto is posed the change of the property is not allowed.  

Mind that a property can be both bound and constrained: ask a permission to change a property and if granted notify to everyone interested that the property has been changed (the one that can pose the veto could be also interesed in knowing when the property is changed)

#### What is a Functor in Haskell
[[Haskell Constructor Classes#Constructor Classes]]

A Functor is a constructor class, which are basically type classes over type constructors (which are functoins that takes a type and return a "new" type)

```haskell
class Functor g where
	fmap :: (a -> b) g a -> g b
```
the previous code is a **type class** where the predicate is over a **type constructor** and not over a type. 
We can have various instance of `Functor` where `g` can be one of many type constructors, such as
- `[-]` (type constructors for lists)
- `Tree`
- `Maybe`
- ...

#### What relationship there is between Functor and the Maybe type clas
**references:** [[Haskell Constructor Classes]], [[Haskell Polymorphism]], [[Haskell Monads]]

#todo

#### Talk about Lazyness in Haskell
**references:** [[Haskell Introduction#Lazyness]]

Haskell is a lazy language, this means that expression are evaluated if and when they are needed. 
Functions are evaluated first, arguments are evaluated if and when they are needed.

Haskell realize lazy evaluation by using call by need parameter passing: an expression passed as argument is bound to the formal parameter but it is evaluated only when needed. 

Call by need with lazy data constructors allows the usage of infinite data structures and infinitely recursive functions without necessarly causing non-termination

#### What are Python Decorator
A decorator is any callable python object that is used to modify a function, method or class definition. In our case we see decorators for functions. 

Basically a decorator is a wrapper that alterate the behavior of the original function. 

```python
def my_decorator(function):
	def wrapper():
		# do something here
		function() # use the original function
		# do other stuff here
	return wrapper
```

When we apply the decorator `@my_decorator` to a function definition we are wrapping the function, now the name of the function will refer to the decorated function. 

#### How can we write functions with a variable number of parameters in Python
**references:** [[Python Functions]]

In Python we have two ways to implement a function with a variable number of parameters: `*args` and `**kwargs`
Specifically the variables passed in positional order are "wrapped" in the touple `args` and the parameter passed in a nominal way are represented in the `kwargs` dictionary

#### What is the Global Interpreter Lock?
**references:** [[Python Concurrency]]. [[Python GC]]

The CPython interpreter assures that only one thread at a time executes python bytecode, thanks to the *global interpreter lock*

The current thread must hold the GIL before it can safely access python objects. This greatly simplified the CPython implementation since everything is thread safe as there simply arent race conditions!

The GIL can actually cause a degeneration in performance since the system calls overhead is significant: two threads executing the same function may take twice as much as it would take to one thread to execute the same function twice. 

This is becuase of the garbage collection in Python: the deallocation is done by referencing counting and mark&sweep. 
The update of the reference count of an object has to be done atomically: with more threads this could be a problem since the modification of the reference count is a race condition and it must be synchronized. 
The problem is that almost anything in Python can change the reference count of objects: almost everything is an object.
Most of the execution time would be wasted on the synchronization of the reference count of objects. 
For this reason we have the GIL.

#### What does the @staticmethod decorator do? 
**references:** [[Python Decorators]]

`@staticmethod` make a function invocable on the class name. A static method do not recieve the reference object as first implicit parameter as other methods do. 
A static method can not access the class/instance attributes, it will result in an error. 

