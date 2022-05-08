# Frameworks
**Software Framework**: a collection of common code providing generic functionality that can be selectively overridden or specialized by user code providing specific functionalities

**Application Framework**: a software framework used to implement the standard structure of an application for a specific developement environment
- *an application framework is an instance of a software framework*

**Example of General Software Framework**: [[Microsoft .NET]]

## Component Frameworks
Component Frameworks are frameworks that:
- support developement, deployment, composition and execution of [[Software Components]] designed according to a given [[Software Components#Component Model]]
- support the developement of individial software components, enforcing the design of precise interfaces
- support the composition/connection of components according to the mechanism provided by the component model
- allows instances of these components to be plugged into component framework itself (eg. add a [[Java Beans]] `JButton` to the palette in the Netbeans tool)
- provide prebuilt functionalities such as useful components or automated assembly functions that automatically instantiate and compose components to perform common tasks

The component framework establishes environmental conditions for the component instances and regulates the interaction between component instances
- example: constructors with no param with Java Beans

## Frameworks vs IDEs
They are orthogonal concepts:
- a framework can be supported by several IDEs
- an IDE can support several frameworks

## Features of Frameworks
A Framework embodies some abstract design with more behavior built in. 
In order to use it you need to insert your behavior into various places in the framework, then the framework's code calls the user defined code: a very general concept: [[Frameworks#Inversion of Control]], as opposed to libraries is the code of the framework that set the execution flow. 

A Framework is made of the following well-knwon parts
- **libraries**: classes with methods, APIs, ...
- **engines**: ready-made extensible programs
- **tools** for developement, configuration, ...
- **reusable abstractions**: abstract data types already implemented and wrapped in well-defined API

Frameworks help solving recurring design problems: they provide a default behavior. 

The non-modifiable framework code define a default behavior and how to fill-in-the-blanks: how to specialize/override/plug-in user defined code that address a specific user need. 

*To extend a framework* (and meet the needs of a particular application) we have two main paths:
- **extension within the framework language**: sublcassing, implementing interfaces, registering event handler, ...
- **plug-ins**: the framework loads extra code in a specific format

## Case Study: TradeMonitor
A trader wants that the system rejects trades when the exposure reaches a certain limit. 
Thus a component (a class, ...) provides a method `TryTrade` which checks the condition. 
The current exposure and the exposure limit are stored in some persistent storage (a database, ...), and are accessed by `TryTrade` using another component, a `DAO` (Data Access Object)

### Take 1: Base Idea
```java
public class TradeMonitor{
	private LimitDao limitDao; 

	public TradeMonitor(){
		this.limitDao = new LimitDao();
	}

	public bool TryTrade(String tradingOp, int amount){
		int limit = limitDao.getLimit(tradingOp);
		int exposure = limitDao.getExposure(tradingOp);

		return (exposure + amount > limit) ? false : true;
	}
}

public class LimitDao{
	public int getExposure(String tradingOp){
		//database interaction
	}
	public int getLimit(String tradingOp){
		//database interaction
	}
}
```

The previous code works but **TradeMonitor is tightly coupled to LimitDao**
- **extensibility**: what if we replace the database with a distributed cache? We would not be able to reuse `LimitDao`, we would have to modify all the code and substitute `LimitDao` with the new object that interacts with the cache
- **testability**: to test `TradeMonitor` we have to use `LimitDao`, testing should be as "compartimentalized" as possible
  **reusability**: the logic is pretty simple but we can't reuse this code in almost any other similar situation

### Take 2: Interface - Implementation Decoupling
We can introduce **interface/implementation** reparation;
```java
public interface LimitRepository{
	public int getExposure(String tradingOp);
	public int getLimit(String tradingOp);
}

public class LimitDao implements LimitRepository{
	public int getExposure(String tradingOp){ ... }
	public int getLimit(String tradingOp){ ... }
}

public class TradeMonitor{
	private LimitRepository limitRepo; 

	public TradeMonitor(){
		this.limitRepo = new LimitDao();
	}

	public bool TryTrade(String tradingOp, int amount){ ... }
}
```
This helps with the coupling, the logic do not depends on `DAO` anymore: we have a bind with an interface which is not a component. 

The code is still higly coupled tho. In the constructor `TradeMonitor()` we have an explicit invocation of `LimitDao()`

### Take 3: Factory
We introduce a **Factory** (creational [[Design Patterns]]). It has the responsability to create the required instance and it can return the right kind of implementation of the `LimitRepository` interface. 
```java
public class LimitFactory{
	public static LimitRepository getLimitRepository(){
		return new LimitDao();
	}
}

public class TradeMonitor{
	private LimitRepository limitRepo;

	public TradeMonitor(){
		limitRepository = LimitFactory.getLimitRepository();
	}

	public bool TryTrade(String tradingOp, int amount){ ... }
}
```

We can see that:
- in the class `TradeMonitor` there is no direct reference to `LimitDao`, no coupleness, which is good
- `LimitDao` has a direct reference to the Factory, `LimitDao` is now higly coupled
	- the factory can instantiate and return only `LimitDao`, it would be better if the factory could return different types of objects based on what it is asked

### Take 4: Service Locator
We can now introduce a **ServiceLocator**, which is an object that acts as a (static) registry for the components you need (in this case the different kinds of `LimitDao` you could design)

```java
public class ServiceLocator{
	public static void RegisterService(Type t, Object o){ ... }
	public static Object getService(Type t){ ... }
}

public class TradeMonitor{
	// as static type still the interface
	private LimitRepository limitRepo; 

	public TradeMonitor(){
		// cast needed: ServiceLocator return an object
		this.limitRepo = (LimitRepoistory) 
				ServiceLocator.getService(typeOf(LimitRepository));
	}

	public bool TryTrade(String tradingOp, int amount) { ... }
}
```

This solution gives extensibility, testability and reusability, mind tho that something is required to sets up the registry. 

`ServiceLocator` pros:
- it succeeds in decoupling the `TradeMonitor` from the `LimitDao`
- allows new components to be dynamically created and used by other components later: the thing that sets up the locator could modify it even at runtime
- it can be generalized in several ways

`ServiceLocator` cons:
- every component that needs a dependency must have a reference to `ServiceLocator`: the locator could become a bottleneck
- all components need to be registered with the service locator and
	- if a component is bounded by name:
		- services can't be type checked 
		- the component has a dependency to the dependent component names: we lookup components by names, what if a component change its name? 
		- if many components share an instance but later you want to specify a differnt instance for some of the components it is difficult
	- if a component is bounded by type
		- we can only bind one instance of a type in the locator: two instances of the same type would be indistinguishable for the locator
- the code needs to handle the case in which a component is not found in the locator

## Inversion of Control
Frameworks, like libraries, provide reusable abstractions (eg. abstract data structures) that the user must implmenet in order to make use of the full framework. 

However the overall program's control flow is dictated by the framework itself, that calls the user-defined specialized code in some needed points. 
This is the opposite from the libraries, where your code calls the code written by somebody else, here your code just fill/specialize some key aspects (needed for the specific application you are writing) and that code is invoked by the framework.

Furthermore, Inversion of Control also concerns **dependencies**, **coupling** and **configuration**. 
This is because *control* is a broad concept: "control flow" is not the only control that exists, we can have "control on dependencies", "coupling control" or "control of configuration". 

### Dependency Injection
Dependency Injection is a specific case of inversion of control, where the control over dependencies is inverted. 
Dependencies are passed through constructors/setters/service lookups and the object will rely on them but not explicitly, and this greatly reduce **coupling**. 

We see only two forms of dependency injection:
- **setter injection**
- **constructor injection**

#### Setter Injection 
*Idea: add a setter, leaving creation and resolution to others*
```java 
public class TradeMonitor{
	private LimitRepository limitRepo; 

	// we do not instantiate the field limitRepo
	public TradeMonitor(){}

	public void setLimitRepository(LimitRepository limitRepo){
		this.limitRepo = limitRepo;
	}

	// we will have to check that limitRepo != null
	public bool TryTrade(String tradeOp, int amount) { ... }

}
```
- **pros**:
	- leverages existing patterns ([[Java Beans]] works well with this schema)
	- simple and ofter already available (no need for registry or other stuff)
- **cons**:
	- possible to create partially constructed objects: objects with some fields left null
	- advertises that dependency can ba changed at runtime (the setter can be invoked anytime and that changes the behavior of the whole application)

**Setter Injection is widely used in the framework Spring**

#### Constructor Injection
Same idea as setter injection but exploiting the constructor(s)
```java
public class TradeMonitor{
	private LimitRepository limitRepo;

	public TradeMonitor(LimitRepository limitRepo){
		this.limitRepo = limitRepo;
	}

	public bool TryTrade(String tradingOp, int amount){ ... }
}
```
- **pros**: 
	- objects can't be partially constructed
	- simple, often already available
- **cons**: 
	- bidirectional dependencies between objects can be tricky: what if an object `B` needs an object `A` in the constructor and viceversa? You can't pass a `null` reference to a constructor but you need the constructor to intantiate the object!
	- constructors can easily get big and parameters confusing
	- if lots of optional dependeincies you may have lots of constructors
		- the number of constructors is exponential in the number of optional parameters 
	- can make class evolution more complicated (an added dependency affects all users of the class) w.r.t setter injection

**Constructor Injection is widely used in the framework PicoContainer**

### Dependency Injection vs Service Locator
Both **Dependency Injection** and **ServiceLocator** provide the desired decoupling. 
**With the service locator the desired component is obtained after a request by the trade monitor: no inversion of control here.** 
With dependency injection there is no explicit request: the component appears in the application class. 
Inversion of Control is a bit harder to understand but is also easier to implement (obv), with the service locator the application still depends of the locator, 
It is easier to find dependencies of components if dependency injection is used: simply check constructors and setters vs check all invocations to locator in the source code

## Desigining Software Frameworks
Designing software frameworks is an intellectual challengin task that requires a deep understanding of the application domain. 
It also requires mastering of [[Design Patterns]], object oriented programming and [[Polymorphism]]. 

### Some Terminology
- **software family**: set of different solutions for a common problem
- **frozen spot**: common (shared) aspect of the software family
- **hot spot**: variable aspect of the family
	- represented by a group of *abstract hook methods*: hook methods are not implemented by the framework since they are variable aspects of the solution. It is only specified the signature of the methods (abstract methods!) and then the code will be provided by the user
	- a hot spot is realized in a framework as a *hot spot subsystem*: relies on inheritance
- **template method**: a concrete method of the base (abstract) class is a method implementing the common behavior of all the members of the family. *template methods are concrete solutions to frozen spots*

### Case Study: Divide and Conquer 
We want to create a framework for the family of divide&conquer algorithms. The idea is to start from a well-known generic algorithm and then apply known techniques and patters to define a framework for a software family.

```
function solve(Problem p) returns Solution
	if isSimple(p)
		return simplySolve(p)
	else
		smallerProblems[] = decompose(p)
		solutions[]
		
		for(i = 0 to smallerProblems.length)
			solutions[i] = solve(smallerProblems[i])

		return combine(sol)
```

Instances of the framework, obtained by standard extension mechanism will be concrete algorithms of the family. 

### Two Principles for Framework Construction
- **unification pronciple**: based on the [[Design Patterns]] *Template Method*
	- *it uses inheritance to implement the hot spot subsytem*
		- template methdos are implemented in the abstract class while the hook methods are only defined in the same abstract class, then hook methods are implemented in subclasses of the abstract class
- **separation principle**: based on the Design Patten *Strategy*
	- *it uses delegation to implement the hot spot subsystem*
		- template methods are implemented in a concrete context class; hook methods are defined in a separate abstract class and implemented in its subclasses
		- the template methods delegate work to an instance of the subclass that implements the hook method

#### Template Method Design Pattern
**(Unification Principle)**
One of the behavioural pattern of the Gang of Four. The intent is to define the skeleton of an algorithm in an operation, deferring some steps to subclasses.
A **template method** belongs to an abstract class and it defines an algorithm in terms of abstract operations that subclasses override to provide concrete behavior. 

Template methods call, among others, the follwing operations:
- **concrete operations** of the abstract class (fixed parts of the algorithm): the frozen spots
- **primitive operations**, i.e. abstract operations that subclasses have to implement (in our case study: decompose, isSimple, combine, ...)
- **hook operations** which provides default behavior that subclasses may override if necessary. A hook operation does nothing by default. 

##### Implementation of Template Methods
Using **Java** visibility modifiers
- **template methods** should not be overrriden: it can be declared `public final`
- **concrete operations** can be declared `private` ensuring that they are only called by the template method
- **primitive operations** that must be overridden are declared `protected abstract`
- **hook operations** that may be overridden are declared `protected`

```java 
// solve is a template method, isSimple, simplySolve, decompose, combine are hot spots
// already defined the interfaces for Solution and Problem

abstract public class DivConqTemplate{
	public final Solution solve(Problem p){
		if(isSimple(p))
			return simplySolve(p);

		Problem[] smallerProblems = decompose(p);
		Solution[] smallerSolutions = 
					new Solution[smallerProblems.length];

		for(int i = 0; i < smallerProblems.length; i++)
			smallerSolutions[i] = solve(smallerProblems[i])

		return combine(p, smallerSolutions);
	}

	abstract protected boolean isSimple(Problem p);
	abstract protected Solution simplySovle(Problem p);
	abstract protected Solution[] decompose(Problem p);
	abstract protected Solution combine(
		Problem p, Solution[] smallerSolutions
	);
}

// application of the framework: QuickSort

public class DescQuickSort implements Problem, Solution{
	//quicksort do in-place sorting, at the end it will be sorted
	private int[] array;
	private int first, last;

	public QuickSortDesc(int[] array, int first, int last){
		this.array = array;
		this.first = first;
		this.last = last;
	}

	public int getFirst(){
		return this.first;
	}
	public int getLast(){
		return this.last;
	}
	public int[] getArray(){
		return this.array
	}
}

public class QuickSort extends DivConqTemplate{
	protected boolean isSimple(Problem p){
		DescQuickSort qs = (DescQuickSort) p;
		return qs.getFirst() >= qs.getLast();
	}

	protected Solution simplySolve(Problem p){
		return (Solution) p;
	}

	protected Problem[] decompose(Problem p){
		DescQuickSort qs = (DescQuickSort) p;
		int first = qs.getFirst();
		int last = qs.getLast();
		int[] array = qs.getArray();

		//pivot value
		int x = array[fisrt];
		int sp = first;

		for(int i = first+1; i <= last; i++){
			if(array[i] < x )
				swap(array, ++sp, i);
		}
		swap(array, first, sp);

		Problem[] ps = new QuickSortDesc[];
		ps[0] = new QuickSortDesc(array, fisrt, sp - 1);
		ps[1] = new QuickSortDesc(array, sp+1, last);

		return ps;
	}

	protected Solution combine(Problem p, Solution[] smallerSolutions){
		return (Solution) p;
	}

	private void swap(int[] array, int first, int last){
		int tmp = array[first];
		array[first] = array[last];
		array[last] = tmp;
	}
}
```

#### Strategy Design Pattern 
**(Separation Principle)**
One of the behavioural pattern of the Gang of Four. The intent is to allow to select (part of) an algorithm at runtime. The client uses an object implementing the interface and invokes methods of the interface for the hot spots of the algoritm. 

##### Implementation of Strategy
We implement in Java. 
The UML diagram of the solution: 
![[strategy-dp-uml.jpg]]

*Separation Principle*: the root class of the framework is `DivConqContext` and it implements the template methods using methods that are specified in `DivConqStrategy`, and the actual implementation of those methods depends on the concrete subclasses.
The method `setAlgorithm` set the concrete instance of `DivConqStrategy`: it is *dependency injection* through a setter method. 

```java
public final class DivConqContext{
	private DivConqStrategy strategy; 

	public DivConqContext(DivConqStrategy strategy){
		this.strategy = strategy;
	}

	public void setAlgorithm(DivConqStrategy strategy){
		this.strategy = strategy;
	}

	// solve uses delegates (strategy methods) to 
	// the implementation of hot spots
	public Solution solve(Problem p){
		Problem[] smallerProblems; 
		if(strategy.isSimple(p))
			return strategy.simplySolve(p);

		Problem[] smallerProblems = strategy.decompose(p);
		Solution[] smallerSolutions = 
					new Solution[smallerProblems.length];

		for(int i = 0; i < smallerProblems.length; i++)
			smallerSolutions[i] = this.solve(smallerProblems[i]);

		return strategy.combine(p, smallerSolutions);
	}
}

// strategy interface with the the hot spot methods
// actual solutions (quicksort, mergesort, ...) will extends this class
abstract public class DivConqStrategy{
	abstract public boolean isSimple(Problem p);
	abstract public Solution simplySolve(Problem p);
	abstract public Problem[] decompose(Problem p);
	abstract public Solution combine(
		Problem p, Solution[] smallerSolutions
	);
}
```

#### Strategy vs Template Method
The two approaches differ in the coupling between the client and the chosen algorithm (the client is the one who wants to use the framework). 
- **template method**: we have a reference to an instance object (of one of the subclasses of `DivConqTemplate`): *in template method the client and the chosen algorithm are tightly coupled*
- **strategy**: the client has to delegate to `DivConqContext` an object (one sublcass of `DivConqStrategy`) that has the implementation of the hot spots. With Strategy the coupling is determined by *dependency injection* and it can change at runtime. *Strategy is not tightly coupled*

### Conclusion 
- software framework design is a complex task
- the starting point is to identifying families of homogeneous software applications
- then we have to identify the frozen spots and the hot spots
- we have to exploit design patterns and other techniques to achieve as generality as possible and to reduce coupling to the possible minimum. Inversion of Control and dependency injection arises naturally and are keys to framework deisgn


