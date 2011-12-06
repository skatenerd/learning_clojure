(ns learning_clojure.macro_experiments
  "Fun with macros")

;could be implemented with a function..
(defmacro 
  my_apply_twice
  "Supply a function which produces a list along with args."
  [f & args]
  `(do
     (let [evaluated# (map eval '~args)
           frst_rslt# (apply ~f evaluated#)]
       (apply ~f frst_rslt#))))
       
;could be implemented with a function...
;UNLESS f takes a bunch of exprs,
;in which case we dont want to eval them first
(defmacro my_apply [f args]
  `(let [to_eval# (cons ~f ~args)]
     ;without the following eval statement,
     ;the macro would just produce a let statement
     ;yielding a list!
     ;we need to evaluate it...
     ;(println (type (first to_eval#)))
     (eval to_eval#)))

(defmacro 
  eval_if_simple 
  "evaluate the code if it has one function call, else return code"
  [expr]
  (let [filtered (filter list? expr)
        numlists (count filtered)]
    (if
      (= numlists 0)
      `~expr
      `'~expr)))
(defmacro eval_simple_bad [expr]
  "this is just an experiment.  the other one is better"
  `(let [filtered# (filter list? '~expr)
         numlists# (count filtered#)]
     (if
       (= numlists# 0)
       (eval ~expr)
       nil)))


(defn square [x] 
  (let [rslt (* x x)]
    (list rslt)))
