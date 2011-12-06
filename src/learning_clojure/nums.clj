(ns learning_clojure.nums)

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
    (if (divides? base remaining_qty)
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
   (cond
    (= x 1) 1
    (=
      0
      (mod x cnt)) cnt
    :else (recur x (+ 1 cnt)))))

(defn largest_factor
  [x]
  (loop [remaining_qty x]
    (let [cur_frst_divisor (frst_divisor remaining_qty)]
      ;check if remaining_qty is prime
      (if
        (=
          remaining_qty
          cur_frst_divisor)
        remaining_qty
        (recur (kill_factor
                 cur_frst_divisor
                 remaining_qty))))))
(defn divides? [divisor dividend]
  (= 0 (mod dividend divisor)))


(defn prime_map
  ([x]
   (loop [remaining_qty x
          cur_map {}]
     (let [
           cur_frst_divisor (frst_divisor remaining_qty)
           cur_factor_exp (integer_log cur_frst_divisor remaining_qty)
           new_map (assoc cur_map cur_frst_divisor cur_factor_exp)
           ]
       (if (= remaining_qty cur_factor_exp)
         new_map
         (recur (/ remaining_qty (exp cur_frst_divisor cur_factor_exp))
                new_map))))))
