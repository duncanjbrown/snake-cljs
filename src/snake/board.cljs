(ns snake.board)

(def size 14)

(def board (vec (repeat size (vec (repeat size nil)))))

(defn- populate
  "Given a board, which is 2d vector of cells,
  a set of cell co-ords, and a type keyword, return the
  2d vector with the type keyword in the specified
  positions"
  [board cells type]
  (reduce (fn [board coords]
            (assoc-in board coords type))
          board
          cells))

(defn- populate-snake
  "Given a board and a set of co-ords, split
  them into head and body and write them into
  the board"
  [board snake]
  (let [[head & body] (reverse snake)]
    (-> board
        (populate [head] :snake-head)
        (populate body :snake))))

(defn- board-cell
  "Generate markup for a given cell type"
  [value]
  (case value
    nil [:div.cell.blank " "]
    :snake [:div.cell.snake "█"]
    :snake-head [:div.cell.snake-head "█"]
    :food [:div.cell.food "♦"]
    :wall [:div.cell.wall " "]))

(defn game
  "Generate board markup from snake, food and
  obstacle co-ordinates"
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
  "Generate markup to display the score"
  [score]
  [:p @score])

(defn overlay
  "Render an overlay suitable for displaying messages
  to the player"
  [game-state]
  (when (= :over @game-state)
    [:div.overlay
     [:h1 "GAME OVER"]
     [:h2 "Press " [:span.key "space"] " to restart"]]))
