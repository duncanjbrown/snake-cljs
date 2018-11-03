(ns snake.snake)

(defn new-snake
  "Build a 4-element snake starting at [4,5]"
  []
  (let [length 4
        start-pos [5 4]]
    (vec (take length (iterate #(vec (map + [0 1] %)) start-pos)))))

(defn self-colliding?
  "Given a set of snake co-ords, return true
  if the head is colliding with the body"
  [snake]
  (let [[head & body] (reverse snake)]
    (some #(= % head) body)))

(defn grow-snake
  "Given a set of snake co-ords, a movement vector
  and a board size, return the co-ords of the snake
  moved one unit in the direction of the vector.
  This implementations wraps movement around the edge
  of the board."
  [snake movement-vector board-size]
  (let [new-head-position (mapv #(mod % board-size)
                                (map + (last snake) movement-vector))]
    (conj snake new-head-position)))

(defn move-snake
  "As grow-snake, but drop the last element of the snake"
  [& args]
  (let [grown (apply grow-snake args)]
    (vec (drop 1 grown))))
