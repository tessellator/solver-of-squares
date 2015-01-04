# Creating Levels

Creating a level is a easy; in fact, a level is just a map with three keys:
`:goal`, `:blocks`, and `:arrows`!  A couple of examples are listed in the
`solver-of-squares.levels` namespace for you to use as a reference.

The value associated with `:goal` is another map with keys being color names
(e.g., `:red`) and the values as a vector of x-y coordinates of the associated
goal points on the board.  (I pick a significant lower-left corner as being
[0 0].)  What terms you use for the colors does not matter, so long as you are
consistent with their use throughout the level definition.

The map associated with `:blocks` is used to describe the current locations
and directions of the blocks in play. Each key is a color name and its
associated value is a map containing `:position` (and an associated position
vector) and `:direction` (and an associated direction keyword; one of `:left`,
`:right`, `:up`, and `:down`).  (Note: Not every color block must have a goal
state!)

Finally, the value associated with `:arrows` is a map whose keys are x-y vectors
that describe the position of a direction-changing arrow and whose values are
the direction of the associated arrow.  If there are no arrows in the level,
this is simply an empty map.
