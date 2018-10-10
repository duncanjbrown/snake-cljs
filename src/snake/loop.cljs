(ns snake.loop
  (:require [snake.snake :as snk]
            [reagent.core :as reagent]
            [snake.board :as board]))

(defn next-state
  [snake food direction]
  (if (some food snake)
    (let [new-snake (snk/grow-snake snake direction board/size)]
      {:snake new-snake :food (board/place-food new-snake)})
    {:snake (snk/move-snake snake direction board/size)}))

