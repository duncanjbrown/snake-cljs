(ns ^:figwheel-always snake.core
  (:require [reagent.core :as reagent]
            [snake.board :as board]
            [snake.snake :as snk]))

(defonce state (reagent/atom {:snake (snk/new-snake)}))

(when-let [element (js/document.getElementById "app")]
  (reagent/render-component [board/game (reagent/cursor state [:snake])] element))
