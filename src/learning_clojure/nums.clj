(defn divides?
  [numerator denominator]
  (=
    0
    (mod numerator denominator)))

(defn exp [base exponent]
  (loop [aggregator 1
         cur_exp exponent]
    (if (zero? cur_exp)
      aggregator
      (recur (* aggregator base)
             (- cur_exp 1)))))

(defn integer_log [base qty]
  (loop [remaining_qty qty
         cnt 0]

    (if (divides? remaining_qty base)
      (recur (/ remaining_qty base)
             (+ cnt 1))
      cnt)))
(defn kill_factor
  [divisor dividend]
  (let [
        divisor_pow (integer_log divisor dividend)
        maximal_divisor (exp divisor divisor_pow)
        ]
    (/ dividend maximal_divisor)
    ))




(defn frst_divisor
  ([x]
  (frst_divisor x 2))
  ([x cnt]
  (if
    (=
      0
      (mod x cnt))
    cnt
    (recur x (+ 1 cnt)))))

(defn largest_factor
  [x]
  (loop [remaining_qty x]
    (let [cur_frst_divisor (frst_divisor remaining_qty)]
      (if
        (=
          remaining_qty
          cur_frst_divisor)
        remaining_qty
        (recur (kill_factor
                 cur_frst_divisor
                 x))))))



(defn prime_map
  ([x]
   (loop [remaining_qty x
          cur_map {}]
     (let [
           cur_frst_divisor (frst_divisor remaining_qty)
           cur_factor_exp (integer_log cur_frst_divisor remaining_qty)
           new_map (assoc cur_map cur_frst_divisor cur_factor_exp)
           ]
       (if (= remaining_qty cur_frst_divisor)
         new_map
         (recur (/ remaining_qty (exp cur_frst_divisor cur_factor_exp))
                new_map))))))


(println (prime_map (* 2 3 5 5 7 11 11 11 17)))
(println (kill_factor 3 (* 27 7)))
(println (frst_divisor (* 11 13 7)))
(println (largest_factor (* 27 7)))
(println (integer_log 3 28))
(println (integer_log 3 27))
(println (exp 3 3))
;(println (frst_divisor 9))

;(println (largest_factor (* 17 2 7 13 37)))
