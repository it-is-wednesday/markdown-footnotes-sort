(ns markdown-footnotes-sort.core
  (:gen-class)
  (:require [markdown-footnotes-sort.sort :refer [mdsort]]
            [clojure.tools.cli :refer [parse-opts]])
  (:import java.io.File))

(set! *warn-on-reflection* true)

(def cli-options
  [["-i" "--in-place" "Make changes to the file in place, overriding current content"]
   ["-h" "--help" "Print this usage text"]])

(defn -main [& args]
  (let [{:keys [options arguments summary errors]} (parse-opts args cli-options)]
    (cond
      ;; any errors occured while parsing CLI args
      errors (print (str (clojure.string/join "\n" errors)
                         "\n"
                         summary))

      ;; --help provided or no arguments provided at all (arguments, not options!)
      (or (empty? arguments)
          (:help options))   (println summary)

      :else (let [filename (first arguments)
                  sorted (-> filename slurp mdsort)]
              (if (:in-place options)
                (spit filename sorted)
                (println sorted))))))

(comment
  (-main "example.md")
  (-main "-i" "example.md")
  (-main "example.md" "-i")
  (-main "-abc")
  (-main))
