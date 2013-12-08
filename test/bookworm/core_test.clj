(ns bookworm.core-test
  (:require [clojure.test :refer :all]
            [bookworm.core :refer :all]))

(def PATH "samples/seven-habits.epub")

(deftest open-books
  (testing "Can open an epub file"
    (let [book (open-book PATH)]
        (is (boolean book))))
  (testing "Can't open a non-existent epub file"
    (try
        (let [book (open-book "lulzbook.epub")]
            (is false))
        (catch Exception e
            (is true)))))

(def TITLE "Seven Habits of Highly Effective People" )

(deftest metadata
    (let [book (open-book PATH)]
        (testing "Can read the title of the book"
            (is (= (get-title book) TITLE)))
        (testing "Can create a lazy sequence of the book's contents"
            (let [text (get-text book)]
                (is (= (type text) clojure.lang.Cons))))))
