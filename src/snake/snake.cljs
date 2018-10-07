(ns snake.snake)

(defn new-snake
  []
  (let [length 4
        start-pos [5 4]]
    (vec (take length (iterate #(vec (map + [0 1] %)) start-pos)))))

(defn move-snake
  [snake movement-vector board-size]
  (let [new-head-position (mapv #(mod % 10)
                                (map + (last snake) movement-vector))]
    (vec (drop 1 (conj snake new-head-position)))))

;; (move-snake [[0 1] [0 2]] [0 1] 10)
