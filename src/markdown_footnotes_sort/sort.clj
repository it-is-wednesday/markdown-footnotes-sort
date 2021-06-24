(ns markdown-footnotes-sort.sort
  (:require
    [clojure.string :as string]))

(defn- pairs->map [pairs]
  (apply assoc {} (flatten pairs)))

(defn- make-label-map [text]
  (->> text
       (re-seq #"\[(\d+)\]:\s?(.*)")
       (map #(seq (drop 1 %)))
       (map (fn [[k v]] [(Integer/parseInt k) v]))
       pairs->map))

(defn- swap-links [s num1 num2]
  (-> s
      (string/replace (re-pattern (str "\\[" num1 "\\]")) "[TEMP]")
      (string/replace (re-pattern (str "\\[" num2 "\\]")) (str "[" num1 "]"))
      (string/replace #"\[TEMP\]" (str "[" num2 "]"))))

(defn- sort-links [text sort-steps]
  (reduce (fn [acc-str [num1 num2]]
            (swap-links acc-str num1 num2))
          text
          sort-steps))

(defn- remove-labels [text]
  (string/trim
   (string/replace text #"\[\d+\]: .*" "")))

(defn- sort-labels [text]
  (let [label-map (->> text make-label-map (into (sorted-map)))
        sorted-labels (for [[num url] label-map]
                        (str "[" num "]: " url))]
    (str (remove-labels text)
         "\n"
         (string/join "\n" sorted-labels))))

(defn- make-link-list [text]
  (->> text
       (re-seq #"\[(\d+)\][^:]")
       (map (fn [match] (->> match (drop 1) first Integer/parseInt)))))

(defn- sorting-steps
  "Return a list of element swaps necessary to sort this collection."
  [coll]
  (loop [[x & xs] coll
         i        1
         acc      []]
    (if (empty? xs)
      acc
      ;; test whether this element is in its right position
      (if (= i x)
        (recur xs (inc i) acc)
        (recur (replace {i x} xs)
               (inc i)
               (conj acc [x i]))))))

(defn mdsort [text]
  (let [steps (-> text make-link-list distinct sorting-steps)]
    (-> text (sort-links steps) sort-labels)))

(comment
  (def content
    (-> "example.md" java.io.File. .getAbsolutePath slurp))

  content
  ;; => "Lorem[1] ipsum[3] dolor[2] sit[3]\n\n[1]: sharks.com\n[2]: dogs.com\n[3]: cats.com\n"

  (def links
    (make-link-list content))

  links
  ;; => (1 3 2 3)

  (def steps
    (sorting-steps (distinct links)))

  steps
  ;; => [[3 2]]

  (sort-links content steps)
  ;; => "Lorem[1] ipsum[2] dolor[3] sit[2]\n\n[1]: sharks.com\n[3]: dogs.com\n[2]: cats.com\n"

  (def label-map
    (make-label-map content))

  label-map
  ;; => {1 "sharks.com", 2 "dogs.com", 3 "cats.com"}

  content
  ;; => "Lorem[1] ipsum[3] dolor[2] sit[3]\n\n[1]: sharks.com\n[2]: dogs.com\n[3]: cats.com\n"

  (swap-links content "2" "3")
  ;; => "Lorem[1] ipsum[2] dolor[3] sit[2]\n\n[1]: sharks.com\n[3]: dogs.com\n[2]: cats.com\n"

  (sort-links content [[3 2] [1 3]])
  ;; => "Lorem[3] ipsum[2] dolor[1] sit[2]\n\n[3]: sharks.com\n[1]: dogs.com\n[2]: cats.com\n"

  (remove-labels content)
  ;; => "Lorem[1] ipsum[3] dolor[2] sit[3]"

  (sort-labels content)
  ;; => "Lorem[1] ipsum[3] dolor[2] sit[3]\n[1]: sharks.com\n[2]: dogs.com\n[3]: cats.com"
  )
