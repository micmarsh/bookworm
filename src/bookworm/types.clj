(ns bookworm.types
    (:use clojure.core.typed))
(import [nl.siegmann.epublib.epub EpubReader])

(def-alias Book nl.siegmann.epublib.domain.Book)
(def-alias Resource nl.siegmann.epublib.domain.Resource)

(def-alias StringSeq (clojure.lang.LazySeq String))
(def-alias CharSeq (clojure.lang.LazySeq Character))

(def-alias Piece [AnyInteger CharSeq -> CharSeq])
