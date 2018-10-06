(ns snake.core
  (:require [reagent.core :as reagent]
            [snake.board :as board]
            [snake.snake :as snk]))

(defonce state (reagent/atom {:direction [0 1] :snake (snk/new-snake) :speed 100}))

(defn tick
  []
  (swap! state assoc :snake (snk/move-snake (:snake @state) (:direction @state) 10))
  (js/setTimeout #(reagent/next-tick tick) (:speed @state)))

(defn main []
  (when-let [element (js/document.getElementById "app")]
    (do
      (reagent/render-component [board/game (reagent/cursor state [:snake])] element)
      (tick))))
