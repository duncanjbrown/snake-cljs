(ns snake.board)

(def size 14)

(def board (vec (repeat size (vec (repeat size nil)))))

(defn- populate
  [board cells type]
  (reduce (fn [board coords]
            (assoc-in board coords type))
          board
          cells))

(defn- populate-snake
  [board snake]
  (let [[head & body] (reverse snake)]
    (-> board
        (populate [head] :snake-head)
        (populate body :snake))))

(defn- board-cell
  [value]
  (case value
    nil [:div.cell.blank " "]
    :snake [:div.cell.snake "█"]
    :snake-head [:div.cell.snake-head "█"]
    :food [:div.cell.food "♦"]
    :wall [:div.cell.wall " "]))

(defn game
  [snake food walls]
  (let [populated-board (-> board
                            (populate @walls :wall)
                            (populate @food :food)
                            (populate-snake @snake))]
    [:div#board
     (for [y (range (count populated-board))]
       ^{:key (str "row" y)} [:div.row
        (for [x (range (count (first populated-board)))]
          (let [value (get-in populated-board [y x])]
            ^{:key (str y x)} [board-cell value]))])]))

(defn score
  [score]
  [:p @score])
