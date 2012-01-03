(ns learning_clojure.vector
  "vector manipulation library.
  some ideas taken from mark reid's minilight in clojure"
  (:require [clojure.contrib.generic.math-functions :as math])
  )


(defn euclidean-distance [p1 p2]
  (let [differences (map - p1 p2)
        sqr-differences (map math/sqr differences)
        sum-differences (reduce + sqr-differences)
        ]
    (math/sqrt sum-differences)
    )
  )
(defn dot-product [frst sec] 
  (reduce + (map * frst sec)))

(defn two-d-perp [[x-comp y-comp]]
  [(* -1 y-comp) x-comp])

(defn scale-vector [v f]
  (let [times-f (fn [x]
                  (* x f))]
    (map times-f v)))

(defn vector-norm [v]
  (Math/sqrt (dot-product v v)))

(defn normalize [v]
  (scale-vector v (/ 1 (vector-norm v))))

(defn get-normal-vector [v1 v2]
  (map - v1 v2))
