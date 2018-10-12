(ns snake.loop
  (:require [snake.snake :as snk]
            [snake.levels :as levels]))

(defn- random-coord-in-board
  [board-size]
  (vec (take 2 (repeatedly #(rand-int board-size)))))

(defn place-food
  [used-cells board-size]
  (let [coords (set [(random-coord-in-board board-size)])]
    (if (some coords used-cells)
      (recur used-cells board-size)
      coords)))

(defn starting-state
  [board-size]
  (let [starting-snake (snk/new-snake)]
    {
     :direction [0 1]
     :snake starting-snake
     :speed 100
     :food (place-food starting-snake board-size)
     :walls (set (take 7 (repeatedly #(random-coord-in-board board-size))))
     :pause false
     :level 1
     :score 0
     }))

(defn next-state
  [snake food walls direction score board-size]
  (cond (snk/self-colliding? snake)
          :dead
        (some walls snake)
          :dead
        (some food snake)
          (let [new-snake (snk/grow-snake snake direction board-size)]
            {:snake new-snake
             :food (place-food (distinct (concat new-snake walls)) board-size)
             :score (inc score)})
        :else
          {:snake (snk/move-snake snake direction board-size)}))
