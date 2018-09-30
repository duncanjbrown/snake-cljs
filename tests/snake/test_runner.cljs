(ns snake.test-runner
  (:require
   [cljs.test]
   [cljs-test-display.core]
   [snake.snake-test])
  (:require-macros
   [cljs.test]))

(cljs.test/run-tests
 (cljs-test-display.core/init! "app-tests")
 'snake.snake-test)
