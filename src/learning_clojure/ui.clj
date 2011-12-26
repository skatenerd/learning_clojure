(ns learning_clojure.ui
  "i am experimenting with ui"
  (:require [clojure.contrib.generic.math-functions :as math]
            )
  )

(defn findmethods [class-to-inspect pattern]
  (let [
        methodsobj (.getMethods class-to-inspect)
        methodsseq (seq methodsobj)
        ]
    (for [m methodsseq
          :let [m-name (.getName m)]
          :when (re-find pattern m-name)
          ]
      m-name)))
(findmethods java.awt.Frame (re-pattern "[vV]is"))
(def hgt 800)
(def wdth 800)
(def rad 50)
(def y-speed-decay 0.95)
(def x-speed-decay 1.0)
(def frame (java.awt.Frame.))
(doto frame
  (.setVisible true)
  (.setSize (java.awt.Dimension. wdth hgt)))
(def gfx (.getGraphics frame))
(doto gfx
  (.setColor (java.awt.Color. 255 128 0))
  )
(def starting-posns
  (for [y (range 0 (int (/ hgt 2)) (* 3 rad))]
    [0 y]))


(defstruct Ball :x :y :velocity)
(defstruct Velocity :delta-x :delta-y)

(def balls
  (for [[x y] starting-posns]
    (struct Ball x y (struct Velocity 20 20))))
(def ball-agents (map agent balls))

(def worldstate (vec ball-agents))
(def draw-agent (agent nil))
(defn probably-one []
  (let [threshold 0.95
        cur-rand (rand)]
    (if (> cur-rand threshold)
      -1
      1)))

(defn draw-world [_]
  (send-off *agent* #'draw-world)
  (. Thread (sleep 800))
;  (doto gfx
;    (.clearRect 0 0 wdth hgt))
  (doseq [curball worldstate]
    (.fillOval gfx (:x @curball) (:y @curball) rad rad))
  nil)

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
(defn traverse [ball]
  (if (< (:x ball) wdth)
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

(send-off draw-agent draw-world)
(doseq [cur-ball worldstate]
  (send-off cur-ball traverse))
