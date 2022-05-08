# Java Reflection
*Reflection*: the ability of a program to manipulate as data something representing the state of the program during its own execution. 

*The refelction is made of the composition of:* 
- **introspection**: is the ability of a program to observe and therefore reason about its own state
- **intercession**: is the ability for a program to modify its own execution state or alter its own interpretation or meaning

Both of this aspects require a mechanism for encoding the execution state as a data: providing such encoding is called *reification*

*There are two main types of reflection:*
- **structural reflection**: concerned with the ability of the language to provide a complete reification of 
	- the program currently executed
	- the abstract data types of the program 
- **behavioral reflection**: concerned with the ability of the language to provide a complete reification of
	- its own semantics and implementation
	- data and implementation of the runtime system

*Reflection comes with three main drawbacks:*
- **performance overhead**: reflection is about runtime entities, being at runtime there are no static optimization available
- **secuirty**: reason about the code may affect security (data leakage, modification of the behaviour, ...)
- **exposure of internals**: reflective code may access internals and break abstraction of meant-to-be black boxes

*Reflection comes handy in various situations:*
- **tesing**: get all the methods that start with "test" and invoke them
- **bean**: netbeans and analogous visual tools usa reflection to compose [[Java Beans]]
- ... 

### Java Reflection
Java supports introspection and reflxive invocation but do not allow to change code while running (aka can't change the semantic of the program while running). 

The [[Java Virtual Machine]] accomplish the reflection by keeping an object of class `java.lang.Class` associated to every type (*primitive* (int, ...), *loaded* (instances of loaded classes), *synthesized* (arrays, ...)). 
The `Class` object *reflects* the type it represents. 

The `Class` objects are consturucted automatically by the [[Java Virtual Machine]] as the corresponding classes are loaded and we can retrieve them in three ways:
- `Object.getClass()`
- `type.getClass()`
- `Class.forName(Object)`

Once we retrieve a class object we can get
- the class name
- the class modifiers (private, ...)
- the info on fields, methods and constructors. Each of them have an associated type (`Field`, `Method`, `Constructor`) which implement the interface `Member` (as class member)

#### Reflection for Introspection
##### Class Methods for locating Fields
- `getDeclaredField(String name)`: returns a `Field` object representing the field called `name`. It must belong to the class this and it can be private.
- `getField(String name)`: returns a `Field` object representing the field called `name`. It must be public and it can be inherited. 
- `getDeclaredFields()`: returns an array of `Field` objects reflecting all the fields declared by the class/interface represented by the current `Class` object. In the returned array we find public, protected, default and private fields, but there won't be inherited fields
- `getFields()`: returns an array of `Field` objects reflecting all the accessible public fields of the class and the inherited ones

In order to have all the fields of a `Class` we have to use a combination of `getDeclaredFields()` and `getFields`

##### Class Methods for locating Methdos
- `getDeclaredMethod(String name, Class<?>...parameterTypes)`: returns a `Method` object that reflects the specified method declared in this class
- `getMethod(String name, Class<?>...parameterTypes`: retuns a `Method` object that reflects the specified public method in this class, even if it inherited
- `getDeclaredMethods()`: returns an array of `Method` objects reflecting all (public and private) methdos declared by the class
- `getMethods()`: returns an array of `Method` objects reflecting all the accessible public methods of the class

In order to have all the methdos of the class (both the declared methods no matter the modifier) and the inherited ones we have to use a combination of `getDeclaredMethdos()` and `getMethods()`

###### Generic Methods: effect of erasure
`getMethod(String name, Class<?>...paramTypes)` returns a method object corresponding to the public specified method. 

When we try to execute the following code
```java
LinkedList<String> list = new LinkedList<>();
Class c = list.getClass();
Method add = c.getMethod("add", String.class); 
```
An excpetion is thrown because of the type erasure of the generics. `LinkedList<String>` once compiled is `LinkedList<Object>` and when we search the method with the name "add" that takes a `String` as parameter we get `NoSuchMethodException`

The correct version of the previous code is the following
```java
LinkedList<String> list = new LinkedList<>();
Class c = list.getClass();
Method add = c.getMethod("add", Object.class); 
```

##### Class Methods for locating Constructors
- `getDeclaredConstructor(Class<?>...parameterTypes)`: returns a `Constructor` object that reflects the specified constructor of the class represented by this `Class` object. The parameterTypes parameter is an array of `Class` objects that identify the constructor's formal parameter types in the declared order
- `getConstructor(Class<>>...parameterTypes)`: returns a `Constructor` object that reflects the specifcied public constructor of the class represented by this `Class` objects. The parameterTypes parameter is an array of `Class` objects that identify the constructor's formal parameter types in the declared order
- `getDeclaredConstructors()`: returns an array of `Constructor` objects reflecting all the constructors declared by the class represented by this `Class` object. 
- `getConstructors()`: returns an array containing `Constructor` objects reflecting all the accessible public constructors (inherited too)

Same reasoning: to get all the constructors we have to use a combination of the two last methdos

###### Example: Retrieving Public Construcotrs
```java 
Class c = Class.forName("Dest");

Constructor[] ctors = c.getConstructors();
for(int i = 0; i < publicFields.length, ++i){
	System.out.println("Constructor (");
	
	Class[] params = ctors[i].getParameterTypes() // return list of types of params
	for(int k = 0; k < params.length; ++k){
		String paramType = params[k].getName();
		System.out.print(paramType + " ");
	}

	System.out.println(")");
}
```

#### Reflection for Program Manipulation
##### Using Default Constructors
```java
Rectangle r = new Rectangle();
```
```java
Class c = Class.forName("java.awt.Rectangle");
Rectangle r = (Rectangle) c.newInstance();
```

##### Using Construcotrs with Arguments
The schema to create new instances using constructors with parameters is the following:
- retrieve the Class object associated to the class of which we want to create a new instance
- define an array of types (array of `Class`) associated to the parameters of the constructor
- creates the array of actual parameters of the constructor
- retrieve the `Constructor` object from the `Class` object using the array of types
- invoke the method using the array of actual parameters

```java
Rectangle r = new Rectangle(12, 24);
```
```java
Class c = Class.forName("java.awt.Rectangle");
Class[] intArgsClass = new Class[]{int.class, int.class};
Object[] intArgs = new Object[]{new Integer(12), new Integer(24)};

Constructor ctor = c.getConstructor(intArgsClass);
Rectangle r = (Rectangle) ctor.newInstance(intArgs);
```

##### Accessing Fields
###### Getting Fields Values
```java
Rectangle r = new Rectangle(12, 24);
// h = r.height
Class c = r.getClass();
Field f = c.getField("height");
Integer h = (Integer) f.get(r);
```

###### Setting Field Values
```java
Rectangle r = new Rectangle(12, 24);
//r.width = 30
Class c = r.getClass();
Field f = c.getField("width");
f.set(r, new Integer(30));
```

##### Invoking Methods
```java 
// wanted behaviour 
String s1 = "Hello"; 
String s2 = "World";
String result = s1.concat(s2);
```
```java
Class c = String.class;
Class[] paramTypes = new Class[] {String.class};
Object[] args = new Object[] {s2};
Method concatMethod = c.getMethod("concat", paramTypes);
String result = (String) concatMethod.invoke(s1, args);
```
- `concat` is an instance method: the first param `s1` is the object on which we call the method 

#### Accessing Private Fields
Certain operations are forbidden by privacy rules:
- changing a final field
- reading or writing a private field
- invoking a private method

Such operations fail also if invoked through reflection, the programmer can request that class member to be accessible. This requested is granted if there is no security manager or if the existing security manager allows it.
`AccessibleObject` is the superclas of Field, Method and Constructor and proviedes three methods
- `boolean isAccessible()`
- `void setAccessible(boolean flag)`
- `static void setAccessible(AccessibleObject[] array, boolean flag)`

