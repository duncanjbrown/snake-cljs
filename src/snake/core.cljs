(ns ^:figwheel-always snake.core
  (:require [reagent.core :as reagent]
            [snake.board :as board]))


(when-let [element (js/document.getElementById "app")]
  (reagent/render-component [board/board (board/build-board)] element))
