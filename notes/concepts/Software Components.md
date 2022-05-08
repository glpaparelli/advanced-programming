# Software Components
## Component-Based Software
The cost of software development is alwayis increasing, reuse of already written software is crucial. 
Also, over the years we transitionend from software products to software families, it is not smart to reimplement every time for similar problems. 
It is better to buy off-the-shelf than re-implementing. 
*Constructing systems by composing software components is easier*

With a **component software** we mean a composite systems made of software components. 
**A component software is a software made by composing software components**

A component software is tipically more reliable
- **software components** are more tested that our new implementation
- system requirements can force use of certified compoents

## Software Components
### Desiderata for Software Components
*Software Components should be:*
- **easy**: easy and euxasthive interface, the component is easy to install and use
- **multipurpose**:  the component can be used in different scenarios
- **extendible**: the component can be easly extended with some home-made functionalities
- **correct**: the component respect the specific (of itself, of the program)
- **robust**: the component has to work even in not ideal scenarios without crashing 
- **portable**: the component is easy to transfer without modification, is easly deployable

### Definition of Software Component
A *software component* is a **unit of composition** with **contractually specified interfaces** and **explicit context dependencies** only. It can be **deployed indipendently** and it is subject to **composition by third party**

#### Unit of Composition 
A software component is a single gear of the component software it builds. 
Each component is an atomic unit of composition. They are black boxes and they are often already compiled (the soruce code can't be seen). 
Being black boxes (no externally observable state) makes them indinstinghishable from copies: instances of the same component are perfectly equals from the outside. 

#### Contractually Specified Interfaces
The interface of a component (how it interact with the outside, what are the functionalities it provides, ...) is formally defined and the qualitative aspects of the interface can be enforced by a contract

#### Explicit Context Dependecy
Specification of the deployment enviroment and run-time enviroment in which the component will be working. 
Which tools, platforms, resources or other components are required by the component so it can properly work?

#### Deployed Independently
We want components to be able to be deployed indipendently from the other compoents and the underlying platform. 
This means that we want that the dependencies of a component are resolved as late as possible: load time or even better at runtime. 
Components have to be able to be composed with each other as late as possible, ideally just before they are actually needed

#### Composition by Third Party
The component can be plugged into a system or composed with other components by third parties that are not aware of the internals of the component

### Forms of a Software Component 
Forms, kinda "stages", like a chrysalis. 
We start from design time and we get to execution:
- **component specification**: *describes the behavior of a set of Components Objects and defines a unit of implementation*. The behavior is defined as a set of **interfaces**. A component specification is realized as a Component Implementation
- **component interface**: *a definition of a set of behaviors that can be offered by a Component Object*
- **component implementation**: *A realization of the component specification*, which is indipendently deployable. This means that it can be installed and replaced independently of other components. 
	- this not means that it is indipendent of other components
	- this not necessarily mean that it is a single physical item such a single file
- **installed component**: *an installed (or deployed) copy of a component implementation*. A component implementation is deployed by registering it with the runtime enviroment
	- this enables the runtime enviroment to identify the installed component to use when creating an instance of the component or when running one of its operations
- **component object**: *an instance of an installed component*. This is a runtime concept, this is an object with its own data and an unique identity. This is the thing that performs the implemented behavior. An installed component may have multiple component objects

## Component Model
The component model defines a conceptual framework that specify how the components are described and implemented. 
The concepts that we are seeing are language/paradigm agnostic, this lays the ground for language interoperability.

This is done by the following ingridients:
- **component interface**: describes the operations (method calls. messages, ...) that a component implements and that other components may use
- **composition mechanism**: the manner in which different components can be composed to work together to acomplish some task 
	- eg: [[Java Beans]] as composition mechanism have the event passing and listening
- **component platform**: a platform for the development and execution of components 
	- eg: Netbeans tool for [[Java Beans]]

## Component Programming
A component can basically be anything and contain anything, it can be a collection of classes, objects, functions/algorithms, data structures and so on. 

A component has a coarser granularity that a class, obviously. 

Components support
- **unification of datas and functions**
- **encapsulation**: no visible state from the outside
- **identity**: each software entity has an unique identity
- **use of interfaces to represent dependencies**

*Component Oeriented Programming has as main goal the code reuse, Object Oriented Programming has as main goal the appropriate domain/problem representation*

### CBSE: Component Based Software Engineering
- based on the concept of software component
- components can be assembled according to the rules specified by the component model
- components are assembled through their interfaces
- a component composition is the process of assembling components to form an "assembly", which is a larger components or an application
- component are performing in the context of a component framework
- a component technology is a concrete implementation of a component model (eg. Netbeans Tool)

## Before Components: Modules
The concept of module was the first attempt to increase software modularity and to increase code reuse (they are now deprecated, eg. in Java we have packages)
- support for information hidning through incapsulation: explicit import and export lists
- reduce risks of name conflicts
- support integrity of data abstraction 
- teams of programmers can work on separate modules in a project

Depending on visibility politcs:
- **open scope**: objects outside the module are visible
- **closed scope**: objects outside are visible only when imported
- **selectively open scope**: visible with qualified name, eg `ModuleName.EntityName`

### Modules vs Components
Modules and Components are diferent: modules are part of a program, components are part of a system (they could be written in different languages, be already compiled, include static resources, ...)