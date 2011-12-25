(ns learning_clojure.ui
  "i am experimenting with ui"
  (:require [clojure.contrib.seq-utils :as seq-utils]
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
(def hgt 500)
(def wdth 500)
(def frame (java.awt.Frame.))
(doto frame
  (.setVisible true)
  (.setSize (java.awt.Dimension. wdth hgt)))
(def gfx (.getGraphics frame))
(doto gfx
  (.setColor (java.awt.Color. 255 128 0))
  )
(defn draw-ball [posn]
  (if (< posn 300)
  (send-off *agent* #'draw-ball))
  (doto gfx
    (.clearRect 0 0 wdth hgt)
    (.fillOval posn 150 50 50))
  (. Thread (sleep 200))
  (+ 30 posn))
  
(def worldstate (ref 0))
(def drawagent (agent 0))
(send-off drawagent draw-ball)
