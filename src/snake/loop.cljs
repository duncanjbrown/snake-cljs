(ns snake.loop
  (:require [snake.snake :as snk]))

(defn- find-unused-cell
  [used-coords board-size]
  (let [coords (vec (take 2 (repeatedly #(rand-int board-size))))]
    (if (some #(= coords %) used-coords)
      (recur used-coords board-size)
      coords)))

(defn- place-food
  [used-coords board-size]
  (conj #{} (find-unused-cell used-coords board-size)))

(defn- maintain-food
  [state board-size]
  (let [{:keys [food snake walls]} state]
    (if (not-empty food)
      state
      (merge state {:food (place-food (concat snake walls) board-size)}))))

(defn- place-walls
  [used-coords board-size]
  (set
   (take 7
         (repeatedly #(find-unused-cell used-coords board-size)))))

(defn- move-and-eat
  [state board-size]
  (let [{:keys [snake direction food score]} state]
    (if-let [eaten-food (some food snake)]
      (merge state {:snake (snk/grow-snake snake direction board-size)
                    :food  (disj food eaten-food)
                    :score (inc score)})
      (update state :snake snk/move-snake direction board-size))))

(defn starting-state
  [board-size]
  (let [snake (snk/new-snake)
        walls (place-walls snake board-size)
        food  (place-food (concat snake walls) board-size)]
    {:snake snake
     :food  food
     :walls walls
     :direction [0 1]
     :speed 100
     :pause false
     :level 1
     :score 0}))

(defn next-state
  [state board-size]
  (cond (snk/self-colliding? (:snake state))
        :dead
        (some (:walls state) (:snake state))
        :dead
        :else
        (-> (move-and-eat state board-size)
            (maintain-food board-size))))
