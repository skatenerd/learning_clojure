(ns learning_clojure.ball_driver
  "i am experimenting with animation"
  (:require (learning_clojure
              [ball_behavior :as ball_behavior]
              [ui :as ui])
  ))

(def hgt 800)
(def wdth 800)

(def dummy-ball-1 (struct ball_behavior/Ball [200 200] [0 0] 10))
(def dummy-ball-2 (struct ball_behavior/Ball [120 300] [10 0] 20))
(def dummy-ball-3 (struct ball_behavior/Ball [10 30] [13 13] 20))
(def balls [dummy-ball-1 dummy-ball-2 dummy-ball-3]) 
(def ball-agents (map agent balls))
(def worldstate (vec ball-agents))

(defn start-moving []
  (doseq [cur-ball worldstate]
    (send-off cur-ball ball_behavior/traverse worldstate)))

(ui/initialize wdth hgt)
(ui/start_drawing worldstate wdth hgt)
(start-moving)
