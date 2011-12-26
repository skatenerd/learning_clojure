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
(def frame (java.awt.Frame.))
(doto frame
  (.setVisible true)
  (.setSize (java.awt.Dimension. wdth hgt)))
(def gfx (.getGraphics frame))
(doto gfx
  (.setColor (java.awt.Color. 255 128 0))
  )
(comment
(defn draw-ball [posn]
  (if (< posn 300)
  (send-off *agent* #'draw-ball))
  (doto gfx
    (.clearRect 0 0 wdth hgt)
    (.fillOval posn 150 50 50))
  (. Thread (sleep 200))
  (+ 30 posn))
  )


(def starting-posns
  (for [y (range 0 (int (/ hgt 2)) 150)]
    [0 y]))
(def ball-agents (map agent starting-posns))
(def worldstate (vec ball-agents))
(println worldstate)
(def drawagent (agent 0))
(defn draw-world [_]
  (send-off *agent* #'draw-world)
  (doto gfx
    (.clearRect 0 0 wdth hgt))
  (doseq [curball worldstate]
    (let [[x y] @curball]
      (.fillOval gfx x y 50 50)))
  (. Thread (sleep 200))
  nil)
(defn traverse [[x y]]
  (if (< x wdth)
  (send-off *agent* #'traverse))
  (. Thread (sleep 200))
  [(+ x 50) (+ y (* 80 (math/sin (* 6.28 (/ x 360)))))])

(send-off drawagent draw-world)
(doseq [cur-ball ball-agents]
  (send-off cur-ball traverse))
