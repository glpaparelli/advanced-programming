# C++ Templates
(Universal Parametric) [[Polymorphism]] of C++: overloading
- function and class templates; type variables
- each concrete instantiation produces a copy of the generic code, specialized for that type: the compiler produce several instantiated version of the code w.r.t the type used

## Function Templates in C++
**Support parametric polymorphism and the type parameters can also be primitive types** 

*example*
```cpp
// T is a class variable, then T can be used as a type variable
template <class T> 
T sqr { return x * x; }
```

The compiler/linker automatically generates one version for each parameter type used by a program: at compile time when the compiler finds that the generic function is used with a specific type it generates the corresponding instantiated code. This means that if a generic function is never used the specific code is never generated.
Parameter types are inferred or indicated explicitly (necessary in case of ambiguity). 

```cpp
int a = 3; 
double b = 3.14;

// generates: int sqr(int x){ return x * x; }
int aa = sqr(a);

// generates: double sqr(double x){ return x * x; } 
double bb = sqr(b);
```

Works for user-defined types as well
```cpp
class Complex{
public: 
	double real; double imag;
	Complex(double r, double im): real(r), imag(im){};

	//overloading of *
	Complex operator*(Complex y){
		return Complex(
			real * y.real - imag * y.imag,
			real * y.imag + imag * y.real
		);
	}
}
// --------------------------------------------------------- //

{ // ...
	Complex c(2, 2);
	Complex cc = sqr(c);
	cout << cc.real << " " << c.imag << endl;
  // ...
}
```

## Templates vs Macros
Macros can be used for polymorphism in simple cases:
```cpp
#define SQR(T) T SQR(T x) { return x * x; }
SQR(int); // int sqr(int x) { return x * x; }
SQR(double); // double sqr(double x) { return x + x; }
```
Macros are executed by the preprocessor, templates by the compiler. 
Macros expansion visibile compiling with option `-E`
Preprocessor makes only (possibly parametric) textual substitution: no parsing, no static analysis check.

**Macros have limits:**
```cpp
#define sqr(x) ((x) * (x))
int a = 2;
int aa = sqr(a++);
```
The previous code is tricky: `sqr(a++)` is expanded as `(a++) * (a++)`
- the first `a++` returns 2 and then `a` is incremented to 3
- the second `a++` is evaluated as 3 and then `a` is increpmented to 4.

With a function we would have that `aa` is equal to 4, while with macro expansion we have that it is equal to 6. 
Side effects are duplicated with macros. For the same reason now `a` is 4 while it should be 3. 

Also: *recursion is not possible with macros*:
```cpp
#define fact(n) (n == 0 ? 1 : fact(n - 1) * n)
```
The compilation fails because `fact` is not defined: `fact(3-1)` is an undefined function since the macro expansion is not recursive
## Non-Type Template Arguments
The template parameters can aslo include expressions of a particular type:
```cpp
// here the template parameter is not only the type variable T
// but also the primitive type int
template <Class T, int N>
T fixed_multiply(T val){
	return val * N;
}

int main(){
	//Class T: <int, N> 
	//int N: (10)
	std::cout << fixed_multiply<int, 2>(10) << '\n'; // 20
}
```
The value of template parameters is determined on compile-time, the second template argument needs to be a constant expresion.


## Template Partial Specialization 
Template Specialization: specify an algorithm for a more specific type.

A (function or class) template can be specialized by definiing another template with:
- same name 
- more specific parameters: partial specialization
- no parameters: full specialization

In this way we can use better implementation for specific kinds of types.
As intuition: similar to overriding (without the concepts of inheritance)

*examples:*
```cpp
// primary template
template <typename T> class Set{
	// use binary tree
};

/*
 full specification: this template has not the type variable,
 it is not generic. Sintattically a template without type variables 
 is introduced as template<>.
 If Set is used with T = char it will be used this template
*/ 
template <> class Set<char>{
	// use a bit vector
}

/*
 partial specification: T* is a pointer to a type T, 
 more specific than the primary template (over pointes of type T and not
 simply on type T) but less specialized that the previous one
*/
template <typename T> class Set<T*>{
	//use hash table
}
```

## Template Metaprogramming 
Templates can be used by a compiler to generate temporary source code, which is merged by the compiler with the rest of the source code and then compiled.
- **only constant expressions**: they have to be computable at compile time. 
- **no mutable variables**: same reason
- no support by IDE's, compilers and other tools

### Computing at Compile Time
*C++ function that compute sum of first n integers*
```cpp
#include <iostream>
int triangular(int n){
	return (n == 1) ? 1 : triangular(n - 1) + n;
}

int main(){
	int result = triangular(20);
	std::cout << result << '\n';
}
```

*C++ template with specialization computing sum of first n integers*
```cpp
#include <iostream>

template <int t>
constexpr int triangular(){
	return triangular<t - 1>() + t; 
}
//base case
template <>
constexpr int triangular<1>(){
	return 1;
}

int main(){
	int result = triangular<20>();
	std::cout << result << 'n';
}
```
- `constexpr` invites the compiler to evaluate the expression

With the template specialization in the compiled code we actually find the result of `triangular<20>()`, which is 210.
Since templates generates code, when we execute the first snippet we have a lot of code generation (the compiler generates the code for `tirangular(20)`, `triangular(19)`, ...).
Instead with the second snippet we have a long compilation time (the compiler has to compute the triangular number of 20) but the size of the compiled code do not explode. 