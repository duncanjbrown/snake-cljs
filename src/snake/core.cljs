(ns ^:figwheel-always snake.core
  (:require [reagent.core :as reagent]
            [snake.board :as board]
            [snake.loop :as loop]
            [snake.input :as input]))

(defonce state (reagent/atom (loop/starting-state board/size)))

(declare tick)
(defn- toggle-pause
  "Pause and unpause the game by directly setting
  a value in the state ratom"
  []
  (swap! state update :pause not)
  (tick))

(defn- restart!
  "Start the game again"
  []
  (reset! state (loop/starting-state board/size))
  (tick))

(defn- reversing-direction?
  "Given two 2d vectors, determine whether they
  cancel each other out"
  [old new]
  (every? zero? (map + old new)))

(defn- handle-command
  "Given a key-value pair of command type -> value,
  dispatch an appropriate function call"
  [command]
  (if-let [direction (:direction command)]
    (if-not (reversing-direction? (:direction @state) direction)
      (swap! state assoc :direction direction)))
  (if (:pause command)
    (toggle-pause)))

(defn- tick
  "Calculate the next state of the game"
  []
  (let [next (loop/next-state @state board/size)]
    (cond
      (:pause next) nil
      (= :dead next) (restart!)
      :else
      (do
        (swap! state merge next)
        (js/setTimeout #(reagent/next-tick tick) (:speed next))))))

(defn main []
  (when-let [element (js/document.getElementById "app")]
    (do
      (.addEventListener js/document "keypress" #(handle-command (input/handle-keypress %)))
      (reagent/render-component
       [:div#game
        [board/game
         (reagent/cursor state [:snake])
         (reagent/cursor state [:food])
         (reagent/cursor state [:walls])]
        [board/score
         (reagent/cursor state [:score])]]
       element)
      (tick))))
