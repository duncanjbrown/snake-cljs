(ns ^:figwheel-always snake.core
  (:require [reagent.core :as reagent]
            [snake.board :as board]
            [snake.loop :as loop]
            [snake.input :as input]))

(defonce state (reagent/atom {}))

(declare tick)
(defn- toggle-pause
  "Pause and unpause the game by directly setting
  a value in the state ratom"
  []
  (swap! state update :pause not)
  (tick))

(defn- start!
  "Start the game"
  []
  (reset! state (loop/starting-state board/size))
  (tick))

(defn- stop!
  "End the game"
  []
  (swap! state merge {:game-state :over}))

(defn- restart!
  "Restart the game while already running"
  []
  (when (= :over (:game-state @state))
    (start!)))

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
  (if-let [new-speed (:set-speed command)]
    (swap! state assoc :speed new-speed))
  (if (:restart command)
    (restart!))
  (if (:pause command)
    (toggle-pause)))

(defn- speed-setting->milliseconds
  "Given a speed setting, return a value in milliseconds
  suitable for using as a timeout between game ticks"
  [speed-setting]
  (let [speed-levels {1 300
                      2 200
                      3 100
                      4 50
                      5 25}]
    (get speed-levels (int speed-setting))))

(defn- tick
  "Calculate the next state of the game. If the game is over,
  don't bother."
  []
  (if (not= :over (:game-state @state))
    (let [next (loop/next-state @state board/size)]
      (cond
        (:pause next) nil
        (= :dead next) (stop!)
        :else
        (do
          (swap! state merge next)
          (js/setTimeout #(reagent/next-tick tick) (speed-setting->milliseconds (:speed next))))))))

(defn help-text
  []
  [:p.help
   "Controls: "
   [:span.key "W"]
   [:span.key "A"]
   [:span.key "S"]
   [:span.key "D"]
   "\t Pause: "
   [:span.key "H"]])

(defn main []
  (when-let [element (js/document.getElementById "app")]
    (do
      (start!)
      (.addEventListener js/document "keypress" #(handle-command (input/handle-keypress %)))
      (reagent/render-component
       [:div#game
        [board/overlay
         (reagent/cursor state [:game-state])
         (reagent/cursor state [:score])]
        [board/game
         (reagent/cursor state [:snake])
         (reagent/cursor state [:food])
         (reagent/cursor state [:walls])]
        [board/score
         (reagent/cursor state [:score])]
        [input/speed-controls
         (reagent/cursor state [:speed])
         handle-command]
        [help-text]]
       element))))
