(ns markdown-footnotes-sort.test
  (:require  [clojure.test :refer [deftest is run-tests]]
             [markdown-footnotes-sort.core :as main]))

(deftest sorting-steps
  (is (= (main/sorting-steps [3 1 2])       [[3 1] [3 2]]))
  (is (= (main/sorting-steps [4 2 6 5 1 3]) [[4 1] [6 3] [5 4]]))
  (is (= (main/sorting-steps [])            [])))

(defn run [& args]
  "Helper method for running tests via `clj` CLI."
  (run-tests 'markdown-footnotes-sort.test))
