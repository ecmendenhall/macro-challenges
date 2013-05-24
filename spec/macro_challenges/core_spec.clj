(ns macro-challenges.core-spec
  (:require [clojure.walk :refer [macroexpand-all]])
  (:use speclj.core
        macro-challenges.core))

(describe "defn macro"
  (it "Should expand into the code below"
    (should= '(def add-one (clojure.core/fn [x] (+ 1 x)))
              (macroexpand '(defn-macro add-one [x] (+ 1 x))))))

(describe "and macro"
  (it "Should expand into the code below"
      (should= 'true
               (macroexpand-1 '(and-macro true)))
      (should= `(clojure.core/let [macro-challenges.core/a true]
                  (if macro-challenges.core/a
                    (and-macro true)
                    macro-challenges.core/a))
               (macroexpand-1 '(and-macro true true)))
      (should= `(clojure.core/let [macro-challenges.core/a false]
                  (if macro-challenges.core/a
                    (and-macro true)
                    macro-challenges.core/a))
               (macroexpand-1 '(and-macro false true)))
      (should= `(clojure.core/let [macro-challenges.core/a true]
                  (if macro-challenges.core/a
                    (and-macro true true)
                    macro-challenges.core/a))
               (macroexpand-1 '(and-macro true true true)))
      (should= `(clojure.core/let [macro-challenges.core/a true]
                  (if macro-challenges.core/a
                    (and-macro true false)
                    macro-challenges.core/a))
               (macroexpand-1 '(and-macro true true false)))))
      
(describe "or macro"
  (it "Should expand into the code below"
      (should= '(clojure.core/let [macro-challenges.core/a true]
                  (if macro-challenges.core/a
                    macro-challenges.core/a
                    (macro-challenges.core/or-macro false)))
               (macroexpand-1 '(or-macro true false)))))


(describe "when macro"
  (it "Should expand into the code below"
      (should= '(if (= 2 (+ 1 1))
                       (do :do-stuff))
               (macroexpand-1 '(when-macro (= 2 (+ 1 1))
                                           :do-stuff)))))


(describe "when-not macro"
  (it "Should expand into the code below"
      (should= '(if (and true false)
                  nil
                  (do :do-stuff))
               (macroexpand-1 '(when-not-macro (and true false)
                                               :do-stuff)))))

(describe "when-let macro"
  (it "Should expand into the code below"
      (should= '(clojure.core/let
                    [macro-challenges.core/temp (seq [1 2 3 4 5])]
                  (clojure.core/when macro-challenges.core/temp
                    (clojure.core/let [s macro-challenges.core/temp]
                      (rest s))))
               (macroexpand-1 '(when-let-macro
                                 [s (seq [1 2 3 4 5])]
                                  (rest s))))))

(describe "while macro"
  (it "should expand into the code below"
    (should= '(clojure.core/loop []
                (clojure.core/when (this-is :truthy)
                  (do-stuff)
                  (recur))) 
             (macroexpand-1 '(while-macro (this-is :truthy)
                               (do-stuff))))))

(describe "-> macro"
  (it "should expand into the code below"
    (let [macro-form '(->-macro 1 (+ 2) (* 3))]

      (should= '(macro-challenges.core/->-macro
                  (macro-challenges.core/->-macro 1 (+ 2)) (* 3))
                (macroexpand-1 macro-form))

      (should= '(* (macro-challenges.core/->-macro 1 (+ 2)) 3)
                (macroexpand macro-form))

      (should= '(* (+ 1 2) 3)
                (macroexpand-all macro-form)))))

