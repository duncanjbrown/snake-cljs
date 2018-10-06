(ns snake.board)

(def width 10)
(def height 10)

(def board (vec (repeat width (vec (repeat height nil)))))

(defn- populate
  [board snake]
  (reduce (fn [board coords]
            (assoc-in board coords :snake))
          board
          snake))

(defn- board-cell
  [value]
  (case value
    nil [:div.cell "_"]
    :snake [:div.cell "█"]))

(defn game
  [snake]
  (let [populated-board (populate board @snake)]
    [:div#board
     (for [y (range (count populated-board))]
       ^{:key (str "row" y)} [:div.row
        (for [x (range (count (first populated-board)))]
          (let [value (get-in populated-board [y x])]
            ^{:key (str y x)} [board-cell value]))])]))
