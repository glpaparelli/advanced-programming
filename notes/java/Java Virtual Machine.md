# Java Virtual Machine (JVM)
*The JVM is a real abstract machine: Oracle do not impose an implementation of the JVM, the specification is only about abstract properties that must to be respected.* 

## Execution Model 
The JVM is a multi-threaded stack based machine
- **multi-threaded:** always more than one single thread in execution even if the program in execution is single-threaded
- **stack based mchine:** 
	- the operations of the JVM operates only on the values contained on the **operand stack** (not related to the classic call stack)
	- inside every activation record on the call stack there is an operand stack
	- more on this later 

## Class FIles and Class File Format
The JVM specification defines a machine indipendent "class file format" that all the JVM implementations must support. Strong syntattic and structural constraints are imposd on the class file to allow static analyis and such. 

- every class file have has an external representation that is platform indipendend. This is the compiled Java file (it is the bytecode of the class)
- then a class is loaded into the JVM and the loading phase transform the .class file into the internal representation, specific for this very implementation of the JVM. 
	- the loading process must be implemented with the implementation of the JVM

## Java Hierarchy
- **5-th layer**: *Java Application* (written in Java Programming Language)
- **4-th layer**: *Java Programming Language*
- **3-th layer**
	- *Java Class Library (JCL)*: all the Java API (utils, stream, ...)
	- *Java Native Interface (JNI)*: handles calls to external programs written in other languages
- **2-th layer**
	- *Java Native Interface (JNI)*: handles calls to external programs written in other languages
	- *Java Virtual Machine (JVM)*
		- classloader: load classes in the runtime enviroment
		- verifier: after a class is loaded it checks that nothing bad will happen
		- execution
- **1-th layer**: operating system

## JVM Data Types
- **primitive types**: the dimensions are by the JVM specs, not implementation dependant
	- **numerical integral**: byte (1 byte), short (2 bytes), int (4 bytes), long (8 bytes), char (2 bytes)
		- most operations are supported only on int, float and double
		- int is a working type. see later on
	- **numerical floating point**: float (4 bytes), double (8 bytes)
	- **boolean**: boolean 
		- mind that boolean is "supported" only for arrays
		- boolean is a primitive type of the programming language Java but it is not a primitive type for the JVM. This is because the smallest indicizable word is of 1 byte hence we can't have operations on bool that only takes 1 bit (can't read a single bit)
		- to save a bit (boolean) we waste a full byte
- **reference types**: pointer to the heap, always 4 bytes
	- class
	- array
	- interface

Mind that we have no information on local variables at runtime. Types of operands (the parameters of the JVM instructions) are scpecified by **opcodes**

## Object Representation in Memory
It is totally left to the implementation, the oracle specification says nothing about it
- included the concrete value of `null` (often is zero, which is not very good)

Since the object structure is not defined (left to the implementation) we can't allocate objects on the stack (can't know the space needed by the object), hence on the stack we have pointers (4 bytes by specific) that refer to the objects allocated in the heap. 
This also makes the garbage collection easier (when no pointers to an object then the object is eliminated)

The object representation must include
- mutex lock 
	- JVM is concurrent and with the Garbage Collection the JVM specific impose that every object have to have a mutex: is the object locked? who has the lock? (the GC takes the lock before eliminating an object)
- GC state (flags): the garbage collection is left to the implementation

