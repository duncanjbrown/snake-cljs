(ns snake.snake-test
  (:require
   [snake.snake :as sut]
   [cljs.test :as t :include-macros true]))

(t/deftest test-new-snake
  (t/testing "It returns a 4-cell snake starting at [5 4]"
    (t/is (= [[5 4] [5 5] [5 6] [5 7]]
             (sut/new-snake)))))

(t/deftest test-move-snake
  (let [starting-snake  [[5 4] [5 5] [5 6] [5 7]]]
    (t/testing "Going right"
      (t/is (= [[5 5] [5 6] [5 7] [5 8]]
               (sut/move-snake starting-snake [0 1] 10))))
    (t/testing "Turning upwards"
      (t/is (= [[5 5] [5 6] [5 7] [4 7]]
               (sut/move-snake starting-snake [-1 0] 10))))
    (t/testing "Turning downwards"
      (t/is (= [[5 5] [5 6] [5 7] [6 7]]
               (sut/move-snake starting-snake [1 0] 10)))))
  (t/testing "Going over the edge of the board and looping round"
    (t/is (= [[5 7] [5 8] [5 9] [5 0]]
             (sut/move-snake [[5 6] [5 7] [5 8] [5 9]] [0 1] 10))))
  (t/is (= [[-2 0] [-1 0] [0 0] [9 0]]
           (sut/move-snake [[-3 0] [-2 0] [-1 0] [0 0]] [-1 0] 10))))
