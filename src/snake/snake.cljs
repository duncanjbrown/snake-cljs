(ns snake.snake)

(defn new-snake
  []
  (let [length 4
        start-pos [5 4]]
    (take length (iterate #(vec (map + [0 1] %)) start-pos))))

(defn move-snake
  [snake movement-vector board-size]
  (let [new-head-position (map #(mod % 10)
                               (vec (map + (last snake) movement-vector)))]
    (drop 1 (conj snake new-head-position))))
