(ns snake.snake-test
  (:require
   [snake.snake :as sut]
   [cljs.test :as t :include-macros true]))

(t/deftest test-new-snake
  (t/testing "Returning a 4-cell snake starting at [5 4]"
    (t/is (= [[5 4] [5 5] [5 6] [5 7]]
             (sut/new-snake)))))