## JVM Data Areas
### Per Thread Area
Inside this area (and each thread has its own) we find:
- **program counter:** pointer to the next instruction in the method area (which is inside the [[Java Virtual Machine#Non Heap]])
	- undefined if current method is native: by specification if the code in execution is not bytecode the JVM do not even know the execution model of that (native) code
- **the native stack:** used for invocation of native functions through the JNI
	- when a native function is invoked the execution continues using the native stack 
	- native functons can call back java methods which use the java stack
- **the java stack:** a stack of frames (aka activation records)
	- a new activatoin record is created each time a method is invoked and it is destroyed when the method completes

#### Structure of Frames
Inside every activation record we find 
- **local variable array**: each cell is 32 bits and it contains 
	- reference to `this` (if the AR is relative to an instance method) at position 0
	- method parameters 
	- local variables
- **operand stack**: to support the evaluation of the method itself
- **reference to the constant pool** of the current class: the reference points to the method area (inside the Non Heap) where the actual bytecode of the method is contained. Is to this reference that we can have the program counter to the next instruction
- **return value**: if some value has to return it will be copied inside the local variable array inside the activation record of the caller

Mind that the sizes of both the operand stack and the local variable array are not fixed but they are extimated at compile time when the bytecode is generated. 

### Heap
The heap is the memory for objects and arrays (Java is different from C++, arrays and objects are never allocated to the stack for the reasons we have seen). 
In Java there is not explicit deallocation, only garbage collection.

### Non Heap
Generally in the execution of a program there is a part of the memory that is static (never grows) (like the code) that is allocated when the program starts and deallocated when the program ends. 
Then there is also the dinamic part of the memory (like the heap) that grows and shrinks during the execution. 
The **non heap** is semi-static: not fixed completely at the start (the method area may grow with the loading of classes) but it is almost never affected by the garbage collector

In this area we find objects that are never deallocated and that are needed for the JVM execution, in particular we find:
- **method area**: here it is stored the internal representation of classes
- **interned strings**: explicit strings between "". Strings are objects and they are saved here because they are immutable and finals, it is useless to allocate them multiple times
- **code cache for JIT**: the Hotspot JVM (the Oracle implementation of the JVM) do code profilation and identifies code area that are "hot", meaning that are executed regularly. These parts are compiled in native code and stored in the **code cache** inside the non heap. 

#### Method Area
This is the portion of the non heap where the class files are loaded. Mind that this area is shared between threads so the access has to be thread safe. 
The method area cheange when
- a new class is loaded
- a symbolic link is resolved by dynamic linking

For each class we find: 
- **classloader reference**
- from the class file 
	- **runtime constant pool**
	- **field data**
	- **method data**
		- ...
		- **method code**

##### Class File Structure
With $u_n$ we intend `unsigned` $n$ bytes. 
Inside a class file we find:
- $u_4$ **magic** indicates that this is a java class file
- *java language version*
	- $u_2$  **minor_version**
	- $u_2$ **major_version**
- *contant pool*
	- $u_2$ **constant_pool_count**: number of elements in static constant pool
	- **cp_info**
	- **constant_pool[constant_pool_count - 1]**: actual constant pool
- *access modifier and other info*
	- $u_2$ **access_flags**: private, public, protected, ...
- *references to class and superclass*
	- $u_2$ **this_class**
	- $u_2$ **super_class**
- *references to direct interfaces*
	- $u_2$ **interfaces_count**
	- $u_2$ **interfaces[interfaces_count]**: actual fields
- *static and instance variable*
	- $u_2$ **fields_count**: number of fields
	- **field_info**: field access (private, int, ...)
	- **fields[fields_count]**: actual fields
- *methods*
	- $u_2$ **methods_count**: number of methods
	- **method_info**: mathods access (private, static, ...), signatures, number of arguments, return type, ...
	- **methods[methods_count]**: actual methods, the bytecode
- *other info on the class*
	- $u_2$ **attributes_count**
	- **attribute_info**
	- **attributes[attributes_count]**

##### Field Data
For each field we have the following field info
- **name**: used for the resolutions of symbolic addresses since the resolution is dynamic
- **type**: *FieldType* descriptors
	- FieldType term, Type, Interpretation
		- B, byte, signed byte
		- C, char, unicode character
		- D, double, double precision floating point value
		- ...
		- I, int, integer
		- L ClassName, reference, an instance of the class ClassName
		- Z, boolean, true or false
		- \[, reference, one array dimension
			- \[\[ indicates two dimensional array (matrix) and so on
- **modifier**: public, private
- **attributes**: for annotations

##### Method Data
For each method
- **name**
- **return type**
- **parameters types** (ordered as in the signature)
- **modifiers**
- **attributes** (annotations)
- **method code**

A method descriptor contains
- a sequence of zero or more parameter descriptor in brackets
- a return descriptor or V for void descriptor

The descriptor of the method 
```java
Object m(int i, double d, Thread t) { ... }
 ```
is `(IDLjava/lang/thread;)Ljava/lang/Object`

###### Method Code
In the runtime constant pool we find, *per method*
- **bytecode**
- **operand stack size**: compiler make an educated guess on the size
- **local variable array size**: compiler make an educated guess on the size
- **local variable table**
- **exception table**: see exception handler
- **LineNumberTable**: which line of source code corresponds to which btecode instrution, used for the debugger

And *per* *exception handler* (one for each try/catch clause in the method)
- start point
- end point
- PC offset for handler code: PC try + PC offset = PC catch
- constant pool index for exception class(es) being caught

##### The Static Constant Pool 
The constant pool is similar to the symbol table (in compilers the symbol table match to each variable identifier some info, such as type, memory location, ...), but the constant pool is way more rich. 

*The constant pool contains constants and symbolic references used for dynamic binding, suitably tagged* (tagged = type descritption) [[Binding#Dynamic Binding]]

In the constant pool we find:
- **numeric literals**: Integer, Float, Long, Double 
- **string literals**: Utf8
- **class references**: Class
- **field references**: Fieldref
- **method references**: Methodref, InterfaceMethodref, MethodHandle
- **signatures**: NameAndType

Mind that operands (the pars to the native operations of the JVM that go on and off the operand stack) ofter are addressed with logic indexes in the constant pool

## JVM Startup
This is the classic procedure when a program starts
- the JVM starts up by **loading** an initial class (where the main method is) using the *bootstrap classloader*
- the class is **linked** and **initialized**
- the `public static void main(String[] args)` method is invoked
- this will trigger loading, linking and initialization of additional classes and interfaces

### Loading, Linking and Initializing
- **loading**: finding the binary representation of a class or interface type with a given name and creating a `Class` object from it: this is the process that trasform the bytecode of a class into the internal representation specific to the current implementation of the jvm
	- mind that the policy of class loading depends on the specific implementation
- **(dynamic) linking**: taking a class or interface and combining it into the runtime state of the JVM so it can be executed
- **initialization**: executing the class/interface initialization method <`clinit`> 
	- there could be static blocks to be executed at loading time, ...

#### Loading
This is a two phase process:
- parse the binary data from the .class file according to specification, then traduce it in the internal representation
- create an instance of the hook object `Class`

The creation of a class or interface $C$ is trigghered by
- other class or interface that refers to $C$
- certain methods (eg. [[Java Reflection]])

Mind that arrays classes are generated automatically by the JVM, which generate and load the class on demand

When the creation is trigghered: 
- check whether the class is already loaded
- if not, invoke the appropriate loader.loadClass (the reference is in the constant pool)
	- each class is tagged with the initiating loader

##### Class Loader Hierarchy
- **bootstrap classloader**: loads the main class and basic java APIs. For the latter It may skip much of the validation that gets done for normal classes since the APIs are already validated
- **extension classloader**: load classes from standard Java extention APIs, such as security extension functions
- **system classloader**: is the default application classloader which loads application classes from the classpath
- **user defined classloaders**: the user can define various classloaders for his specific needs

##### The Runtime Constant Pool
The [[Java Virtual Machine#The Static Constant Pool]] is used to construct the *runtime constant pool* upon class/interface creation (after loading)

All references in the runtime constant pool are initially symbolic, they are resolved upon usage. The symbolic references are derived from the .class file
Class names are those returned by `Class.getName(wantClassName)`. 
Mind that the method references are made of name, method descriptor and class name

#### (Dynamic) Linking 
The reference to the constant pool inside the activation record helps to support the **dynamic linking**. 
Dynamic because it is done when the program start to run and not at compile time, this is because the static linking could affect the portability of Java
When a Java class is compiled all references to variables and methods are stored in the class's constant pool as symbolic references: in the bytecode there are logic addresses and they are resolved only at runtime during the execution 

The specific JVM implementation can choose when to resolve symbolic references:
- **eager or static resolution**: when the class file is verified after being loaded. 
	- if at loading phase the verifier finds an unresolved reference we resolve it
	- the problem is that we resolve addresses that may be never used (think to if branches)
- **lazy or late resolution**: when the symbolic reference is used for the first time it gets resolved

Mind that by specific the JVM has to behave as if the linking was done lazily. If you use the eager resolution you have to wait until a reference is used for the first time to throws error that you know from the loading phase (if there are errors)

The **binding** is the process of an entity (field, method or class) identified by a symbolic reference being replaced by a direct reference ([[Binding]])
This only happens once since after the first translation the symbolic reference is substitutited with its direct reference in the whole constant pool

*The linking is made of three steps:*
- [[Java Virtual Machine#Verification]]
- **preparation**: allocation of storage (method tables). Once the verification is done the JVM allocates the memory for the class variables and initialize them to the default value according to the type
	- mind that this has nothing to do with the user-defined values
- **resolution**: optional step, resolve symbol references by loading referred classes/interfaces (only with eager resolution, otherwise this is postponed untill the first use of the reference)

##### Verification
The verification is needed since we have no guarantee that the class file was generated by a Java Compiler and we can't know if the class file was already verified (class files are meant to be shared, this means that you also have to be careful). This process also enhances runtime performance. 

With verification we check that:
- there are no operand stack over/under-flows
- all local variables uses and stores are valid
- the arguments to all the JVM instructions are of valid types
- ...

The verification **mainly** takes place during the loading and the linking process (hence it happens at runtime)
- **at loading time**: this is a syntax step, we check that the file is properly formatted and all its data are recognized by the JVM
- **at linking time**: 
	- checks that do not involve instructions
		- final classes are not subclassed, final methods are not overridden
		- every class has a superclass (besides `Object`)
		- all field references and method references in the constant pool have valid names, classes and type descriptor
		- variables are initialized
		- typechecking on the method parameters based on the signatures
		- ...
	- checks that do involve instructions
		- data-flow analysis on each method
		- check that at any given point in the program, no matter which is the code path taken to reach that point
			- the operand stack have no over/under-flow
			- no local variable is accessed unless it is known to contain a value of an appropiate type
			- methods are invoked with the appropiate arguments
			- ...
	- **at runtime**: the first time a method is actually invoked a check is done by the JVM (runtime process because it requires the resolution of the references)
		- the referenced method/field exists in the given class
		- the currently executing method has access to the referenced method/field

#### Initialization
The `<clinit>` initialization method is invoked on classes and interfaces to initialize class variables
- the syntax is specified by the JVM: it is a static method that set to the standard values the static fields (eg. `int` are 0 by default before the user initialization)

Direct superclasses need to be initialized prior. 
This can be tricky, keep in mind that :
*A reference to a static field causes initialization of only the class or interface that actually declares it, even though it might be referred to through the name of a sublcass, subinterface or a class that implements an interface*. 
An example of this behaviour is the following code
```java
class Super{
	static int taxi = 1729;
}

class Sub extends Super{
	static { // static block, executed at initialization time
		System.out.print("Sub");
	}
}

class Test{
	public static void main(String[] args){
		System.out.println(Sub.taxi);
	}
}
```
The code only prints "1729" even tho we have a direct access to a field of the superclass. 

Inizialization happens on direct use with method invocation, construction and field access. 

We have `<init>`, the initialization methdod for instances
- `invokespecial` instruction
- can be invoked only on uninitialized instances

## JVM Exit 
### (Objects) Finalization
*The finalization process happens whenever an object have to be deallocated, not only on JVM Exit.*

With Finalization we mean the invocation of the method `finalize()`, which is invoked just before the garbage collection. 
`finalize()` is used to relaase resources (such as locks) when an object is no more reachable (and hence it will be deallocated by the GC)

- the body of `finalize()` is user defined
- `finalize()` is invoked only on instances

The Java Language Specification do not define when `finalize()` is invoked since the GC is implementation dependant. It is also not defined if/which thread is going to execute it. 
As a guide line, `finalize()` has to be invoked before putting the memory portion dedicated to that instance in the list of the free allocable memory. 

Unlike Initialization here there is no automatic invocation of super's finalizers 
- it is pragmatics to call `super.finalize()` in the body of `finalize()`

Each object can be:
- **reachable**: there is a reference to the object somewhere on the code
- **finalizer-reachable**: the object can be reached only in the body of `finalize()`, in the rest of the code the object is unreachable
- **unreachable**: the object can't be reached (can for be deallocated)
- **unfinalized**: `finalize()` was not called and the object is still reachable, can't be finalized yet
- **finalizable**: can call `finalize()` since it was never called and the object is unreachable
- **finalized**: `finalize()` was called by the JVM and it will not ba called again (if not explictly by the dev)

### Exiting
*This is what the JVM do when the program in execution terminates:*
- **classFinalize**: similar to object *finalization*
- a class can be unloaded when
	- no instances exist
	- the class object is unreachable

The JVM exits when
- all its non-deamon threads terminate
- it is invoked `Runtime.exit` or `System.exit`

It is possible to set the invocation of `finalize()` on all objects just before exit
## JVM Instruction Set
*really really short notes*

**Basic instructions** that are executable on the virtual machine. They are the analogue of the instructions in machine language.

Properties:
- **Variable length**: **one-byte opcode** followed by **arguments**, against the stack machine of JVM (32 bit); 
- May contain **symbolic references**;
- Only **relative branches** (no GOTOs, jumps are done by only summing/subtracting values to the PC);
- **Byte aligned**: a data is x-byte aligned if it has a number of bytes which is multiple of x. The instruction set is 1-byte aligned, saving a lot of space but in order to read everything we must read 1 byte at a time;

Examples:
- **Load** and **store** move back and forth data between the operand stack (where the stack machine JVM operates) and the local variable array;
- Arithmetic, control transfer, method invocation and return...

Moreover, each instruction can have **different forms** (like different constructors), e.g. different forms of "iload".

There are 3 addressing modes:
	- **Intermediate**, a constant is part of instruction (e.g. iload_1: we know that we refer to the element in position 1 of the LVA);
	- **Indexed**, access variable from local variable array;
	- **Stack**, pop operand stack.
	
OpCodes are **explicitly typed**: the first letter of the opCode identify the type. However, non-supported types will have to be converted, almost no support for *byte*, *char* and *short*. Instead, *int* is used as "computational type". 
