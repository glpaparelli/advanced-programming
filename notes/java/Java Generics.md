# Java Generics
Java Generics are the Java way to [[Polymorphism]], specifically in Java we have *universal -> parametric -> explicit -> bounded (invariant)* polymorphims

Classes, interfaces and methods can have **Type Parameters** (in the following example `E`) which can be used arbitrarily in the definition:
```java
interface List<E>{
	boolean add(E element);
	E get(int index);
}
```
And they can be instatiated by providing arbitrary (reference) type arguments (reference types: a reference to an object):
```java
List<Integer>
List<String>
...
```

The "declaration" of a generic type is *explicit*, meaning that if we want to use a generic type `E` we have to use the explicitly show it (not as in Haskell, where everything is basically generic)

### Type Erasure
All type parameters of generic types are transformed to `Object` or to their first [[Java Generics#Bounded Type Parameters]] after compilation. 
The main reason is the backward compability with the legacy code, the result is the fact that at runtime all the instances of the same generic type have the same type. 

*example:*
```java
List<Integer> intList = new ArrayList<Integer>();
List<String> strList = new ArrayList<String>();

intList.getClass() == strList.getCLass() // true
```

### Generic Methods
Methdos can use the type parameters of the class/interface where they are defined, if any:
```java
interface List<E>{
	boolean add(E element);
	E get(int index);
}
```
The type `E` is generic and used by the methdos of the interface/class itslef

Methods can also introduce their own type parameters, as:
```java
public static <T> T getFirst(List<T> list)
```
The generic type `T` is visible only inside this method and this allow us to maintain generic abstraction of single methods

### Bounded Type Parameters
```java
class NumList<E extends Number> {
	void m(E arg){
		arg.intValue();
	}
}
```
When we want to instantiate a `NumList` object we have to use as type parameter a class that implements `Number`. This way we can invoke methods that are surely defined since inherited,

#### Type Bounds
- **upper bound**: `<TypeVar extends SuperType>`
	- `SuperType` and any of its subtype are ok
- **multiple upper bounds**: `<TypeVar extends ClassA & InterfaceB & ...>`
	- single inheritance in Java, syntattically first the class and then the interfaces
- **lower bound**: `<TypeVar> super SubType`
	- `SubType` and any of its supertype are ok

*Type bounds for methods guarantee that the type argument supports the operations used in the method body*

### Invariance, Covariance and Controvariance
In Java we know that `Integer` is a subtype `Number` but *it is not true* that `List<Integer>` is a subtype of `List<Number>`.
Given two concrete types `A` and `B`, `Class<A>` *has no relationship* with `Class<B>`, regardless of any relationship between `A` and `B`

**Formally**: subtyping in Java is *invariant* for generic classes

- **invariant**: the relationship between `A` and `B` do not influence the relationship between `Class<A>` and `Class<B>`
- **covariant**: if `A` is a subclass of `B` then `Class<A>` is a subclass of `Class<B>`
- **controvariant**: if `A` is a subclass of `B` then `Class<B>` is a subclass of `Class<A>`, there is an inversion of relation 

On the other hand, as expected, if `A` extends `B` and they are generic classes then for each type `C` we have that `A<C>` extends `B<C>`
- `ArrayList<Integer>` *is a subtype* of `List<Integer>`

#### Covariance and Controvariance
Let's say that we have the interface:
```java 
interface List<T> {
	boolean add(T elt);
	T get(int index);
}
```
Hence, when instantiated with `Number` and `Integer` we have that:
- `List<Number>`
	- `boolean add(Number elt)`
	- `Number get(int index)`
- `List<Integer>`
	- `boolean add(Integer elt)`
	- `Integer get(int index)`

Let's now check the following ipothetic code:
```java 
List<Integer> intList = new ArrayList<>();
List<Number> numList - new ArrayList<>();

// statement 1
numList = intList;

// statement 2 
intList = numList;

// statement 3
numList.add(new Number(...));

// statement 4
Integer n = intList.get(0);
```
We have 
- **statement 1**: with covariance it would be possible since `Integer` extends `Number` 
	- the **statement 3** would allow to add a `Number` to a list of `Integer` but `Number` is not a subtype of `Integer`. With dynamic dispatching on `numList` it would be invoked the add of `List<Integer>` and a type error would be raised
- **statement 2**: with controvariance it would be possible since the relationship between `List<Integer>` and `List<Number>` would be the reverse of the relationship between `Integer` and `Number`
	- in the **statement 4** `intList.get(0)` would return a `Number`, which is a supertype of `Integer` and hence the assigment fails

The *substitution principle* ([[Polymorphism#Inclusion]]) is violated by both covariance and controvariance, hence **the Java choice to use only invariance is correct** here. 


##### Where Covariance would be Safe
```java 
interface List<T> {
	T get(int index);
}
```
Hence, when instantiated with `Number` and `Integer` we have that:
- `List<Number>`
	- `Number get(int index)`
- `List<Integer>`
	- `Integer get(int index)`

In this case a covariant setting, where `List<Integer>` is a subtype of `List<Number>` would be safe. 
*In general: covariance is safe if the type is read-only*

##### Where Controvariance would be Safe
```java 
interface List<T> {
	boolean add(T elem);
}
```
Hence, when instantiated with `Number` and `Integer` we have that:
- `List<Number>`
	- `boolean add(Number elem);`
- `List<Integer>`
	- `boolean add(Integer elem);`

In this case a controvariant setting, where `List<Integer>` is a supertype of `List<Number>` would be safe. 
*In general: controvariance is safe if the type is write-only*

### Java Arrays
Arrays are like built-in containers. Assume that `Type1` is subtype of `Type2`, how are related `Type1[]` and `Type2[]`? 

Consider the following generic class, mimicking arrays:
```java
class Array<T>{
	public T get(int index){...}
	public void set(T newVal, int index) {...}
}
```
According to the Java rules we have seen so far we know that `Array<Type1>` and `Array<Type2>` is not related by subtyping. 

But the previous rules do not apply in this cases: if `Type1` is subtype of `Type2` then `Type1[]` is subtype of `Type2[]`. 
*Java arrays are covariant.*

Why? Think to the method `void sort(Object[] array)`. This works because `Object[]` is the supertype of any array, it exploit the covariance. 
Without the covariance we would need to define a new method `sort` for evert array! Sorting do not insert new objects in the array, it is a read-only operation, which we have seen to be safe with covariance. 

#### Problems with Array Covariance
Even if the array covariance works for sort it may cause type errors in other cases:
```java
// defined classes Fruit, Apple and Pear (subtypes of Fruit)

Apple[] apples = new Apple[1];
Fruit[] fruits = apples // ok by covariance

fruits[0] = new Pear();
```
The last row compiles since statically the types are ok: `Pear` is subtype of `Fruit`. The problem is the fact that the static type of `apples` is `Apple[]` and `apples` and `fruits` are alias. 
While the static type of `fruits` is `Fruit[]`, `fruits` referes to `apples` and `Pear` is not a subtype of `Apple`. 

The previous code breaks a general **Java rule:** for each reference variable the dynamic type (type of the object referred by it) must be a subtype of the static one (type of the declaration).

The dymaic type of an array is known only at runtime: every array update includes a runtime check, assigning to an array element an object of a non compatible type throws a `ArrayStoreException`

This scenario also shows why *Java do not supports generic arrays*: the dynamic type of an array is known only at runtime but the generics at runtime are casted to `Object` due to type erasure. 
This would result in non-detectable errors and since Java is type-safe generics array are not supported

### Wildcards "?"
A wildcard is an anonymous variable
- `?` is of unknown type
- wildcards are used when a type is used exactly once and the name is unknown
- they are used for *use-site variance*: they provide on-spot co/contra-variance functionality

#### Syntax of Wildcards
- `? extends Type`: denotes an unknown subtype of `Type`
- `?`: shorthand for `? extends Object`
- `? super Type`: denotes an unknown supertype of `Type`

#### Wildcards for Covariance
Invariance of generic classes is correct but restrict (it is bad for code reuse), wildcards can alleviate the problem. 

*example*: 
```java
interface Set<E>{
	//adds to this all the elements in c (discarding duplicates)
	void addAll(??? c);
}
```
what is a "general enough" type for the method `addAll`?
- `void addAll(Set<E> c)`: ok, but what if I want to add to the set all the elements of a list `List<E> c`, or what if I want to add all the elements of a `Queue<E>`? 
- `void addAll(Collection<E> c)`: better solution since `List`, `Queue`, ... are all subtypes of `Collection`. The problem is that it do not take into account the elements accepted by covariance. The elements of a `List<T>` with `T` subtype of `E` should be allowed to be added to the set
- **best solution:** `void addAll(Collection<? extends E> c`
	- match any collection of elements of type subtype of `E`

##### What about Type Safety?
```java 
List<Apple> apples = new ArrayList<Apple>();
List<? extends Fruit> fruits = apples; 
fruits.add(new Pear()); // compile-time error
```
- `? extends Fruit` match every sublcass of `Fruit` (the compiler can't know anything else), and being covariance the compiler won't allow the modificatoin of `fruits`
- we can't do `fruits.add(new Pear())` for **two** reasons
	-  the compiler can't know the type of `fruits` at static type
		- the compiler won't allow the modification of `fruits`

#### When should Wildcards be used?
The "**PECS**" Principle: **P**roducer **E**xtends, **C**onsumer **S**uper
- use `? extends T` when you want to get values (from a producer): supports covariance 
	- [[Java Generics#Wildcards for Covariance]]
- use `? super T` when you want to insert values (in a consumer): support contravariance 
- do not use `?` (`T` is enough) when you both obtain and produce values

*example: the most general type of copy*
```java
<T> void copy(List<? super T> dst, List<? extends T> src); 
```
We exploit covariance for `src` and contravariance for `dst`

#### The Price of Wildcards
A wildcard type is anonymous/unknown, and it reduce the possibility of reading/writing of objects due the restriction of covariance and contravariance

##### The Price of Wildcard Covariance
```java
List<Apple> apples = new List<Apples>();
List<? extends Fruit> fruits = apples; // covariance

fruits.add(new Pear());  // A
Fruit f = fruits.get(0); // B
fruits.add(new Apple()); // C
fruits.add(null);        // D

```
- **A**: compile-time error
	- seen in [[Java Generics#What about Type Safety]]
- **B**: ok 
	- elements of `fruits` are surely subtype of `Fruit` and hence the can be assigned to `Fruit f`
- **C**: compile-time error
	- `? extends Fruit` match any subclass of `Fruit`, in this case is possible to infer that `Apple` is matched and it the statement would be correct
	- but what if there was an if statement where in the "then" branch we assigned `List<Apple>` to `fruits` and in the "else" branch we assigned `List<Pear>` to `fruits`?
	- also, can't add anything due to covariance constraints
- **D**: ok
	- lists that exploit covariance (as in this case) can't be modified, we can only add `null`

##### The Price of Wildcard Contravariance
```java
List<Fruit> fruits = new ArrayList<Fruit>();
List<? super Apples> apples = fruits; //contravariance

apples.add(new Apple());     // A
apples.add(new FujiApple()); // B
apples.add(new Fruit());     // C
Fruit f = apples.get(0);     // D
Object o = apples.get(0);    // E
```
- the assigment `apples = fruits` is legit: `? super Apples` match every class from `Apple` to `Object` and in particular it matches `Fruit`
- **A**: ok
	- we can add an `Apple` since it is surely subclass of a superclass of `Apple` and this check can be done statically
- **B**: ok
	- as for **A**
- **C**: compile-time error
	- if `? super Apple` is `Apple` than we can't add a new `Fruit` since it is not a subclass of `Apple` (instead it is a superclass of `Apple`)
	- if `? super Apple` is `Fruit` or some higher object thant the types are correct but this can't be known at static time (actually not even at runtime: in Java we have type erasure)
- **D**: compile-time error
	- while the types here are correct we see that `? super Apple` can match any type from `Apple` to `Objet`. In this case it matches `Fruit` but as before there could be some branching and what is matched would be known only at runtime (actually not even at runtime: in Java we have type erausre)
	- the only way where the types are surely allowd is to assign to the most general type of all, `Object`, as in **E**
	- mind that the compiler signal error no matter what since we are reading with contravariance
- **E**: ok
	- check **D**

### Limitations of Java Generics
Mostly due to the concept of type erasure
- **cannot instantiate generics types with primitive types**
	- `ArrayList<int>` do not compile: primitive types are not under `Object`, it would not be possible to perform the type erasure which is key to retrocompatibility
- **cannot create instances of Type Parameters**
	- in `List<T>` we can't do `new T()` since the `new` is done at runtime, and at runtime `T` is `Object`, we would only instantiate `Object`
- **cannot declare static fieds whose types are Type Parameter**
	- `public class C<T>{ public static T local; ... }`: every instance of `C` would have a static `Object` field
- **cannot use `casts` or `instanceof` with Parameterized Types**
	- `list instanceof ArrayList<Integer>` do not compile
	- `list instanceof ArrayList<?>` is ok
... 
