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

;; types
;; boolean
;; false and nil are false, all else evaluates to true
(boolean 0) 		; true
(boolean nil) 	; false
;; string
;; string literal in double quotes
"my string"
;; str concatenates (no string interpolation)
(str "my " "string")
(+ "my " "string") 			; ClassCastException
(let [first "Prename"
	last "Famname"]
	(str "Hi! I'm " first " " last))
;; format string (str concatenation can be achieved this way, too)
;; %s for string
(format "Hi! I'm %s %s" "Prename" "Familyname")
;; %i for integer
(format "How many names? %i names!" 2)
;; %.Nf for floating point to N places
(format "What is pi to 5 places? %.5f" 3.1415927)
(format "What is pi to 2 places? %.2f" 3.1415927)
;; %b for boolean
(format "The number 0 is true in Clojure: %b" 0)
;; integer
;; add, subtract, multiply, divide
(+ 2 3)
(- 2 3)
(* 2 3)
(/ 2 3) 		; represented as fraction 2/3!
;; modulo
(mod 3 2) 	; 1
;; max value
(max 1 2 3 4 5)
;; min value
(min 1 2 3 4 5)
;; exponential operations not predefined
(defn power
	[x n]
	;; reduce takes repeat sequence and reduces multiplying each element
	(reduce * (repeat n x)))
)
;; bigint for very large numbers (N is bigint literal)
(+ 9223372036854775807 10) 			;; ArithmeticException integer overflow
(+ (bigint Long/MAX_VALUE) 10)

