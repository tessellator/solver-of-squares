(ns solver-of-squares.platform)

(defn abs [x]
  "Returns the absolute value of x."
  (if (neg? x) (- x) x))

(def max-value
  "Represents the maximum allowable integer value for the platform"
  Integer/MAX_VALUE)