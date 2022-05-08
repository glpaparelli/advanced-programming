# Microsoft .NET
*.NET as fast as possible*

Related: [[Software Components#Component Programming]], [[Frameworks]]

*.NET objectives* are:
- **iteroperability:** let possibly remote components written in different languages (conform to .NET specification) to interact with each other
- **performance:** final compiled code is platform dependent hence executed efficiently

## Before .NET
**COM** (**C**omponent **O**bject **M**odel) is the ancestor of .NET and the ancestor of the microsoft component technology. 
COM do not specify any language, the only requirement of a component is that the language in which the component is written supports pointers and, implictly or explicitly, function call through pointers.

Each COM component has to expose an interface, which consits of a pointer to a virtual method table that contains a list of pointers to the functions that implement the functions declared in the interface itself. 
On component method invocation a pointer to the interface is passed to the accessing instance variable. 

## .NET
The .NET framework is the microsoft approach to components and it is a framework to rapidly build a microsoft component software. 
Microsoft wanted to achieve integration and interoperability of applications that are distribuited (logically and/or phisically) on heterogeneous nodes (i.e. applications written in different languages). 

**.NET consists of:**
- **Common Language Specification (CLS)**: guidelines that languages should follow if they want to interact with other .NET languages. *The rules specified in the CLS makes possible for .NET to uniform the type systems of the supported languages*
- **Base Class Libreay (BCL)**: a consisten, object oriented library containing reusable types and utilities to develop CLI, GUI and more (kinda Java API). It is organized by namespaces (unlike Java APIs that are organized in packages), which consist of many classes and sub-namespaces. 
- **Common Language Runtime (CLR)**: the virtual machine enviroment for developement and execution. CLR is the .NET analogous of the [[Java Virtual Machine]]. Thanks to CLR a class in one language can inherit properties and methods from related classes in other languages. 
- **Common Intermediate Language (CIL)**: programs are compiled by the CLR in the common intermediate language (which is the .NET analogous to java bytecode) and then it is compiled again into target machine code. The intermediate language code can be in the format of .exe or .dll, and the code, when compiled by .NET, is called *managed code* and can interact with the support provided by the .NET infrastructure
- **JIT compiler:** compiler from CIL to the target platform language. This provide a performance boosts and also allow to do typechecking exploiting the **Common Type System (CTS)** and code verification
- **Class Loader:** support to dynamic class loading
- **Runtime Support**: to execute target code + garbage collection

### .NET Type System
The CLR defines a **Common Type System (CTS)**, a standard set of data types and rules for creating new types.

![[dot-net-cts.jpg]]

**We have three type constructors:**
- **enum:** used for constants
- **struct:** like classes but with no inheritance (they only inherit from Object) and they are allocated on the stack and not on the heap
- **delegate:** represents references to methods with a specific parameter list and return type. They can be used to pass methods as arguments, enabling higher-order capabilites. They can also be used to support event-base programming

Then we have **value types** which contains the base types (numbers, ...) and structs. 

### The .NET Component Model

A **.NET Component** is an **assembly**, which is a single, pre-compiled and self-describing CIL **module**.  An assembly is built from one or more classes/modules and it is deployed in an .dll assembly file.
*Assemblies* are the unit of deployment/distribution & can be dynamically loaded. 

**Modules**, which are assemblies without manifest, are components but they can't be dynamically loaded. 

*An assemby has the following charatteristics:*
- **self describing**
- **platform independent**
- **located by strong name**

*An assembly consists of up to 4 parts:*
- **manifest:** self-description of the component it contains strong name, type reference info, list of files, info or references assemblies and more
- **metadata:** module's metada
- **CIL code:** module's CIL code
- **resources:** not executable info (images, ...)

#### .NET Component Composition Methods
- **containment:** only outer component interface is visible externally (internal component interface is hidden), but outer methods implementation could uses inner component methods declared in inner component interface
- **aggregation:** the interface of  the inner component is visible from the outside, so a client could invoke internal component functionalities (inner interface is aggregated to the outer interface)

**NB:** containment + aggregation can be composed in a Tree like structure result in a final component

#### Remoting Connectors for .NET Distribuited Components
As 2 java programs can't comunicate if thet are on different JVMs, two programs can't comunicate if thet run on different CLR.

To allow them to communicate there is the **remoting channel connection**. This is done by marshaling:
- **marshal by value (MBV)**: pass a copy of the object
- **marshal by reference (MBR)**: creates a proxy of a remote object. 

For asynchronous callbacks there are **Remoting Delegates**, which can be invoked remotely for notification purposes.