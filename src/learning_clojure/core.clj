(ns learning_clojure.core
  "This is the main namespace"
  (:require [clojure.string
             :as string 
             :only [split join]]
            [learning_clojure.zanzibar :as z]
            [clojure.contrib.seq-utils :as seq-utils]
            [clojure.contrib.math :as math]
            )
  )
(defn print_output [f arg]
  (println (f arg)))

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
(defn get_book [book_params]
  (let [title (rand_title (:title_words book_params) (:word_limit book_params))
        author (rand_name (:first_names book_params) (:last_names book_params))
        price (round (rand (:min (:price_range book_params)) (:max (:price_range book_params))) 2)]
    (Book. title author price)))

(defn rand_books
  ([n book_params]
   (rand_books n #{} book_params))
  ([n s book_params]
   (if (> n 0)
     (recur (- n 1) (conj s (get_book book_params)) book_params)
     s)))
(defn square [x] 
  (let [rslt (* x x)]
    (list rslt)))

(defn main []
  (println "starting the main function")
  (def title_words #{"guns" "pistachio" "rockets" "dinosaurs"})
  (def price_range {:min 0.40 :max 8.00})
  (def first_names #{"jason" "marie" "totoro"})
  (def last_names #{"miyazaki" "dungheep" "cowpie"})
  (def book_params {:title_words title_words
                    :first_names first_names
                    :last_names last_names
                    :price_range price_range
                    :word_limit 3})
  (def book_set (rand_books 10000 book_params))
  (println (pick_random book_set))

  (z/my_apply_twice square (+ 2 2))
  (def incr (fn [x] (+ x 1)))
  (def incr_list (fn [& args]
                   (map incr args)))
  (z/my_apply_twice incr_list 0 1 2 3 4 5)
;  (learning_clojure.zanzibar/my_apply_twice
  (println (z/my_apply + (list 1 2 3 4)))
  (println (z/eval_if_simple (+ 1 2)))
  (println (z/eval_if_simple (+ 1 (* 2 3))))
;  (learning_clojure.zanzibar/my_apply_twice square 2)
  
)

(main)
