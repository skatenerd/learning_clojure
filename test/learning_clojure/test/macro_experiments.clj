(ns learning_clojure.test.macro_experiments
  (:use [learning_clojure.macro_experiments])
  (:use [clojure.test]))

(def incr (fn [x] (+ x 1)))
(def incr_list (fn [& args]
                 (map incr args)))

(deftest apply_twice_tests
  (is (= '(256) (my_apply_twice square (+ 2 2))))
  (is (= '(2 3 4 5 6 7) (my_apply_twice incr_list 0 1 2 3 4 5))))
  
(deftest my_apply_tests
  (is (= 10 (my_apply + (range 1 5)))))

;testing macros is tricky!
;do i test the macroexpansion?
;do i define something as the output?
(deftest eval_if_simple_tests
  (is (= 3 (eval_if_simple (+ 1 2))))
  (let [expanded (eval_if_simple (+ 1 (* 3 4)))]
    (is (= (list + 1 (list * 3 4))) expanded))
)
