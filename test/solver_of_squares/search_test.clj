(ns solver-of-squares.search-test
  (:require [clojure.test :refer :all]
            [solver-of-squares.search :as s]))

(deftest test-manhattan-distance
  (testing "that the calculation is correct"
    (are [x y z] (= (s/manhattan-distance x y) z)
         [0 0] [0 0] 0
         [1 0] [0 0] 1
         [1 0] [0 1] 2
         [-1 0] [0 1] 2)))
