(ns solver-of-squares.algorithms
  (:require [solver-of-squares.platform :refer [abs max-value]]))

(set! *warn-on-reflection* true)


(defn manhattan-distance [current target]
  "Calculates the Manhattan distance between a current and target vector"
  (->> (map (comp abs -) target current)
       (reduce +)))

;;;;;;;;;;; TRANSIENT PRIORITY QUEUE FUNCTIONS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn- tswap! [trans idx1 idx2]
  (let [a (nth trans idx1), b (nth trans idx2)]
    (assoc! trans idx1 b idx2 a)))

(defn- heapify!
  ([f A]
   (heapify! f A (int (/ (count A) 2))))

  ([f A i]
   (if (zero? i) A (recur f (f A i) (dec i)))))

(defn- sift-up! [f A i]
  (let [idx (dec i) pidx (dec (int (/ i 2)))]
    (if (zero? idx)
      A
      (if (< (f (nth A idx)) (f (nth A pidx)))
        (recur f (tswap! A idx pidx) (inc pidx))
        A))))

(defn- sift-down! [f A i]
  (let [c (count A) t2 (* 2 i) t (dec t2)]
    (if (> c t)
      (let [l (f (nth A t))
            r (if (= c t2) max-value (f (nth A t2)))]
        (if (> (f (nth A (dec i))) (min l r))
          (let [smallest (if (< l r) t t2)]
            (recur f (tswap! A (dec i) smallest) (inc smallest)))
          A))
      A)))

(defn- insert! [f A v]
  (let [c (count A)]
    (sift-up! f (conj! A v) (inc c))))

(defn- heap-pop! [f A]
  (let [c (count A)]
    (sift-down! f (pop! (tswap! A 0 (dec c))) 1)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn a*
  ([initial-state succ goal? cost? estimate? unique]
   (a* initial-state succ goal? cost? estimate? unique max-value #{}))

  ([initial-state succ goal? cost? estimate? unique max-cost]
   (letfn [(wrap-state
             [state]
             (let [c (cost? state), e (estimate? state)]
               (hash-map :state state :cost c :estimate e :total (+ c e))))
           (apply-conj!
             [A xs]
             (if (empty? xs) A (recur (insert! :total A (first xs)) (rest xs))))]
     ((fn [queue seen]
        (if-let [i (nth queue 0)]
          (let [state (:state i)
                u (unique state)]
            (cond
              (goal? state) state
              (> (:cost i) max-cost) nil
              (contains? seen u) (recur (heap-pop! :total queue) seen)
              :else (recur (->> (succ state)
                                (map wrap-state)
                                (apply-conj! (heap-pop! :total queue)))
                           (conj seen u))))))
      (transient [(wrap-state initial-state)])
      #{}))))
