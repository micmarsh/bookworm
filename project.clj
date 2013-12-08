(defproject bookworm "0.1.0"
  :description "A Library for Reading Epubs in Clojure"
  :url "https://github.com/micmarsh/bookworm"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                [nl.siegmann.epublib/epublib-core "3.1"]
                [org.clojure/core.typed "0.2.19"]
                [com.github.kyleburton/clj-xpath "1.4.3"]
                [enlive "1.1.5"]]
  :plugins [[lein-localrepo "0.4.0"]])
