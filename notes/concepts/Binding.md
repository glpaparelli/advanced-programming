# Binding as Fast as Possible
*A very fast and dumb reminder of binding*

**Binding**: the process of association between a name (identifier) and an expression. 

Here we talk about the **binding of functions:** the process of association between the function name and the actual implementation that has to be executed. 

The binding of the function name with the actual code can be done at 
- **compile time**: *early binding* (aka static binding)
- **execution time**: *dynamic binding* (aka late binding)

### Early Binding
*If the language is statically typed the binding is done at compile time.* 
For example: [[C++ Templates]], the compiler instantiate the template and binds the invocation with the actual instantiation of the template at compile time, based on the most specific template w.r.t the actual invocation type

### Dynamic Binding
*If the language is dynamically typed the binding is done at runtime binding.*
The binding is done at runtime based on the types of the actual arguments / the most specific type used for the method invocation

**Java uses dynamic binding**: while it might be possible to use early binding in some cases, overriding with subclasses and [[Java Generics]] are bound to runtime information
*example*:
```Java
// Given the Class A with two subclasses, B, C
A a = null;
if(something)
	a = new B();
else 
	a = new C();
```

