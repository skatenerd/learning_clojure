(ns learning_clojure.books
  "This is the main namespace"
  (:require [clojure.string
             :as string 
             :only [split join]]
            [clojure.contrib.seq-utils :as seq-utils]
            [clojure.contrib.math :as math]
            )
  (:import (java.util.concurrent Executors))
  )

(defn round [x places]
  (let [coeff (math/expt 10 places)
       ]
    (float (/ (math/round (* coeff x))
               coeff))))

(defn pick_random [coll]
  (let [seq_cast (seq coll)]
    (rand-nth seq_cast)))

(let [default_rand rand]
  (defn rand
    ([x] (default_rand x))
    ([min max] (+ (rand (- max min)) min))))

(defprotocol Taxable
  (tax [x rate]))

(defrecord Book [title author price]
  Taxable
  (tax [x rate] (round (* (+ 1 rate) price) 2)))

(defn rand_name [first_names last_names]
  (string/join " " [(pick_random first_names) (pick_random last_names)]))

(defn rand_title [title_words word_limit]
  (let [f (fn [cur_title _]
            (str cur_title " " (pick_random title_words)))]
    (reduce f "" (range (rand 1 word_limit)))))

(defn get_rand_book [book_params]
  (let [title (rand_title (:title_words book_params) (:word_limit book_params))
        author (rand_name (:first_names book_params) (:last_names book_params))
        price (round (rand (:min (:price_range book_params)) (:max (:price_range book_params))) 2)]
    (Book. title author price)))

(defn rand_books
  ([n book_params]
   (rand_books n #{} book_params))
  ([n s book_params]
   (if (> n 0)
     (recur (- n 1) (conj s (get_rand_book book_params)) book_params)
     s)))


(defn main []
  (println "this function does nothing")
  (comment

  (def ml (map ref (range 0 8)))
  (dosync
    (doseq [cur_ref ml]
      (alter cur_ref (fn [elt]
                 (/ 2 (inc elt))))))
  (println ml)
  )
)
