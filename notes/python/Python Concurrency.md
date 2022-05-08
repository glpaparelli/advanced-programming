# Python Concurrency
## The Global Interpreter Lock (GIL)
The CPython interpreter assures that only one thread executes python bytecode at a time, thanks to the **global interpreter lock**. 

The current thread must hold the GIL before it can safely access python objects. 
This simplifies the CPython implementation by making the object model (including critical built-in types such as dictionaries) implicitly safe against concurrent access: duh, basically we have no concurrent access, no race conditions whatsoever. 

This obviously costs on performance: parallelism on multi-processors machines is wasted. 

Actually the GIL can cause a degeneration on performance, the system call overhead is significant expecially on multicore hardware: *two threads calling a function may take twice as much time as a single thread calling the function twice*

