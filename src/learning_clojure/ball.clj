(ns learning_clojure.ball
  "i am experimenting with animation"
  (:require [clojure.contrib.generic.math-functions :as math])
  )
(def y-speed-decay 0.95)
(def x-speed-decay 1.0)
(def rad 50)
(defstruct Ball :x :y :velocity)
(defstruct Velocity :delta-x :delta-y)



(defn initialize [wdth hgt]
  (def max-wdth wdth)
  (def max-hgt hgt)
  (def starting-posns
    (for [y (range 0 (int (/ hgt 2)) (* 3 rad))]
      [0 y]))

  (def balls
    (for [[x y] starting-posns]
      (struct Ball x y (struct Velocity 20 20))))
  (def ball-agents (map agent balls))

  (def worldstate (vec ball-agents))

  )





(defn probably-one []
  (let [threshold 0.95
        cur-rand (rand)]
    (if (> cur-rand threshold)
      -1
      1)))







(defn traverse [ball]
  (if (< (:x ball) max-wdth)
      (send-off *agent* #'traverse))
  (. Thread (sleep 500))
  (let [
        old-x (:x ball)
        old-y (:y ball)
        velocity (:velocity ball)
        x-increment (:delta-x velocity)
        y-increment (:delta-y velocity)
        new-x (+ old-x x-increment)
        two-pi (. Math PI)
        new-y (+ old-y y-increment)
        new-x-increment (* x-speed-decay x-increment (probably-one))
        new-y-increment (* y-speed-decay y-increment (probably-one))
        new-velocity (struct Velocity new-x-increment new-y-increment)
        ]
  (struct Ball new-x new-y new-velocity)))


(defn start-moving []
  (doseq [cur-ball worldstate]
    (send-off cur-ball traverse)))



(comment
(defn euclidean-distance [p1 p2]
  (let [differences (map - p1 p2)
        sqr-differences (map math/sqr differences)
        sum-differences (reduce + sqr-differences)
        ]
    (math/sqrt sum-differences)
    )
  )
(defn speed [v]
  (let [zero-vector (for [component v]
                      0)]
    (euclidean-distance v zero-vector)))

(defn balls-collide? [frst sec]
  (< (euclidean-distance [(:x frst) (:y frst)]
                         [(:x sec) (:y sec)])
     (* 2 rad)))

(defn dot-product [frst sec] 
  (reduce + (map * frst sec)))

(defn perp [[x-comp y-comp]]
  [(* -1 y-comp) x-comp])

)
