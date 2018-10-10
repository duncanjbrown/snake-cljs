(ns snake.board)

(def size 12)

(def board (vec (repeat size (vec (repeat size nil)))))

(defn- populate
  [board cells type]
  (reduce (fn [board coords]
            (assoc-in board coords type))
          board
          cells))

(defn- board-cell
  [value]
  (case value
    nil [:div.cell "_"]
    :snake [:div.cell "█"]
    :food [:div.cell "F"]))

(defn place-food
  [snake-cells]
  (let [coords (set [[(rand-int size) (rand-int size)]])]
    (if (some coords snake-cells)
      (recur snake-cells)
      coords)))

(defn game
  [snake food]
  (let [populated-board (-> board
                            (populate @food :food)
                            (populate @snake :snake))]
    [:div#board {:on-key-down #(println "ss")}
     (for [y (range (count populated-board))]
       ^{:key (str "row" y)} [:div.row
        (for [x (range (count (first populated-board)))]
          (let [value (get-in populated-board [y x])]
            ^{:key (str y x)} [board-cell value]))])]))
