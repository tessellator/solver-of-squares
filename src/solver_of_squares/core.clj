(ns solver-of-squares.core
  (:require [solver-of-squares.search :refer [a* manhattan-distance]]
            [solver-of-squares.levels :refer :all]))
(set! *warn-on-reflection* true)

(def history (comp :history meta))

(defn change-position [[x y] direction]
  (condp = direction
    :left [(dec x) y]
    :right [(inc x) y]
    :up [x (inc y)]
    :down [x (dec y)]))

;; This heuristic is NOT admissible.  Needs a bit of work...
(defn estimate-remaining-cost [goal state]
  (reduce + (map #(manhattan-distance (goal %) (-> state % :position)) (keys goal))))

(defn- resolve-conflicts [state block direction]
  (letfn [(change-pos [color] (change-position (-> state color :position) direction))
          (update-pos [color] (assoc-in state [color :position] (change-pos color)))]
    (let [ks (filter #(not= block %) (keys state))
          conflict (first (filter #(= (-> state block :position) (-> state % :position)) ks))]
      (if conflict
        (recur (update-pos conflict) conflict direction)
        state))))

(defn- update-directions [state arrows]
  (->> (for [x state y arrows] [x y])
       (filter (fn [[[_ {pos :position}] [arr-pos _]]] (= pos arr-pos)))
       (reduce (fn [s [[block _] [_ dir]]] (assoc-in s [block :direction] dir)) state)))

(defn move-block
  ([state block arrows]
   (move-block state block (-> state block :direction) arrows))

  ([state block direction arrows]
   (-> (assoc-in state [block :position] (change-position (-> state block :position) direction))
       (resolve-conflicts block direction)
       (update-directions arrows)
       (with-meta {:history (conj (history state) block)}))))

(defn solve [problem]
  (if-let [solution (a* (with-meta (:blocks problem) {:history []})
                        (fn [state] (map #(move-block state % (:arrows problem)) (keys state)))
                        #(zero? (estimate-remaining-cost (:goal problem) %))
                        #(count (history %))
                        (partial estimate-remaining-cost (:goal problem))
                        50)]
    (history solution)))
