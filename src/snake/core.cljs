(ns ^:figwheel-always snake.core
  (:require [reagent.core :as reagent]
            [snake.board :as board]
            [snake.loop :as loop]
            [snake.input :as input]))

(defonce state (reagent/atom (loop/starting-state board/size)))

(declare tick)
(defn- toggle-pause
  []
  (if-let [tick-timeout (:tick-timeout @state)]
    (do
      (js/clearTimeout tick-timeout)
      (swap! state assoc :tick-timeout nil))
    (tick)))

(defn- restart!
  []
  (reset! state (loop/starting-state board/size))
  (tick))

(defn- handle-command
  [command]
  (if-let [direction (:direction command)]
    (swap! state assoc :direction direction))
  (if (:pause command)
    (toggle-pause)))

(defn- tick
  []
  (let [{:keys [snake food direction speed]} @state
        next (loop/next-state snake food direction board/size)]
    (if (false? next)
      (restart!)
      (do
        (swap! state merge next)
        (swap! state assoc :tick-timeout (js/setTimeout #(reagent/next-tick tick) speed))))))

(defn main []
  (when-let [element (js/document.getElementById "app")]
    (do
      (.addEventListener js/document "keypress" #(handle-command (input/handle-keypress %)))
      (reagent/render-component
       [:div#game
        [board/game (reagent/cursor state [:snake]) (reagent/cursor state [:food])]]
       element)
      (tick))))
