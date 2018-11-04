(ns snake.loop
  (:require [snake.snake :as snk]))

(defn- find-unused-cell
  "Given a board size and a set of used-coords,
  return a random unused coord"
  [used-coords board-size]
  (let [coords (vec (take 2 (repeatedly #(rand-int board-size))))]
    (if (some #(= coords %) used-coords)
      (recur used-coords board-size)
      coords)))

(defn- place-food
  "Find a place on the board to place some food,
  given the board-size and the list of occupied
  coords"
  [used-coords board-size]
  (conj #{} (find-unused-cell used-coords board-size)))

(defn- maintain-food
  "If the board lacks food, put some on it"
  [state board-size]
  (let [{:keys [food snake walls]} state]
    (if (not-empty food)
      state
      (merge state {:food (place-food (concat snake walls) board-size)}))))

(defn- place-walls
  "Put some obstacles into the board, given the
  board size and the list of occupied coords"
  [used-coords board-size]
  (set
   (take 7
         (repeatedly #(find-unused-cell used-coords board-size)))))

(defn- move-and-eat
  "Update the snake according to the direction
  in state, perhaps consuming some food and growing
  along the way"
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
     :speed 3
     :pause false
     :score 0}))

(defn next-state
  [{:keys [snake walls] :as state} board-size]
  (cond (or (snk/self-colliding? snake)
            (some walls snake))
        :dead
        :else
        (-> (move-and-eat state board-size)
            (maintain-food board-size))))
