(ns ^:figwheel-always snake.core
  (:require [reagent.core :as reagent]
            [snake.board :as board]
            [snake.snake :as snk]))

(defonce state
  (let [starting-snake (snk/new-snake)]
    (reagent/atom {
                   :direction [0 1]
                   :snake starting-snake
                   :speed 100
                   :food (board/place-food starting-snake)
                   :tick-timeout nil})))

(defn tick
  []
  (let [{:keys [snake food direction speed]} @state]
    (if (some food snake)
      (let [new-snake (snk/grow-snake snake direction 10)]
        (swap! state assoc
               :snake new-snake
               :food (board/place-food new-snake)))
      (swap! state assoc :snake (snk/move-snake snake direction 10)))
    (swap! state assoc :tick-timeout (js/setTimeout #(reagent/next-tick tick) speed))))

(defn- direction-to-vector
  [direction]
  (direction {:up [-1 0]
              :down [1 0]
              :left [0 -1]
              :right [0 1]}))

(defn- keypress-to-command
  [keycode]
  (get {119 :up 115 :down 97 :left 100 :right 104 :pause} keycode))

(defn- toggle-pause
  []
  (if-let [tick-timeout (:tick-timeout @state)]
    (do 
      (js/clearTimeout tick-timeout)
      (swap! state assoc :tick-timeout nil))
    (tick)))

(defn- handle-keypress
  [e]
  (let [command (keypress-to-command (.-charCode e))]
    (if-let [new-direction (direction-to-vector command)]
      (swap! state assoc :direction new-direction)
      (if (= command :pause)
        (toggle-pause)))))

(defn main []
  (when-let [element (js/document.getElementById "app")]
    (do
      (.addEventListener js/document "keypress" handle-keypress)
      (reagent/render-component
       [:div#game
        [board/game (reagent/cursor state [:snake]) (reagent/cursor state [:food])]]
       element)
      (tick))))
