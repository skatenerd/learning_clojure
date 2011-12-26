(ns learning_clojure.ui
  "i am experimenting with ui"
  (:require [clojure.contrib.generic.math-functions :as math]
            (learning_clojure [ball :as ball]))
  )

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
(def draw-agent (agent nil))

(defn draw-world [_]
  (send-off *agent* #'draw-world)
  (. Thread (sleep 800))
;  (doto gfx
;    (.clearRect 0 0 wdth hgt))
  (doseq [curball ball/worldstate]
    (.fillOval gfx (:x @curball) (:y @curball) ball/rad ball/rad))
  nil)


(send-off draw-agent draw-world)
(ball/initialize wdth hgt)
(ball/start-moving)
