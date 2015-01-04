(ns solver-of-squares.heap-test
  (:require [clojure.test :refer :all]
            [solver-of-squares.heap :as h]))

(deftest test-swap!
  (testing "that a swap does actually occur"
    (let [c (-> (transient [1 2 3 4])
                (h/swap! 0 2)
                (persistent!))]
      (is (= c [3 2 1 4])))))

(deftest test-insert!
  (testing "that insert preserves the heap"
    (let [c (persistent! (h/insert! identity (transient [3 8 5]) 1))]
      (is (= c [1 3 5 8])))))

(deftest test-pop!
  (testing "that pop preserves the heap"
    (let [c (persistent! (h/pop! identity (transient [1 3 5 8])))]
      (is (= c [3 8 5])))))
