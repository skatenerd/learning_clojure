(ns learning_clojure.test.books
  (:use [learning_clojure.books])
  (:use [clojure.test]))



(def title_words #{"guns" "pistachio" "rockets" "dinosaurs"})
(def price_range {:min 0.40 :max 8.00})
(def first_names #{"jason" "marie" "totoro"})
(def last_names #{"miyazaki" "dungheep" "cowpie"})
(def book_params {:title_words title_words
                  :first_names first_names
                  :last_names last_names
                  :price_range price_range
                  :word_limit 3})

;finish me!
(defn book_complies_with_params [b constraints]
  (<= (:price b) (:max (:price_range constraints))))

(deftest rand_book_compliance
  (let [cur_rand_book (get_rand_book book_params)]
    (is (book_complies_with_params cur_rand_book book_params))))

(deftest pick_rand_book
  (let [book_set (rand_books 10000 book_params)
        cur_rand_book (pick_random book_set)]
    (is (book_complies_with_params cur_rand_book book_params))))

