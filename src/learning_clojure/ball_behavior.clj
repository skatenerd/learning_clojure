(ns learning_clojure.ball_behavior
  "i am experimenting with animation"
  (:require [clojure.contrib.generic.math-functions :as math]
            [learning_clojure.vector :as vector])
  )
(defstruct Ball :posn :velocity :mass)
;this should go into the Ball struct
(def rad 50)

(declare find-closest-ball 
         collide 
         balls-collide?)

(defn traverse [ball worldstate]
  (send-off *agent* #'traverse worldstate)
  (. Thread (sleep 300))
  (dosync
  (let [
        old-mass (:mass ball)
        old-velocity (:velocity ball)
        old-posn (:posn ball)
        closest-ball (find-closest-ball old-posn worldstate)
        collides (balls-collide? ball @closest-ball)
        new-velocity (cond collides
                           (first (collide [old-mass old-velocity old-posn]
                                           [(:mass @closest-ball) (:velocity @closest-ball) (:posn @closest-ball)]))
                           :else
                           old-velocity)
        normal-vector (vector/get-normal-vector
                        old-posn
                        (:posn @closest-ball))
        physical-push (if collides
                        (map -
                        (vector/scale-vector
                          normal-vector
                          (/ rad
                             (vector/vector-norm normal-vector)))
                             normal-vector)

                        [0 0])

        new-posn (map + new-velocity old-posn physical-push)
        new-mass old-mass
        ]
    ;todo: set the other guy moving in the other direction!

  (struct Ball new-posn new-velocity new-mass))))





(defn dist-to-posn [posn ball-ref]
  (vector/euclidean-distance (:posn @ball-ref)
                     posn)) 
(defn find-closest-ball [ball-posn worldstate]
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
    (vector/euclidean-distance v zero-vector)))

(defn balls-collide? [frst sec]
  (< (vector/euclidean-distance (:posn frst)
                         (:posn sec))
     rad))

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

(defn collide [[m1 v1 p1] [m2 v2 p2]]
  (let 
    [normal-vector (vector/normalize (vector/get-normal-vector p1 p2))
     perp-vector (vector/two-d-perp normal-vector)
     b1-normal-speed (vector/dot-product v1 normal-vector)
     b2-normal-speed (vector/dot-product v2 normal-vector)
     b1-perp-speed (vector/dot-product v1 perp-vector)
     b2-perp-speed (vector/dot-product v2 perp-vector)
     b1-perp-v (vector/scale-vector perp-vector b1-perp-speed)
     b2-perp-v (vector/scale-vector perp-vector b2-perp-speed)
     [b1-new-normal-speed b2-new-normal-speed] (one-d-elastic-collision [m1 b1-normal-speed]
                                                                [m2 b2-normal-speed])
     b1-new-normal-v (vector/scale-vector normal-vector b1-new-normal-speed)
     b2-new-normal-v (vector/scale-vector normal-vector b2-new-normal-speed)
     b1-new-v (map + 
                   b1-new-normal-v
                   b1-perp-v)
     b2-new-v (map +
                   b2-new-normal-v
                   b2-perp-v)


     ]
    [b1-new-v
     b2-new-v]))
