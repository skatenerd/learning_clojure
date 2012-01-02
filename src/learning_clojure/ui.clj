(ns learning_clojure.ui
  "i am experimenting with ui"
  (:require [clojure.contrib.generic.math-functions :as math]
            (learning_clojure [ball_behavior :as ball_behavior]))
  )
(def draw-agent (agent nil))
(def frame)
(def gfx)

(defn draw-world [_ worldstate wdth hgt]
  (send-off *agent* #'draw-world worldstate wdth hgt)
  (. Thread (sleep 100))
  ;(doto gfx
    ;(.clearRect 0 0 wdth hgt))
  (doseq [curball worldstate]
    (let [[cur-x cur-y] (:posn @curball)]
      (.fillOval gfx cur-x cur-y ball_behavior/rad ball_behavior/rad)))
  nil)

(defn start_drawing [worldstate wdth hgt]
  (send-off draw-agent draw-world worldstate wdth hgt))


;initialization
(defn initialize [wdth hgt]
  (def frame (java.awt.Frame.))
  (doto frame
    (.setVisible true)
    (.setSize (java.awt.Dimension. wdth hgt)))
  (def gfx (.getGraphics frame))
  (doto gfx
    (.setColor (java.awt.Color. 255 128 0))
    )
)
