(ns snake.input)

(defn- direction-to-vector
  "Map a human-readable direction keyword
  to a 2d vector fit for transforming the snake"
  [direction]
  (direction {:up [-1 0]
              :down [1 0]
              :left [0 -1]
              :right [0 1]}))

(defn- keypress-to-command
  "Resolve a numbered keycode to a keyword
  representing a command"
  [keycode]
  (println keycode)
  (get {119 :up 115 :down 97 :left 100 :right 104 :pause 32 :restart} keycode))

(defn speed-controls
  [current-speed command-handler]
  [:label "Speed"
   [:input {:type "range" :value @current-speed :min 1 :max 5
            :style {:width "100%"}
            :on-change (fn [e]
                         (command-handler {:set-speed (.. e -target -value)}))}]])

(defn handle-keypress
  "Handle the keypress event in JS and return a map
  suitable for core/handle-command"
  [e]
  (if-let [command (keypress-to-command (.-charCode e))]
    (if-let [new-direction (direction-to-vector command)]
      {:direction new-direction}
      {command true})))
