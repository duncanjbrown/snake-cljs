(ns snake.board)

(def width 10)
(def height 10)

(defn build-board
  []
  (vec (repeat width (vec (repeat height nil)))))

(defn board
  [board]
  [:ul
   (for [y (range (count board))]
     [:li
      [:ul
       (for [x (range (count (first board)))]
         [:li "x"])]])])