;; lists
;; basic collections - less useful than more used collections like vectors, maps
'(1 2 3) 	; ' prevents evaluation
(1 2 3) 	; ClassCastException
;; conj to add value to front of list
(conj '(1 2 3) 4) 	; (4 1 2 3)
;; removing elements not supported by default
;; seq library ("Sequences") does have remove, filter, drop
(seq '(-1 1 2 3))
(filter pos? seq)
;; read a value by accessing nth index
(nth '(1 2 3) 1) 	; 2
;; count length of list
(count '(1 2 3)) 	; 3

;; vectors
;; like lists but "more efficient and useful"
;; vector literal - no need for ' because not evaluated
[1 2 3]
;; conj to add values to end
(conj [1 2 3] 4)
;; like lists, remove not supported by default - see seq
;; nth accesses the indexed element
(nth [1 2 3] 1) 	; 2
;; first and last access initial and final elements
(first [1 2 3]) 	; 1
(last [1 2 3]) 		; 3
;; .indexOf is a Java interop for getting the index of a value
;; returns the first index or -1 if element is not in vector
(.indexOf [1 2 3] 4) 	; -1

;; sets
;; unordered collections of unique values
;; set literal
#{1 2 3}
#{1 2 3 3} 	; IllegalArgumentException Duplicate key: 3
;; conj to add unique value to a set (not added if exists)
(conj #{1 2 3} 4)
(conj (conj #{1 2 3} 4) 4)
;; disj to remove value from a set
(disj #{1 2 3} 3) 	; ${3 2}
(disj #{1 2 3} 5)		; ${1 3 2}
;; sort to return ordered list of elements in the set
(sort (conj #{1 2 3} 4)) 	; (1 2 3 4)
;; contains? to check if set has value
(contains? #{1 2 3} 3) 	; true
(contains? ${1 2 3} 4)	; false
;; clojure.set/subset? to check if set is a subset of another
(clojure.set/subset? #{1 2} #{1 2 3 4})		; true
(clojure.set/subset? #{0 4} #{1 2 3 4})		; false
;; clojure.set/superset? to check if set is a superset of another
(clojure.set/superset? #{1 2 3 4} #{1 2})	; true
(clojure.set/superset? #{1 2 3 4} #{0 4})	; false

;; maps
;; kv data structure
;; map literal {} - most types accepted as keys but commonly string or keyword
{:fruit "lilikoi" :hapiness "up" :animal "dog"}
{"fruit" "lilikoi" "happiness" "up" "animal" "dog"}
;; get value from key or nil if key does not exist
(get {:fruit "lilikoi" :happiness "up" :animal "dog"} :animal) 	; "dog"
(get {:fruit "lilikoi" :happiness "up" :animal "dog"} :vehicle) ; nil
;; keywords can be used like functions to get values
(:animal {:fruit "lilikoi" :happiness "up" :animal "dog"})
;; assoc to add a kv pair - existing keys will see value replaced
(:assoc {:fruit "lilikoi" :happiness "up" :animal "dog"} :vehicle "surfboard")
;; merge to combine two maps
(:merge {:fruit "lilikoi" :happiness "up" :animal "dog"} {:vehicle "surfboard" :fruit "rambutan"})
;; keys to get all keys from map
(keys {:fruit "banana" :hapiness "wow" :animal "hyena"}) 	; (:fruit :hapiness :animal)
;; vals to get all values from map
(keys {:fruit "banana" :hapiness "wow" :animal "hyena"}) 	; ("banana" "wow" "hyena")

;; sequences
;; abstract data types behind lists, vectors, maps, sets
;; Seq library provides functions that apply to any collection type
;; "seqable" data types can have these functions applied to them
(map inc [ 1 2 3 ]) 	; (2 3 4)
(map inc `( 1 2 3 ))	; (2 3 4)
(map inc #{ 1 2 3 }) 	; (2 4 3)
;; note that collection functions may need to be applied to keys/values
;; inside map, not called on whole map
(map key {:a 1 :b 2 :c 3}) 	; (:a :b :c)
;; seq constructs sequence, converting any seqable data type to sequence
(seq '(1 2 3)) 	; (1 2 3)
(seq [1 2 3]) 	; (1 2 3)
(seq #{1 2 3}) 	; (1 3 2)
(seq {:a 1 :b 2 :c 3}) 	; ([:a 1][:b 2][:c 3])
;; consider the type of a sequence
(type (seq [1 2 3])) 	; clojure.lang.PersistentVector$ChunkedSeq
;; first to get initial element
(first [1 2 3]) 	; 1
(first "Hello") 	; \H
;; rest to get all elements except initial one
(rest [1 2 3]) 		; (2 3)
(type [1 2 3])		; starts out as a PersistentVector
(type (rest [1 2 3])) 	; but returns a PersistentVector$ChunkedSeq
;; cons to add to head of sequence and return a new one
(cons 0 '(1 2)) 	; (0 1 2)
;; concat to combine seqs
(concat '(1 2 3) '(4 5 6)) 	; (1 2 3 4 5 6)
;; map to apply function to each element in seq
(map inc [ 1 2 3 ])
(map (fn [x] (inc (val x))) {:a 1 :b 2 :c 3}) 	; (2 3 4)
;; reduce to get single value from seq
;; requires two-arg function since each time prev value compared to next
(reduce + [1 2 3 4]) 		; 10
(reduce inc [1 2 3 4]) 	; ArityException
(reduce + 2 [1 2 3 4]) 	; starting value instead of using first in seq
;; into to insert all elements of sequence into another sequence
(into [1 2 3] `(4 5 6)) ; [1 2 3 4 5 6]
;; into to convert between seq types
(into [] `(1 2 3))
(into {} [[:a 1] [:b 2] [:c 3]])
;; reverse to reverse seq
(reverse [1 2 3]) 	; (3 2 1)
;; iterate to get infinite series - CAUTION will not terminate!
(iterate + 0)
;; range to get seq of numbers between two points, optionally passing interval
(range 1 4) 	; (1 2 3)
(range 0 6 2) ; (0 2 4)
;; repeatedly to repeat function
(repeatedly 5 (fn [] (println "Hello world!")))
;; doseq to run on each element of seq (compare other languages for / for-each)
(doseq [dogs ["Fluffstuff" "Waggler" "Floppy" "Gup"]] (println dogs))
;; doseq supports binding multiple values
(doseq [a [1 2] b [3 4]] (println (+ a b)))
;; take to get first n elements
(take 2 (range 0 4)) 	; (0 1)
;; take-while to get elements until condition met
;; NOTE not meant for taking elements only matching condition (that's select)
;; me: I don't see `select` - was `filter` intended here?
(take while neg? [-3 -2 -1 0 1 2 3]) 	; (-3 -2 -1)
;; drop to remove first n elements
(drop 2 (range 0 4)) 	; (2 3)
;; drop-while to remove elements until condition met
(drop-while neg? [-3 -2 -1 0 1 2 3]) 	; (0 1 2 3)
;; filter to take elements matching condition
;; this guide sees the filter rule as "remov[ing]" those elements, while
;; I conceive of it as filtering / straining through an operation
(filter pos? [-1 2 3]) 	; (2 3)
;; the operation is a predicate and can be your own defined function
(filter (fn [v] (= v 2)) [-1 2 3]) 	; (2)
;; remove to remove elements with predicate and return "what's removed"
(remove pos? [-2 -1 0 1 2]) 	; (-2 -1)
;; partition-by to split collection based
(partition-by #(< 3 %) [1 2 3 4 5 6]) 		; ((1 2 3) (4 5 6))
;; partition-by does not merge repeats of the same value
(partition-by #(< 3 %) [1 2 3 4 5 6 1 3]) ; ((1 2 3) (4 5 6) (1 3))
;; group-by to split and merge collection
;; group-by returns a map with condition return as keys
(group-by #(< 3 %) [1 2 3 4 5 6 1 3]) 	; {false [1 2 3 1 3], true [4 5 6]}
;; most seqs are lazy, and `map`, `range`, `reduce` return lazy seqs
;; `println` can handle lazy seqs, printing one at a time
;; here `iterate` generates each number when requested and returns to `println`
(println (iterate inc 1)) 	; count up from 1 - press CTRL + C to stop
;; lazy behavior allows running `iterate` without crashing
(println (take 5 (iterate inc 0))) 	; (0 1 2 3 4)
;; for to use list comprehension NOT for looping
;; NOTE Clojure has no mutable local vars in loops, so no imperative looping!
(for [x '(0 1 2)] (+ 10 x)) 	; (10 11 12)
;; this is looking for the set of all x that matches condition
(for [x [0 1 2 3 4 5]
		:let [y (* x 2)] 	; let binds "intermediate values"
		:when (even? y)] 	; when evaluates body only when predicate is true
	y) 	;	(0 4 8)
;; use w(hile to stop evaluation when predicate is false
(for [x (range 5) :while (not= x 3))]) 	; (0 1 2)
;; for does depth first nesting so good for combining elements
(for [x ['a 'b 'c] y [1 2 3] [x y]]) 	; ([a 1] [a 2] [a 3] [b 1] ... [c 3])

;; recursion
;; function calling self inside its own definition
(defn fib-rec [n]
	(if (or (= n 0) (= n 1))
		n (+ (fib-rec (- n 1)) (fib-rec (- n 2)))))
(fib-rec 6) ; 8
;; recur to bind args to new values and call function with those values
(defn fib-recur [n]
	(let [fib (fn [first second n])
		(if (= iteration n)
		one
		(recur second (+ first second) (inc n)))]
	(fib 0N 1N 0))) 	; use bigint to avoid stack overflow
(fib-recur 6) 	; still 8
;; example of counting down until reaching 0
(defn countdown [result n]
	(if (= n 0)
		result
		(recur (conj result n) (dec n))))
(countdown [] 5) 	; [5 4 3 2 1]
;; recur optimizes recursion vs "simple recursion" where function is called inside itself
;; recursive calls create stack frame on mem so deep recursion means many stack frames
;; recur is tail recursive, calling function at end of its definition to avoid this
(fib-rec 100000) 		; StackOverflowError
(fib-recur 100000) 	; takes a long time but will compute
;; error when trying to call recur not at end of function
(defn fibo-loop [current next iter]
	(if (= 0 iter)
	current
	(+ 0 (recur next (+ current next) (dec iter))))) 	; error can only recur from tail position
;; loop/recur to write statements syntactically similar to imperative loop
(defn countup [max]
	(loop [count 0]
		(if (= count max)
			(println (str "Finished counting to " max))
			(do
				(println (str "Number: " count)
				(recur (inc count))))))) 	; continue counting with next value
;; NOTE the number of args to recur must match the recursion function
;; NOTE loop/recur is just doing recursion but with a friendly paint coat

;; macros
;; replaced with defined Clojure code before evaluated
;; use create your own syntax, control flow, value types, ...
;; defmacro to define a macro, using ' before if and not
(defmacro unless [test when]
	"Evaluates then when test evaluates to falsey")
	(list 'if (list 'not test) 	; if and not without apostrophe raises error
	 then))
(unless false (println "false!"))
;; macroexpand to see the code that replaces it
(macroexpand '(unless false (println "hi"))) 	; apostrophe to prevent evaluation
;; quote to return the unevaluated expression itself
(+ 1 1) 				; 2
(quote (+ 1 1)) ; (+ 1 1)
;; ' as just a more concise way to write quote
'(+ 1 1) 				; (+ 1 1)
;; so that's why if and not needed ': prevent evaluation inside the macro definition
;; it's also why loaded namespaces need ': avoid resolving the namespace symbol
(require 'clojure.string)
;; syntax-quotes to return "fully qualified namespace"
;; please use to avoid namespace conflicts when defining macros
'(dec (inc 1)) 	; (dec (inc 1))
`(dec (inc 1)) 	; (clojure.core/dec (clojure.core/inc 1))
;; unquote to evaluate an expression in syntax (but not regular) quoting
`(+ 1 ~(inc 1)) ; (clojure.core/+ 1 2)
'(+ 1 ~(inc 1)) ; (+ 1 clojure.core/unquote (inc 1))
;; unquote-splice to expand sequences and splice sequence contents into syntax quote
`(+ ~(list 1 2 3)) 	; (clojure.core/+ (1 2 3))
`(+ ~@(list 1 2 3)) ; (clojure.core/+ 1 2 3)

;; threading macros
;; macros for nested structres
;; NOTE nothing to do with parallel computing
(conj (conj (conj [] 1) 2) 3) 	; join numbers to seq one by one
;; thread-first macro, starting from initial value then joining each
(-> []
	(conj 1)
	(conj 2)
	(conj 3))
;; thread-last macro to pass evaluated forms to the last argument of a previous form
(->> ["you" "me"]
	(map clojure.string/upper-case)
	(map #(str "Hello to " %))) 	; "Hello to YOU Hello to ME"
;; Thread-last passes the sequence to the first map which capitalizes both
;; then the second map which runs an anon func (that's the #() syntax) to build
;; the string.
;; Here's the equivalent without threading macros.
(map #(str "Hello to " %) (map clojure.string/upper-case ["you" "me"]))

;; delays
;; delay to defer evaluation
(def awaited (do [] (+ 1 2)))					; 3
(def awaited (delay (do [] (+ 1 2)))) ; not done immediately
;; force to evaluate delayed expression
(force awaited) 	; 3
;; could also use anonymous function to print later
(def awaited (fn [] (+ 1 2)))
(awaited) 	; 3
;; but here delay is only evaluated once and cached for future calls
;; any statements executed (not evaluated cache) will just run once
(defn awaited (delay (do [] (prn "Summing") (+ 1 1))))
(force awaited) 	; "Summing" 2
(force awaited) 	; 2
;; NOTE second force did not print because prn already executed

;; futures
;; sync thread to print after set amount of time
(do (Thread/sleep 3000) (println "Hello world!"))	; Hello world! after 3s
;; future to run thread after doing immediate work
(do (future (Thread/sleep 3000) (println "Hi again!")) (println "Hi!"))	; Hi!, wait 3s, Hi again!
;; deref to get the value from the future call (to "dereference a future")
(let [future-evaluation (future (inc 1))] (println future-evaluation)) 	; #<core$future_call...>
(let [future-evaluation (future (inc 1))] (println (deref future-evaluation))) 	; 2
;; @ is a shorthand for dereferencing a future
(let [future-evaluation (future (inc 1))] (println @future-evaluation)) ; 2
@(future (Thread/sleep 3000) "Hi once more!") 	; Hi once more! after 3s
;; dereferencing a future blocks until the future returns
;; pass deref a timeout to wait plus value to return if timeout
(deref (future (Thread/sleep 1000) "future!") 2000 "not waiting") ; future!
(deref (future (Thread/sleep 3000) "future!") 2000 "not waiting") ; not waiting
;; combine future and deref to run many tasks in different blocking threads
(let [sleep-wait
	(map (fn [time]
		(future
			(Thread/sleep time)
			(println (str time "sec"))))
			[1000 2000])]
	(doall (map deref sleep-wait))
	(println "finished")) 	; 1000 sec, 2000 sec, finished
;; realized? to check if a future is done
(def new-future(future (Thread/sleep 5000)))
(repeatedly 6
	(fn []
	(println (realized? new-future))
	(Thread/sleep 1000))) 	; false, false, false, false, true

;; promises
;; promise to defer evaluation until you have some value in hand
;; easy to implement using callback - promise, listener, fire callback when value passed to promise
(def example-promise (promise))
(def listen-callback (fn []
	(println "Listening for promise")
	(future (println "Callback: " @example-promise))))	; NOTE dereferenced promises block just like dereferenced futures
(defn time-consuming-task []
	(Thread/sleep 5000)
	(deliver example-promise "This is the value once dereferenced!"))
;; start listener and wait for task
;; NOTE the dereferencing of example-promise blocks for 5 seconds then callback fires
(listen-callback)(time-consuming-task) 	; Listening for promise, wait 5 sec, This is the value...

;; atoms
;; atom to point to a value
;; NOTE atoms as countering common opinion that there's no state in Clojure therefore it's useless in real applications
(def atom-string (atom "hi!"))
(def atom-map (:a "a" :b "b"))
(deref atom-string)	; hi!
@atom-string 				; hi!
;; reset! to set an atom value
;; NOTE this value overwrites the original and is not based on previous value
;; NOTE atoms are mutable and can be reset infinitely
@atom-string 	; hi!
(reset! atom-string "hi, world!")
@atom-string 	; hi, world!
;; swap! to to set an atom value with a function
;; swap! takes current value as an argument, setting state based on previous state
(def atom-int (atom 0))
(swap! atom-int
	(fn [current-val]
		(inc current-val))) 	; 1
;; the swap function can take multiple arguments
(def atom-int (atom 10))
(defn multiply
	[current-atom num]
	(* current-atom num))
(swap! atom-int multiply 10) ; 100
;; atoms are threadsafe unlike mutable variables in other languages
(def x 0)
(repeatedly 10 (fn [] (def x (inc x))))
x 	; 10
;; now do the same but in parallel threads using future
;; NOTE because of the threads there is no guarantee on the return value of x
(repeatedly 10 (fn [] (future (def x (inc x))))) 	; sometimes 8, sometimes 5, ...
;; swap! to guarantee the final value using [compare-and-swap](https://en.wikipedia.org/wiki/Compare-and-swap)
(def x (atom 0))
(repeatedly 10 (fn [] (future (swap! x inc))))
@x 	; 10

;; refs
;; ref to manage multiple states consistently
;; NOTE atom (above) manages changes to single state
(def example-ref (ref 0))
@example-ref 	; 0
;; dosync to start a transactional update to ref
;; ref-set to change the value
(dosync
	(ref-set example-ref 1)
	(ref-set example-ref 2)) 	; 2
@example-ref 	; 2
;; run update inside dosync otherwise no transaction is running to make updates
(ref-set example-ref 3) 	; IllegalStateException
;; alter to update a ref using a function
(def example-ref (ref 0))
(dosync
	(alter example-ref
	(fn [current_value]
		(inc current_value)))) 	; 1
@example-ref 	; 1
;; ref update functions can take multiple args
(def example-ref (ref 100))
(defn multiply
	[current_value n]
	(* current_value n))
(dosync
	(alter example-ref multiply 10)) 	; 1000
;; Transactions #1: exploring how transactions work from inside dosync
(def user (ref {}))
(dosync
	(alter user merge {:name "Ruff"})
	(throw (Exception. "it went wrong!!"))
	(alter user merge {:age 2})) 	; Exception it went wrong!!
@user 	; {}
;; NOTE alter didn't update because the dosync must complete successfully
;; compare this to the way atoms work
(def user-record (atom {}))
(do
	(swap! user-record merge {:name "Ruff"})
	(throw (Exception. "it went wrong!!"))
	(swap! user-record merge {:age 2}))
@user-record 	; {:name "Ruff"}
;; NOTE the first swap! updated but the error between them left the atom half updated
;; Transactions #2: exploring how transactions work from outside dosync
(def my-ref (ref 0))
(future
	(dosync
		(alter my-ref inc)
		(Thread/sleep 2000))) 	; pending future
(println @my-ref) 	; 0
;; wait 2 secs then print to see that the update to ref is now committed
(println @my-ref) 	; 1

;; Java
;; "Java interop" - CLJ code can borrow from Java libraries and execute Java
(new java.util.Date)
;; pass constructor arg, use dot form after class name
(java.util.Date "2018/08/08")
(java.util.Date.)
;; bind Java instance like CLJ value
(let [date (new java.util.Date)]
	(println date))
;; example calling Java class methods using Classname/Method
(Math/pow 3 3) 	; 9
;; example calling Java instance methods using dot notation
(let [date (new java.util.Date)]
	(.toString date)) 	; (. date toString) works, too!
;; example passing arguments to instance methods
(let [date-1 (new java.util.Date)
	date-2 (new java.util.Date)]
	(.equals date-1 date-2)) 	; true
