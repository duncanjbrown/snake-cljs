(ns snake.loop
  (:require [snake.snake :as snk]))

(defn- random-coord-in-board
  [board-size]
  (vec (take 2 (repeatedly #(rand-int board-size)))))

(defn place-food
  [snake-cells board-size]
  (let [coords (set [(random-coord-in-board board-size)])]
    (if (some coords snake-cells)
      (recur snake-cells board-size)
      coords)))

(defn starting-state
  [board-size]
  (let [starting-snake (snk/new-snake)]
    {
     :direction [0 1]
     :snake starting-snake
     :speed 100
     :food (place-food starting-snake board-size)
     :pause false
     }))

(defn next-state
  [snake food direction board-size]
  (cond (snk/self-colliding? snake)
          :dead
        (some food snake)
          (let [new-snake (snk/grow-snake snake direction board-size)]
            {:snake new-snake :food (place-food new-snake board-size)})
        :else
          {:snake (snk/move-snake snake direction board-size)}))

