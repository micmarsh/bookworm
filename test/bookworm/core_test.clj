(ns bookworm.core-test
  (:require [clojure.test :refer :all]
            [bookworm.core :refer :all]))

(deftest open-books
  (testing "Can open an epub file"
    (let [book (open-book "samples/zarathustra.epub")]
        (is (boolean book))))
  (testing "Can't open a non-existent epub file"
    (try
        (let [book (open-book "lulzbook.epub")]
            (is false))
        (catch Exception e
            (is true)))))

(def TITLE "Thus Spake Zarathustra / A book for all and none" )

(deftest metadata
    (let [book (open-book "samples/zarathustra.epub")]
        (testing "Can read the title of the book"
            (is (= (get-title book) TITLE)))
        (testing "Can create a lazy sequence of the book's contents"
            (let [text (get-text book)]
                (println "woah")
                (is (= (type text) clojure.lang.LazySeq))))))
