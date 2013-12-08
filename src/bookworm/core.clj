(ns bookworm.core
    (:use [clj-xpath.core :only [$x]]
          clojure.core.typed
          bookworm.types)

    (:require [clojure.xml :as xml]
              [clojure.zip :as zip]))
(import [nl.siegmann.epublib.epub EpubReader])

(ann ^:no-check contents [Book -> (Vec Resource)])

(ann ^:no-check resource-streams
    [(Seqable Resource) -> (Seqable (Option java.io.InputStream))])

(ann ^:no-check section-map [java.io.InputStream -> Seqable])

(ann ^:no-check resolve-options
    (All [T]
        [(Seqable (Option T)) -> (Seqable T)]))

(ann clojure.core/flatten [(Seqable Seqable) -> Seqable])
(ann get-char-stream [Book -> CharSeq])

(ann clojure.core/take Piece)
(ann clojure.core/drop Piece)
(ann clojure.core/cons [String StringSeq -> StringSeq])
(ann to-strings
    (Fn [CharSeq -> StringSeq]
        [CharSeq AnyInteger -> StringSeq]))

(ann ^:no-check open-book [String -> (Option Book)])

(ann get-text [Book -> StringSeq])

(ann ^:no-check get-title [Book -> (Option String)])

(defn- contents [book]
    (vec (.getContents book)))

(defn- resource-streams [sections]
    (map #(.getInputStream %) sections))

(defn- section-map [xml-stream]
    (->> xml-stream
        slurp
        ($x "//child::p")))

(def ^:private resolve-options (partial filter identity))

(defn- get-char-stream [book]
    (let [sections (contents book)
          streams (resource-streams sections)
          xml-maps (map section-map (resolve-options streams))
          flat (flatten xml-maps)]
            (mapcat #(get % :text) flat)))

(defn- to-strings
    ([char-stream]
        (to-strings char-stream 1000))
    ([char-stream length]
        (cons
            (apply str (take length char-stream))
            (lazy-seq
                (to-strings (drop length char-stream) length)))))

(def open-book
    (let [reader (EpubReader.)]
        (fn [name]
            (.readEpub reader
                (java.io.FileInputStream. name)))))

(def get-text (comp to-strings get-char-stream))

(defn get-title [book]
    (.getTitle book))
