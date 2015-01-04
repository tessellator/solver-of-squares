(ns solver-of-squares.core-test
  (:require [clojure.test :refer :all]
            [solver-of-squares.core :refer :all]))

(deftest test-history
  (let [h (history (with-meta [] {:history [:red]}))]
    (is (= h [:red]))))

(deftest test-change-position
  (are [dir result] (= (change-position [0 0] dir) result)
       :left [-1 0]
       :right [1 0]
       :up [0 1]
       :down [0 -1]))

(deftest test-estimate-remaining-cost
  (let [goal {:red [0 1] :blue [3 3]}
        state {:red {:position [1 2] :direction :up}
               :blue {:position [1 3] :direction :left}
               :gray {:position [1 1] :direction :right}}]
    (is (= (estimate-remaining-cost goal state) 4))))
