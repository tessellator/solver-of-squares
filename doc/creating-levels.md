# Creating Levels

Creating a level is a easy; in fact, a level is just a map with three keys:
`:goal`, `:state`, and `:arrows`!  A couple of examples are listed in the 
`solver-of-squares.levels` namespace for you to use as a reference.
 
The value associated with `:goal` is another map with keys being color names 
(e.g., `:red`) and the values as a vector of x-y coordinates of the associated 
goal points on the board.  (I pick a significant lower-left corner as being 
[0 0].)  What terms you use for the colors does not matter, so long as you are
consistent with their use throughout the level definition.

The map associated with `:state` has two key-value pairs.  The first is
`:history` which should have an empty vector as a value.  This will be populated
as the algorithm searches for a solution.  The other key is `:blocks`, and it is
used to describe the current locations and directions of the blocks in play.
Its value is a map whose keys are a color name and whose values are maps of
`:position` (and an associated position vector) and `:direction` (and an 
associated direction keyword; one of `:left`, `:right`, `:up`, and `:down`).
(Note: Not every color block must have a goal state!)

Finally, the value associated with `:arrows` is a map whose keys are x-y vectors
that describe the position of a direction-changing arrow and whose values are
the direction of the associated arrow.  If there are no arrows in the level,
this is simply an empty map.