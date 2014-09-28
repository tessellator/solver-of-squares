(ns solver-of-squares.core
  (:require [solver-of-squares.algorithms :refer [a* manhattan-distance]]
            [solver-of-squares.levels :refer :all]))

(set! *warn-on-reflection* true)


;; This heuristic is NOT admissible.  Needs a bit of work...
(defn- estimate-remaining-cost [goal state]
  (let [ks (keys goal)
        xs (map #(get-in state [:blocks % :position]) ks)
        ys (map goal ks)]
    (reduce + (map manhattan-distance xs ys))))

(defn- change-position [[x y] direction]
  (condp = direction
    :left [(dec x) y]
    :right [(inc x) y]
    :up [x (inc y)]
    :down [x (dec y)]))

(defn- goal? [ks goal state]
  (every? identity (map #(= (goal %) (get-in state [:blocks % :position])) ks)))

(defn- resolve-conflicts [state block direction]
  (letfn [(get-pos [color] (get-in state [:blocks color :position]))
          (change-pos [color] (change-position (get-pos color) direction))
          (update-pos [color] (assoc-in state [:blocks color :position] (change-pos color)))]
    (let [colors (keys (:blocks state))
          ks (filter #(not= block %) colors)
          pos (get-pos block)
          conflict (first (filter #(= pos (get-pos %)) ks))]
      (if conflict
        (recur (update-pos conflict) conflict direction)
        state))))

(defn- update-directions [state arrows]
  (->> (for [x (:blocks state) y arrows] [x y])
       (filter (fn [[[_ {pos :position}] [arr-pos _]]] (= pos arr-pos)))
       (reduce (fn [s [[block _] [_ dir]]] (assoc-in s [:blocks block :direction] dir)) state)))

(defn- move-block
  ([state block arrows]
   (let [direction (get-in state [:blocks block :direction])]
     (move-block state block direction arrows)))

  ([state block direction arrows]
   (let [curr-pos (get-in state [:blocks block :position])
         new-pos (change-position curr-pos direction)]
     (-> (assoc-in state [:blocks block :position] new-pos)
         (resolve-conflicts block direction)
         (update-directions arrows)
         (assoc :history (conj (:history state) block))))))

(defn- get-successive-states [arrows state]
  (map #(move-block state % arrows) (keys (:blocks state))))

(defn solve [problem]
  (if-let [solution (a* (:state problem)
                        (partial get-successive-states (:arrows problem))
                        (partial goal? (keys (:goal problem)) (:goal problem))
                        #(count (:history %))
                        (partial estimate-remaining-cost (:goal problem))
                        :blocks
                        50)]
    (:history solution)))
