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
(meta #'hello)			; reader macro for var (#'') works the same way
