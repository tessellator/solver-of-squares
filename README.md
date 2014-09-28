# A Solver of Squares

This is a small, interactive Clojure application that allows you to design new
levels or solve existing ones in the style of [Game About Squares]
(http://gameaboutsquares.com).

This project is intended to provide a small playground for learning about
Clojure, particularly the use and value of transients.


## Usage

The program is built to be interactive with a REPL.  Simply jump into the REPL
and type the following to find the solution to level 11:

```
(in-ns 'solver-of-squares.core)
(solve level11)
```

I have only provided the problem definitions for levels 11 and 21 (although I
have tested the algorithm with all the problems).  After all, you should try to
solve them yourself first!  Or, design your own levels!

Be sure to check out the docs for information on how to create levels and what
algorithms were used (and sometimes discarded) in developing this application


## License

Copyright © 2014 Chad Taylor

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
