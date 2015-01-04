(ns solver-of-squares.search
  (:require [solver-of-squares.platform :refer [abs]]
            [solver-of-squares.heap :as h]))

(set! *warn-on-reflection* true)


(defn manhattan-distance [current target]
  "Calculates the Manhattan distance between a current and target vector"
  (->> (map (comp abs -) target current)
       (reduce +)))

(defn a*
  "Perform A* search and return goal state if found, nil otherwise."
  ([initial-state succ goal? cost? estimate? max-cost]
   (letfn [(wrap-costs [state]
             (let [c (cost? state), e (estimate? state)]
               (vary-meta state assoc :cost c :estimate e :total (+ c e))))

           (apply-conj! [A xs]
             (if (empty? xs) A (recur (h/insert! (comp :total meta) A (first xs)) (rest xs))))]

     (let [cost (comp :cost meta)
           total (comp :total meta)]

       ((fn [queue seen]
          (if-let [state (nth queue 0)]
            (cond
              (goal? state) state
              (> (cost state) max-cost) nil
              (contains? seen state) (recur (h/pop! total queue) seen)
              :else (recur (->> (succ state)
                                (map wrap-costs)
                                (apply-conj! (h/pop! total queue)))
                           (conj seen state)))))

        (transient [(wrap-costs initial-state)])
        #{})))))
