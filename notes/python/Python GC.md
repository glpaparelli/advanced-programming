# Python Garbage Collection
CPython manages memory with a **reference counting** + **mark&sweep** cycle collector scheme:
- **reference counting**: each object has a counter storing the number of references to it. When it becomes 0 memory can be reclaimed. 
- **pros**:
	-  simple implementation 
	- memory is reclaimed as soon as possible, 
	- no need to freeze execution pasing control to a garbage collector
- **cons**: 
	- additional memory needed for each object
	- cycling structures in garbage cannot be identified, thus the need for mark&sweep 
		- **mark&sweep**: at runtime stop the execution and from the variables in you program start visit any reachable object and mark them, after this marking phase check all objects and deallocate the non-marked ones)

## Handling Reference Counters
Updating the refcount of an object has to be done atomically. 

In case of multi-threading you need to synchronize all the times you modify refcounts, or else ypu can have wrong values. 

Synchronization primitives are quite expensive on contemporary hardware. 
*Since almost every operation in CPython can cause a refcount to change somewhere (almost everything is an object) handling refcounts with some kind of synchronization would cause spending almost all the time on synchronization*

As a consequence: [[Python Concurrency]]