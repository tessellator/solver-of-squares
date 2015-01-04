(ns solver-of-squares.heap
  (:refer-clojure :exclude [pop! swap!])
  (:require [solver-of-squares.platform :refer [max-value]]))

(defn swap!
  "Swaps the values of a transient collection at the specified indices."
  [trans idx1 idx2]
  (let [a (nth trans idx1), b (nth trans idx2)]
    (assoc! trans idx1 b idx2 a)))

(defn- sift-up! [f A i]
 (let [idx (dec i) pidx (dec (int (/ i 2)))]
   (if (zero? idx)
     A
     (if (< (f (nth A idx)) (f (nth A pidx)))
       (recur f (swap! A idx pidx) (inc pidx))
       A))))

(defn- sift-down! [f A i]
 (let [c (count A) t2 (* 2 i) t (dec t2)]
   (if (> c t)
     (let [l (f (nth A t))
           r (if (= c t2) max-value (f (nth A t2)))]
       (if (> (f (nth A (dec i))) (min l r))
         (let [smallest (if (< l r) t t2)]
           (recur f (swap! A (dec i) smallest) (inc smallest)))
         A))
     A)))

(defn insert! [f A v]
 (let [c (count A)]
   (sift-up! f (conj! A v) (inc c))))

(defn pop! [f A]
 (let [c (count A)]
   (sift-down! f (clojure.core/pop! (swap! A 0 (dec c))) 1)))
