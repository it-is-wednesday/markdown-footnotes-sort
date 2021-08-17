#!/usr/bin/env bb

(ns mdsort
  "Expects a filename as the only argument, and sorts its footnotes *in place*!

  The links will be sorted in the order they appear"
  (:require [clojure.string :as str]))

(def filename (first *command-line-args*))

(comment
  (def filename "example.md"))

(def text (-> filename java.io.File. .getAbsolutePath slurp))

(when-not filename
  (println "Please provide a file to sort :)")
  (System/exit 1))

(def link-pattern
  "Link to a footnote, looks like: foo[1]"
  (re-pattern #"\[([\d]+)\]([^:])"))

(def footnote-pattern
  "Actual footnotes, e.g. [1]: foo"
  (re-pattern #"\[([\d]+)]:\s+?(.*)"))

(def links ;; => {"1" "1", "2" "3", "3" "2"}
  "A sorted map of each link num and its current position"
  (->> (re-seq link-pattern text)
       (map #(nth % 1)) ;; get the link number
       (distinct)
       (map-indexed (fn [i link-num] [link-num (-> i inc str)]))
       (sort)
       (into {})))

(def footnotes ;; => {"1" "sharks.com", "2" "dogs.com", "3" "cats.com"}
  "Map numbers to their actual content"
  (->> (re-seq footnote-pattern text)
       (map #(vec (drop 1 %)))
       (into {})))

(def sorted-footnotes-part ;; => "[1]: sharks.com\n[2]: cats.com\n[3]: dogs.com"
  "Footnotes part as it will appear in the final text"
  (->> links
       (map (fn [[i link]] (format "[%s]: %s" i (footnotes link))))
       (str/join "\n")))

(def text-sorted-footnotes
  ;; => "Lorem[1] ipsum[3] dolor[2] sit[3]\n[1]: sharks.com\n[2]: cats.com\n[3]: dogs.com"
  (-> (str/replace text footnote-pattern "")
      (str/trimr)
      (str "\n" sorted-footnotes-part)))

(def text-sorted-footnotes-and-links
  ;; => "Lorem[1] ipsum[2] dolor[3] sit[2]\n[1]: sharks.com\n[2]: cats.com\n[3]: dogs.com"
  (str/replace text-sorted-footnotes link-pattern
               (fn [[_ link last-char]]
                 (format "[%s]%s" (get links link) last-char))))

(spit filename text-sorted-footnotes-and-links)
