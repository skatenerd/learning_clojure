(ns learning_clojure.ball
  "i am experimenting with animation"
  (:require [clojure.contrib.generic.math-functions :as math])
  )
(defstruct Ball :posn :velocity :mass)


(defn initialize [wdth hgt]
  (def y-speed-decay 0.95)
  (def x-speed-decay 1.0)
  (def rad 50)
  (def std-mass 100)
  (def max-wdth wdth)
  (def max-hgt hgt)
;  (def starting-posns
;    (for [y (range 0 (int (/ hgt 2)) (* 3 rad))]
;      [0 y]))

  (def dummy-ball-1 (struct Ball [200 200] [0 0] 10))
  (def dummy-ball-2 (struct Ball [120 300] [10 0] 20))
  (def dummy-ball-3 (struct Ball [10 30] [13 13] 20))
  (def balls [dummy-ball-1 dummy-ball-2 dummy-ball-3]) 
  ;(def balls
;    (for [[x y] starting-posns]
;      (struct Ball [x y] [20 20] std-mass)))
  (def ball-agents (map agent balls))

  (def worldstate (vec ball-agents))
)



(declare euclidean-distance vector-norm find-closest-ball scale-vector get-normal-vector collide balls-collide?)


(defn traverse [ball]
  (send-off *agent* #'traverse)
  (. Thread (sleep 300))
  (dosync
  (let [
        old-mass (:mass ball)
        old-velocity (:velocity ball)
        old-posn (:posn ball)
        closest-ball (find-closest-ball old-posn)
        collides (balls-collide? ball @closest-ball)
        new-velocity (cond collides
                           (first (collide [old-mass old-velocity old-posn]
                                           [(:mass @closest-ball) (:velocity @closest-ball) (:posn @closest-ball)]))
                           :else
                           old-velocity)
        ;new-velocity old-velocity
        ;
        normal-vector (get-normal-vector
                        old-posn
                        (:posn @closest-ball))
        physical-push (if collides
                        (map -
                        (scale-vector
                          normal-vector
                          (/ rad
                             (vector-norm normal-vector)))
                             normal-vector)

                        [0 0])

        new-posn (map + new-velocity old-posn physical-push)
        new-mass old-mass
        ]
    ;set the other guy moving in the other direction!

  (struct Ball new-posn new-velocity new-mass))))


(defn start-moving []
  (doseq [cur-ball worldstate]
    (send-off cur-ball traverse)))



(defn euclidean-distance [p1 p2]
  (let [differences (map - p1 p2)
        sqr-differences (map math/sqr differences)
        sum-differences (reduce + sqr-differences)
        ]
    (math/sqrt sum-differences)
    )
  )
(defn dist-to-posn [posn ball-ref]
  (euclidean-distance (:posn @ball-ref)
                     posn)) 
(defn find-closest-ball [ball-posn]
  (let [dist-to-ball (partial dist-to-posn ball-posn)
        not-touching-first-ball (fn [ball-agent]
                                  (not (= 
                                         (:posn @ball-agent)
                                         ball-posn)))
        filtered-worldstate (filter not-touching-first-ball
                                    worldstate)]
    (apply min-key dist-to-ball filtered-worldstate)))


(defn speed [v]
  (let [zero-vector (for [component v]
                      0)]
    (euclidean-distance v zero-vector)))

(defn balls-collide? [frst sec]
  (< (euclidean-distance (:posn frst)
                         (:posn sec))
     rad))

(defn dot-product [frst sec] 
  (reduce + (map * frst sec)))

(defn two-d-perp [[x-comp y-comp]]
  [(* -1 y-comp) x-comp])
(defn one-d-elastic-collision [[m1 v1] [m2 v2]]
  (let [
        v1f-numerator (+
                        (* v1 (- m1 m2))
                        (* 2 m2 v2))
        v1f-denominator (+ m1 m2)
        v2f-numerator (+
                        (* v2 (- m2 m1))
                        (* 2 m1 v1))
        v2f-denominator (+ m1 m2)]
    [(/ v1f-numerator v1f-denominator)
     (/ v2f-numerator v2f-denominator)]))
;(one-d-elastic-collision [200 20] [20 -20])
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

(defn collide [[m1 v1 p1] [m2 v2 p2]]
  (let 
    [normal-vector (normalize (get-normal-vector p1 p2))
     perp-vector (two-d-perp normal-vector)
     b1-normal-speed (dot-product v1 normal-vector)
     b2-normal-speed (dot-product v2 normal-vector)
     b1-perp-speed (dot-product v1 perp-vector)
     b2-perp-speed (dot-product v2 perp-vector)
     b1-perp-v (scale-vector perp-vector b1-perp-speed)
     b2-perp-v (scale-vector perp-vector b2-perp-speed)
     [b1-new-normal-speed b2-new-normal-speed] (one-d-elastic-collision [m1 b1-normal-speed]
                                                                [m2 b2-normal-speed])
     b1-new-normal-v (scale-vector normal-vector b1-new-normal-speed)
     b2-new-normal-v (scale-vector normal-vector b2-new-normal-speed)
     b1-new-v (map + 
                   b1-new-normal-v
                   b1-perp-v)
     b2-new-v (map +
                   b2-new-normal-v
                   b2-perp-v)


     ]
    [b1-new-v
     b2-new-v]))
