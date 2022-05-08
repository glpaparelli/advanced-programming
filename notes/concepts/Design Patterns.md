# Design Pattenrs
*Each pattern describes a problem which occours over and over again in our envoriment, and then describes the core of the solution to that problem, in such a way you can use this solution a million times over, without ever doing it the same way twice.*

The common definition is: **A Solution to a Problem in a Context**

## Software Design Pattern
*A software design pattern is a resusable solution to a commonly occurring problem within a given context in software design*

#### SW Design Patterns solve
- **software structural problems:** problems about a specific feature of our application
	- abstraction 
	- information hiding
	- ...
- **non-functional problems:** non functional properties that we want to have in our application
	- reliability
	- efficiency
	- ...

The most famous patterns are the 23 patterns introduced by the Gang of Four. We have 3 categories
- **Creational:** patterns that deal with the creation of an object. examples: [[Design Patterns#A First Example Singleton]] or [[Frameworks#Take 3 Factory]] 
- **Structural:** patterns that deal with the class structure such as Inheritance and Composition
- **Behavioral:** pattenrs that provide solution for better interaction between objects, how to provide lose coupling and flexibility to extend easly in the future. 
  examples: [[Frameworks#Template Method Design Pattern]] or [[Frameworks#Strategy Design Pattern]] 
#### SW Design Patterns Components
- **Name:** an easy name that hints the problem and the solution
- **Problem Addressed:** Intent of the pattern
- **Context:** circumstances under which the problem can occour, it is used to determine applicability of the solution
- **Forces:** constraints or issues that the solution must address, they are imposed by the context
- **Solution:** the static and dynamic relationships among the pattern components. The solution must resolve all the forces

#### A First Example: Singleton
- **Name:** Singleton
- **Problem Addressed:** how can we guarantee that one and only one instance of a class can be created? 
- **Context:** in some applications it is important to have exactly one instance of a class
- **Forces:** 
	- encapsulation
		- we can't make the object globally accessible as a global variable
	- polymorphic redefinition
		- we can't use class static operations and attributes
- **Solution:** create a class with a class operation `getInstance()`. when the class is first accessed we create the instance of the object and return it to the client. On every subsequent calls of `getInstance()` no new instance is created, we simply retrun the existing object

*example in Java:*
```Java
class Singleton{
	private static Singleton uniqueInstance = null; 

	//private constructor
	private Singleton(){...}

	public static Singleton getInstance(){
		if(uniqueInstance == null)
			return new Singleton();
	
		return uniqueInstance;
	}
}
```
To specify that a class has only one instance we make it inherit from `Singleton` and the controlled access to a single instance is done through `Singleton` encapsulation
- we can easly tailor it for any finitie number of instances
- the pattern limits flexibility: significant redesign of the code if at some point I realize that I don't need `Singleton` anymore

#### Design Patterns vs [[Frameworks]]
- Design Patterns are **more abstract** that Frameworks
	- DP are conceptual, FW are pratical
- Design Patterns have **smaller/fewer architectural elements** than Frameworks
	- a typical framework contains several design patterns, the reverse is never true
- Design Patterns are **less specialized** than Frameworks
	- a framework always have a particular application domain
	- a design pattern can be used in any kind of application
 