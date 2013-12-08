(ns bookworm.core

    (:use [net.cgrand.enlive-html
          :only [html-resource select]]
          clojure.core.typed
          bookworm.types)

    (:require [clojure.xml :as xml]
              [clojure.zip :as zip]))
(import [nl.siegmann.epublib.epub EpubReader])

(ann ^:no-check contents [Book -> (Vec Resource)])
(defn contents [book]
    (vec (.getContents book)))

(ann ^:no-check resource-streams
    [(Seqable Resource) -> (Seqable (Option java.io.InputStream))])
(defn resource-streams [sections]
    (map #(.getInputStream %) sections))

(ann ^:no-check section-map [java.io.InputStream -> Seqable])
(defn section-map [xml-stream]
    (-> xml-stream
        html-resource
        (select [:p])))

(ann ^:no-check resolve-options
    (All [T]
        [(Seqable (Option T)) -> (Seqable T)]))
(def ^:private resolve-options (partial filter identity))

(ann clojure.core/flatten [(Seqable Seqable) -> Seqable])
(ann get-char-stream [Book -> CharSeq])
(defn get-char-stream [book]
    (let [sections (contents book)
          streams (resource-streams sections)
          xml-maps (map section-map (resolve-options streams))
          flat (flatten xml-maps)
          contents (map :content flat)
          clean (filter (partial every? string?) contents)]
            (apply concat (map seq clean))))

(ann clojure.core/take Piece)
(ann clojure.core/drop Piece)
(ann clojure.core/cons [String StringSeq -> StringSeq])
(ann to-strings
    (Fn [CharSeq -> StringSeq]
        [CharSeq AnyInteger -> StringSeq]))
(defn to-strings
    ([char-stream]
        (to-strings char-stream 1000))
    ([char-stream length]
        (cons
            (apply str (take length char-stream))
            (lazy-seq
                (to-strings (drop length char-stream) length)))))

(ann ^:no-check open-book [String -> (Option Book)])
(def open-book
    (let [reader (EpubReader.)]
        (fn [name]
            (.readEpub reader
                (java.io.FileInputStream. name)))))

(ann get-text [Book -> StringSeq])
(def get-text (comp to-strings get-char-stream))

(ann ^:no-check get-title [Book -> (Option String)])
(defn get-title [book]
    (.getTitle book))
