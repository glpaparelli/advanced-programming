import os
import time
import statistics
from threading import Thread

"""
- n_threads: number of threads to launch
- seq_iter: number of times func has to be invoked by each thread
- iter: number of times where the execution of all the threads has to
  be executed (each thread is executed iter times and each thread call invoke
  func seq_iter times)
"""
def bench(n_threads=1, seq_iter=1, iter=1):
    def inner(func):
        def wrapper(*args, **kwargs):
            it_times = []
            for n in range(iter):
                threads = []
                it_start = time.perf_counter()
                #create the threads, then start it and then wait for all threads
                for i in range(n_threads):
                    t = Thread(
                        target=lambda: [func(*args, **kwargs) for _ in range(seq_iter)]
                    )
                    threads.append(t)
                    t.start()
                for t in threads:
                    t.join()
                it_times.append(time.perf_counter() - it_start)

            dict = {
                "fun": func.__name__,
                "args": args,
                "n_threads": n_threads,
                "seq_iter": seq_iter,
                "iter": iter,
                "mean": statistics.mean(it_times),
                "variance": statistics.variance(it_times, statistics.mean(it_times)),
            }
            return dict
        return wrapper
    return inner

# NOOP for n/10 seconds
def just_wait(n): 
    time.sleep(n * 0.1)

# CPU intensive
def grezzo(n): 
    for i in range(2**n):
        pass

def test(iter, fun, args):
    for i in range(4, 0, -1):
        n_threads, seq_iter = 2 ** (4 - i), 2**i
        filename = f"{fun.__name__}_{str(args)}_{n_threads}_{seq_iter}.txt"

        path = os.path.dirname(os.path.realpath(__file__))
        path = os.path.join(path, "output", filename)

        with open(path, "w") as f:
            res = bench(n_threads, seq_iter, iter)(fun)(args)
            f.write(str(res))


test(10, grezzo, 20)
test(10, just_wait, 1)

"""
- Conclusions

Benchmarking with two functions, a CPU heavy one (grezzo) and a CPU light one (just_wait)
we get the following results

1) with the CPU light function (just wait) we have a boot in performance (a reduction of the 
   mean of execution times) with the increase of threads. 
   The function time.sleep simply halts the current thread and make it wait for the specified 
   time. With one thread we have that the whole process is put on wait for every iteration. 
   With more threads that compete with each other the execution time is greatly reduced since 
   sleep halts one thread, while the others can be executed. 
   A parallel wait is much more efficient than a "sequential" wait.

2) with the CPU heavy function (grezzo) we do not have an increase in performance with the 
   increase of threads. This is a result of the GIL that prevents the threads to operate in 
   parallel. There is actually a decrease of performance with the progressive increase of threads, 
   which is a result of the overhead given by the handling of threads (scheduling and such). 
   With heavier tasks (or with a slower CPU) we could see that the performance decrease way more 
   significantly than fractions of seconds.  

"""