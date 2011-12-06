(ns learning_clojure.test.nums
  (:use [learning_clojure.nums])
  (:use [clojure.test]))

(deftest frst_divisor_test
  (is (= (frst_divisor 1) 1))
  (is (= (frst_divisor 2) 2))
  (is (= (frst_divisor (* 11 13 7)) 7)))
(deftest largest_factor_test
  (is (= (largest_factor (* 27 7)) 7)))
(deftest integer_log_test
  (is (= 0 (integer_log 3 28)))
  (is (= 3 (integer_log 3 27))))
;(deftest prime_map_test
;  (println (prime_map (* 2 3 3 5 5)))
;  (is (= {2 1 3 2 5 2} (prime_map (* 2 3 3 5 5)))))
