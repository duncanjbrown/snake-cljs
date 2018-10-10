(ns snake.snake)

(defn new-snake
  []
  (let [length 4
        start-pos [5 4]]
    (vec (take length (iterate #(vec (map + [0 1] %)) start-pos)))))

(defn self-colliding?
  [snake]
  (let [[head & body] (reverse snake)]
    (some #(= % head) body)))

(defn grow-snake
  [snake movement-vector board-size]
  (let [new-head-position (mapv #(mod % board-size)
                                (map + (last snake) movement-vector))]
    (conj snake new-head-position)))

(defn move-snake
  [& args]
  (let [grown (apply grow-snake args)]
    (vec (drop 1 grown))))
