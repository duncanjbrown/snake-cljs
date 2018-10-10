(ns snake.input)

(defn- direction-to-vector
  [direction]
  (direction {:up [-1 0]
              :down [1 0]
              :left [0 -1]
              :right [0 1]}))

(defn- keypress-to-command
  [keycode]
  (get {119 :up 115 :down 97 :left 100 :right 104 :pause} keycode))

(defn handle-keypress
  [e]
  (let [command (keypress-to-command (.-charCode e))]
    (if-let [new-direction (direction-to-vector command)]
      {:direction new-direction}
      {command true})))
