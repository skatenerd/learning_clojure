(ns learning_clojure.list_processing
  "i am experimenting with list processing"
  (:require [clojure.contrib.seq-utils :as seq-utils]
            )
  )

(defn my_flatten [nonflat]
  (loop [rtnval '()
         remaining nonflat]
    (let [curelem (first remaining)
          rest_elems (rest remaining)]
      (if (nil? curelem)
        (reverse rtnval)
        (if (list? curelem)
          (recur (apply conj
                        rtnval
                        (my_flatten curelem))
                 rest_elems)
          (recur (conj 
                    rtnval
                    curelem)
                 rest_elems))))))
                      



;(defn flattenonce [nonflat]
  ;(loop [rtnval '()
         ;remaining nonflat]
;
;(defn elimlists [l]
         ;(filter (fn [element] (not (list? element))) l))
;(defn simple? [l]
  ;(= (elimlists l)
     ;l))


(defn main []
  (println "tryna flatten a list")
  (def l '(1 (2 (3 (4 (5) 6) 7) 8) 9 10))
  (println l)
  (println (my_flatten l))
  (comment

  (def ml (map ref (range 0 8)))
  (dosync
    (doseq [cur_ref ml]
      (alter cur_ref (fn [elt]
                 (/ 2 (inc elt))))))
  (println ml)
  )
)
(main)
