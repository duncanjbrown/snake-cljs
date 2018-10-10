(ns snake.loop
  (:require [snake.snake :as snk]))

(defn place-food
  [snake-cells board-size]
  (let [coords (set [[(rand-int board-size) (rand-int board-size)]])]
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
     :tick-timeout nil
     }))

(defn next-state
  [snake food direction board-size]
  (cond (snk/self-colliding? snake)
          false
        (some food snake)
          (let [new-snake (snk/grow-snake snake direction board-size)]
            {:snake new-snake :food (place-food new-snake board-size)})
        :else
          {:snake (snk/move-snake snake direction board-size)}))

