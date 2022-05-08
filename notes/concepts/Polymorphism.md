# Polymorphism
Greek word that mean "having several forms"
- **forms**: aka types, either primitive or user defined

"Polymorphic" are:
- **function names, operators, methods**: with the same name can take as parameters many forms (many types)
- **types**: parametric data types, type constructors, generics 

## Classification of Polymorphism
There are two main categories of polymorphism. Mind that they are not mutual exclusive: we can have both at the same time.

The full schema is the following (-> means that points to an already existing node of the tree)
**polymorphism**
- **universal**
	- **coercion**
	- **parametric**:
		- **implicit**
		- **explicit**
			- **bounded**
				- **covariant**
				- **invariant**
				- **contravariant**
	- **inclusion**
		- -> **bounded**
		- -> **overriding**
- **ad hoc**
	- **overloading**
	- **overriding**

 ### Ad Hoc Polymorphism
 With **ad hoc** polymorphism the same function name denotes different algorithms (different code). The code to execute is determined by the actual types. 

#### Overloading
Concept present in all languages, at least built in for arithmetic operators such as `+`, `*`, ...
- eg Java: `+` is the sum for numbers but also the concatenation of strings

- sometimes is supported for user defined functions (Java, C++)
- sometimes is supported even for primitive operators (C++, Haskell)

*The code to execute is determined by the type of the arguments*, thus
- [[Binding#Early Binding]] in statically typed languages
- [[Binding#Dynamic Binding]] in dynamically typed languages

##### Example
Let's say that we want to implement: $f(x) = x^2$

*C Language*
```C
// no support for overloading:  
// different names for the same logic
int intSqr(int x){
	return x * x;
}
double doubleSqr(double x){
	return x * x
}
```

*Java, C++*
```java
// overloading: the implementation to
// execute is decided based on the type of pars
int sqr(int x){
	return x * x;
}
double sqr(double x){
	return x * x
}
```

*Haskell*
[[Haskell Polymorphism#Type Classes]]

#### Overriding
A method `m()` of a class `A` can be redefined in a subclass `B` of `A`

[[Binding#Dynamic Binding]]:
```java
A a = new B(); //legal
a.m(); // the overridden method in B is invoked
```

### Universal Polymorphism 
With **universal** polymorphism there is only one algorithm: a single (universal) solution that is applied to different objects. 
The call of the algorithm is type indipendent: it is the algorithm that can handle different types of arguments. 

#### Coercion
**Coercion**: automatic conversion of an object to a different type. 
It is opposed to casting, which is explicit. 

Usually if there is no loss of information the coertion is explicit (no harm can be done), if there is loss of information the casting has to be explicit: the compiler can't take the liberty of destroying potentially usefull info. 
*example*
```C
int x = 5; 
double dy = 3.14;

// coercion, "implicit casting", no info loss
double dx = x; 

//casting, potentially dangerous info loss
int y = (int) dy; 
```

*Coertion can be used for polymorphism* but it is a **degenerate and uninteresting case**
```C
double sqrt(double x){...}

//the par is an int, which is "coerced" to double
double d = sqrt(5); 
```

#### Inclusion
Inclusion polymorphism is also known as **subtyping polymorphism**, or just *inheritance*. 

The polymorphism is ensured by the **substitution principle**: an object of a subtype (subclass) can be used in any context where an object of the supertype (superclass) is expected 
- the subclass has *at least* all the fields and methods of the superclass

Java and C++ uses the substitution principle for classes: methods/functions with formal parameter of type $T$ accept an actual parameter of type $S <: T$ ($S$ subtype of $T$)
- if the methods have been *overridden* then it is [[Polymorphism#Overriding]] since the signature is the same but the body are different
- if the inherited method methods are not modified (not specialized) then it is universal polymorphism because is the same code (the code of the parent) that goes in execution