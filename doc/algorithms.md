# Algorithms

## A* Search
To solve a level, the application uses the [A* search algorithm]
(http://en.wikipedia.org/wiki/A*_search_algorithm), and the implementation is
in the `solver-of-squares.search` namespace.  The implementation is general
and could easily be used for other problems.

The heuristic currently used is simple [Manhattan distance]
(http://xlinux.nist.gov/dads//HTML/manhattanDistance.html).  However, it does
not provide an *admissible* heuristic because it can over-estimate the number
of moves required to get all blocks into the goal position.  (For example, it
does not take into account that moving one block may move another.)  While the
heuristic is simple and performs well for most levels, it cannot guarantee to
provide an optimal (shortest-path) solution.

If you think you have a better heuristic, please open an issue and let's explore
it together!

## Transient Priority Queue
Each step in A* expands the next-smallest unexpanded node.  I tried a few
different approaches before settling on a transient priority queue
implementation.  Each approach is discussed in turn.

My original implementation was to use the first state in the list (the one
with the smallest total cost), conj the children states onto the rest of the
list, then sort the list.  This causes _O(nlgn)_ operations on average per
recursion.  Since the list is nearly sorted (just the few out of place at the
end), this approach uses way more operations than should be necessary.  It was
also very slow - solving level 11 (provided with this project) took 10+ minutes
of execution before I bailed out without an answer.

My next approach was to look at using a sorted-set or sorted-map.  The problem
with them is that the keys would have to be the total cost, and there might
(and usually are) several different states with the same total cost.  I did not
want to lose these states (overwritten since associated with the same key).

After looking at these options, I decided to implement a priority queue.  I
thought about using Java's priority queue, but decided against it because I did
not want something that mutated in-place.  (You will see later that I did just
that anyway.)

My first attempt at building a priority queue was a bit of a disaster.  I did
recognize the need to perform transient functions at least within a single step
of the algorithm, but I wanted to build the structure of the internal heap to
play to the strengths of vector (add/remove from the end in constant time).  To
achieve this, I attempted to build what I called an inverse queue - that is, a
normal heap but its definition reversed so the top element in the heap would be
located at the end of the vector.

Given the heap
```
      A
    /   \
   B     C
  / \   /
 D  E  F
```

a normal representation would be `[A B C D E F]`, but an inverse representation
would be `[F E D C B A]`.  This seemed to work well at first.  I could create
the heap in _O(n)_ time, and pulling the smallest element would be in _O(1)_
time.  After the new elements were created, I would conj one of them onto the
vector and sift down (an _O(lgn)_ operation).  However, this approach led to
incorrect results because when a conj occurred that changed the number of
elements in the tree since the last heapify operation had been run, it changed
the interpretation of the heap.  (It was essentially adding a new node at the
top of the heap and then restructuring the heap with different parent-child
pairings.

For example, given the inverse heap `[F E D C B G]` (after element A has been
popped off the heap), sifting down will create a correct new inverse heap
`[F E G C D B]`.  Repeating the process for element H, however, presents a
problem.

Before conj'ing element H:
```
        B
      /   \
     D     C
    / \   /
   G  E  F
```


After conj'ing element H:
```
        H
      /   \
     B     D
    / \   / \
   C   G E   F
```

These are obviously two very different heaps (and the second *could* have had
errors).

The solution was to heapify after the new states were added.  This led to
correct results and ran in _O(n)_ time, an improvement over _O(nlgn)_ from
before.  However, this approach was still performing more operations than
necessary because it did not take advantage of the partial heap between states.
(In fact, running min on the list at each step would have likely been a bit
faster.)

The final solution falls back to a vanilla min-heap approach, with the smallest
element as the first in the vector.  To pop it off the vector, switch it with
the last value (_O(1)_), pop it off the vector (_O(1)_), and sift the large
value down the tree (_O(lgn)_).  Inserting a new element is as simple as pushing
it onto the end of the vector (_O(1)_) and then sifting it up the heap
(_O(lgn)_).  The result is a series of operations that occur in _O(lgn)_ time,
which scales well as the problem space grows.

Since this solution needs to directly manipulate the vector in order to be
efficient, Clojure transients are used extensively.  It could be argued that
the resulting solution is equivalent to using the java.util.PriorityQueue.

