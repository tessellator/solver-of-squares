(ns solver-of-squares.levels)

(def level11
  {:goal   {:red [1 2] :blue [2 2] :navy [3 2]}
   :blocks {:red  {:position [0 4] :direction :right}
            :navy {:position [2 0] :direction :up}
            :blue {:position [4 4] :direction :left}}
   :arrows {[2 4] :down}})

(def level21
  {:goal   {:brown [0 3] :yellow [2 1] :green [2 2] :red [2 3]}
   :blocks {:brown  {:position [0 4] :direction :down}
            :red    {:position [0 0] :direction :right}
            :green  {:position [2 0] :direction :up}
            :yellow {:position [2 4] :direction :left}}
   :arrows {[0 0] :right
            [0 1] :down
            [0 2] :up
            [0 4] :down
            [2 0] :up
            [2 4] :left}})
