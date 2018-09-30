(ns snake.board)

(def width 10)
(def height 10)

(defn build-board
  []
  (vec (repeat width (vec (repeat height nil)))))

(defn board
  [board]
  [:div#board
   (for [y (range (count board))]
     [:div.row
       (for [x (range (count (first board)))]
         [:div.cell "_"])])])
