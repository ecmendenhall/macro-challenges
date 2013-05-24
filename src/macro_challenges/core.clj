(ns macro-challenges.core)

(defmacro defn-macro [name args body]
  `(def ~name (clojure.core/fn ~args ~body)))

(defmacro and-macro
  ([] true)
  ([x] x)
  ([x & more]
     `(let [a ~x]
        (if a
          (and-macro ~@more)
          a))))

(defmacro or-macro
  ([] nil)
  ([x] x)
  ([x & more]
     `(let [a ~x]
        (if a
          a
          (or-macro ~@more)))))

(defmacro when-macro [test & body]
  `(if ~test (do ~@body)))

(defmacro when-not-macro [test & body]
  `(if ~test nil (do ~@body)))

(defmacro when-let-macro [[form test] & body]
  `(let [temp ~test]
     (when temp
       (let [~form temp]
         ~@body))))

(defmacro while-macro [test & body]
  `(loop []
     (when ~test
       ~@body
       (recur))))

(defmacro ->-macro
  ([arg] arg)
  ([arg first-form] `(~(first first-form) ~arg ~@(rest first-form)))
  ([arg first-form & more-forms]
     `(->-macro (->-macro ~arg ~first-form) ~@more-forms)))
  
