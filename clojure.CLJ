;; clojure repl
;; testing out on web, I learned the following

(+ 1 1)

(/ 1 2)

(/ 1 2.0)

(+ 1 2 3 4 5)

(defn square [x] (* x x))
(square 2)

(fn [x] (* x x) 2)

(def square (fn [x]) (* x x))

(map inc [1 2 3 4 5])

(defn hello-world [name]
	(println (format "Hello, %s!" name)))
(hello-world "you")

;; comparison of doseq, dotimes and the more used map
(defn square-list [nums]
	(doseq [nums]
		(println (str num))))

(defn square-list-times [maxn]
	(dotimes [n maxn]
		(println (str (* n n)))))

(defn square-list-map [minn maxn]
	(map square (range minn (inc maxn)))) 	; square (def abv) on every n from min n to max n

;;
;; from Clojure By Example
;; https://kimh.github.io/clojure-by-example/
;;

;; executed statement enclosed in parens is a Clojure "form"
(println "Hello, world!") 	; returns nil

(def h "Hello, world!")
(println h)

;; symbol that is not evaluated
(type 'h)

;; evaluate symbol then tell type
(type h)

;; error trying to resolve unbound symbols
;; (println b)

;; let = bind locally within a form using symbol-value vectors
(let [l "light" ld (str l " and darkness")] (println (str "let there be " ld)))

;; symbol not usable outside the let (similar to private variables in other langs)
;; (println ld)

;; Clojure evaluates and resolves each symbol in its scope
;; this is "lexical scope" - compiler looks for location of symbols in your program
(let [a "aaagh"](let [a "AAAH"] (println a))) 	; what do you think will be printed?

;; resolution bubbles up until finding a binding
(let [a "aaaaaaaah"] (let [] (println a))) 			; no symbol 'a in empty binding so prints outer 'a value
;; - if symbol is out of lexical scope returns "Unable to resolve symbol"
;; - lexical scope as familiar from other langs
;; - dynamic scope also exists, but don't worry about that now

;; def - bind symbols for access outside of declaration scope
(def l "light")

;; defn - function name, "doc description", [arguments], (body)
(defn hello "Say hi to name argument" [name] (println (str "Hi, " name)))

;; doc - read documentation
(doc hello)

;; add metadata
(defn hello "Say hi to name argument" {:added "1.0" :static true} [name] (println (str "Hi, " name)))

;; meta - expand metadata (note you must convert fn into var object to avoid passing value)
(meta (var hello))
(meta #'hello)			; reader macro for var (#') works the same way

;; anonymous fn
;; fn is first-class object: can pass to fn, return it from fn, bind to var, etc.
(fn [] (println "Hello world"))
(def hello-world-fn (fn [] println "Hello world!"))
(hello-world-fn)

;; #() is shortcut for (fn)
#(+ 1 1)

;; passed-in args replace %
#(+ 2 %)
(let [plus #(+ 2 %)](plus 2))

;; numbered arguments
(let [plus #(+ 2 %1 %2 %3)](plus 2 2 3))

;; passing fn as arg
(def say-hello (fn [name] println(str "Hello " name)))
(def make-me-speak (fn [phrase-fn name] (phrase-fn name)))
(make-me-speak say-hello "world!")

;; closure: inner fn returned from outer and does things with args from outer fn
(defn inner [from-outer-arg] (fn [] (println from-outer-arg)))
(def outer1 (inner "a line printed by inner"))
(def outer2 (inner "another line printed by inner"))
;; note that these print different things even when from-outer-arg doesn't change

;; namespaces
;; create a namespace
(create-ns 'my.clojure.example)
;; move between namespaces
(in-ns 'user)
my.clojure.example> (defn current-language [] "Clojure")
my.clojure.example> (current-language)
my.clojure.example> (in-ns 'user)
user> (current-language) 	; error resolving symbol
;; require namespace to provide scope
user> (require 'my.clojure.example)
user> (my.clojure.example/current-language)
user> (require '[my.clojure.example :as mce])
user> (mce/current-language)
;; require multiple namespaces
user> (require '[my.clojure.example :as mce] '[clojure.core :as cljc])
;; refer a loaded namespace to call methods without namespace prefix
user> (require 'my.clojure.example)
user> (refer 'my.clojure.example)
user> (current-language)
;; use to load and refer at once
user> (use 'my.clojure.example)
user> (current-language)
;; import a Java namespace
user> (import java.util.Date)
user> (new Date)
;; ns creates namespace and allows passing require, use, import
user> (ns example.namespace
	(:require clojure.java.io)
	(:use clojure.data)
	(:import [java.util List Set]))

;; control flow
;; if statements have a predicate, an arg for true (if), an arg for false (else)
(if true
	(println "Printed if true, so always printed")
	(println "Printed if false, so never printed"))
;; run multiple if branches by passing do
(if true
	(do
	(println "a one")
	(println "and a two"))) 	; prints a one, a two, nil
;; if-let expressions to use conditional test result later
(defn positive-numbers [numbers]
	(if-let [pos-nums (not-empty (filter pos? numbers))] 	; bind filtered collection to pos-nums
		pos-nums 										; if not empty print pos-nums
		"no positive numbers")) 		; else
(positive-numbers [-2 -1 1 2]) 	; 1 2
;; filter returns empty sequence which is NOT falsy
;; so not-empty returned nil for possibility of running else branch
(filter (pos? [-2 -1])) 						; []
(boolean (filter pos? [-2 -1])) 		; true
(not-empty (filter pos? [-2 -1])) 	; nil
;; when for truthy concerns
(when true		; like just running the if branch
	(println "always print this")
	(println "this, too"))
(when false 	; nil -
	(println "never print this")
	(println "nor this"))
;; when-let for assigning and using later like if-let
(when-let [pos-nums (filter pos? [-2 -1 1 2])] 	; never run else branch
	pos-nums
	(println "print if positive numbers"))
;; switch case - pass statements, last evaluates like default
user=> (defn case-test
	[n]
	(case n
		1 "n equals 1"
		2 "n equals 2"
		"n is not 1 or 2"))
#'user/case-test
(println (case-test 1))
(println (case-test 3))
;; cond - write your own tests instead of case tests (which use =)
(defn cond-test
	[n]
	(cond
		(= n 1) "n equals 1"
		(and (> n 3) (< n 10)) "n is greater than 3 and less than 10"
		:else "n is not 1, nor between 3-10 (exclusive)"))
;; condp - use a predicate for the condition
(defn condp-test
	[n]
	(condp contains? n
		#{1 2 3} "n is 1, 2 or 3"
		"n is not 1, 2 or 3"))
(contains? #{1 2 3} 2) 			; true
(println (condp-test-2 2)) 	; first branch runs
(contains? #{1 2 3} 2) 			; falsy
(println (condp-test-2 5)) 	; default branch runs
