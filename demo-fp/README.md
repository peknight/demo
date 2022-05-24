# Note

## Functional Programming in Scala

[fpinscala github repo](https://github.com/fpinscala/fpinscala.git "Functional Programming in Scala's github repository")  

### Referential Transparency

* An expression `e` is referentially transparent with regard to a program `p` if every occurrence of `e` in `p` can be
replaced by the result of evaluating `e` without affecting the meaning of `p`.

* A function `f` is pure if the expression `f(x)` is referentially transparent for all referentially transparent `x`.


### Algebraic data type (ADT)

An ADT is just a data type defined by one or more data constructors, each of which may contain zero or more arguments.
We say that the data type is the sum or union of its data constructors, and each data constructor is the product of its
arguments, hence the name algebraic data type.

### ~~Stream~~

`Stream` is deprecated. Use `LazyList` (which is fully lazy) instead of `Stream` (which has a lazy tail only)

### LazyList

`unfold` is a general stream-building function. It takes an initial state, and a function for producing both the next
state and the next value in the generated stream.

```
def unfold[A, S](init: S)(f: S => Option[(A, S)]): LazyList[A] = ???
```

The `unfold` function is an example of what's sometimes called a *corecursive* function. Whereas a recursive
function consumes data, a corecursive function produces data.

Corecursion is also sometimes called *guarded recursion*, and productivity is also sometimes called *cotermination*.

### Correction

#### 13.3.2 Trampolining: a general solution to stack overflow

[Mistake in `TailRec` REPL session](https://github.com/fpinscala/fpinscala/issues/308)

This code:
```
val g = List.fill(100000)(f).foldLeft(f) {
  (a, b) => x => Suspend(() => a(x).flatMap(b))
}
```

Should be:

```
val g = List.fill(100000)(f).foldLeft(f) {
  (a, b) => x => Suspend(() => ()).flatMap { _ => a(x).flatMap(b) }
```

## Scala With Cats

[Scala with Cats Book](https://underscore.io/books/scala-with-cats/)

### Cats 2.2.0

All Cats type class instances for standard library types are now available in implicit scope, and no longer have to be imported. See [Cats 2.2.0](https://github.com/typelevel/cats/releases/tag/v2.2.0)

### The implicitly Method

The Scala standard library provides a generic type class interface called `implicitly`. We can use `implicitly` to
summon any value from implicit scope:

```scala
def implicitly[A](implicit value: A): A = value
```

### The summon Method

The effect of calling summon[Foo] is that the compiler will look for a given definition of type Foo.
It will then call the summon method with that object, which returns the object right back.

```scala
def summon[T](using t: T): t
```

### Importing All The Things

```scala
import cats._ // imports all of Cats' type classes in one go;
import cats.implicits._ // imports all of the standard type class instances and all of the syntax in one go.
```

### Show

[`cats.Show`](https://typelevel.org/cats/api/cats/Show.html)

`Show` provides a mechanism for producint developer-friendly console output without using toString.

```scala
package cats

trait Show[A] {
  def show(value: A): String
}

object Show {
  // Convert a function to a `Show` instance:
  def show[A](f: A => String): Show[A] = ???

  // Create a `Show` instance from a `toString` method
  def fromToString[A]: Show[A] = ???
}
```

```scala
import cats.Show
import cats.syntax.show._ // for show
```

### Eq

[`cats.Eq`](https://typelevel.org/cats/api/cats/kernel/Eq.html)

`Eq` is designed to support type-safe equality and address annoyances using Scala's built-in == operator.

```scala
package cats

trait Eq[A] {
  def eqv(a: A, b: A): Boolean
  // other concrete methods based on eqv...
}
```

The interface syntax, defined in `cats.syntax.eq`, provides two methods for performing equality checks provided there is
an instance Eq[A] in scope:

* `===` compares two objects for equality;
* `=!=` compares two objects for inequality;

```scala
import cats.Eq
import cats.syntax.eq._ // for === =!=
```

We can define our own instances of `Eq` using the `Eq.instance[A]` method, which accepts a function of type
`(A, A) => Boolean` and returns an `Eq[A]`.

### Option

We can using the `Option.apply` and `Option.empty` methods from the standard library:

```scala
Option(1)
Option.empty[Int]
```

or using special syntax from `cats.syntax.option`:

```scala
import cats.syntax.option._

1.some
none[Int]
```

### Monoid

[`cats.Monoid`](https://typelevel.org/cats/api/cats/kernel/Monoid.html)

Formally, a monoid for a type `A` is:

* an operation `combine` (or `op` in fpinscala) with type `(A, A) => A`
* an element `empty` (or `zero` in fpinscala) of type `A`

In *Functional Programming in Scala*:  
A monoid is a type together with a binary operation (`op`) over that type, satisfying associativity and having an
identity element (`zero`)

Here is a simplified version of the definition from Cats:

```scala
trait Monoid[A] {
  def combine(x: A, y: A): A
  def empty: A
}
```

> *Monoid Laws*
>
> Monoids must formally obey several *laws*. For all values `x`, `y`, and `z`,
> in `A`, `combine` must be associative and `empty` must be an identity element:
>
> ```
> def associativeLaw[A](x: A, y: A, z: A)(implicit m: Monoid[A]): Boolean = {
>   m.combine(x, m.combine(y, z)) == m.combine(m.combine(x, y), z)
> }
>
> def identityLaw[A](x: A)(implicit m: Monoid[A]): Boolean = {
>   (m.combine(x, m.empty) == x) && (m.combine(m.emtpy, x) == x)
> }
> ```

Cats provides syntax for the `combine` method in the form of the `|+|` operator.

```scala
import cats.Monoid
import cats.syntax.semigroup._ // for |+|
```

### Semigroup

[`cats.Semigroup`](https://typelevel.org/cats/api/cats/kernel/Semigroup.html)

A semigroup is just the `combine` part of a monoid.  
A more accurate (though still simplified) definition of Cats' `Monoid` is:

```scala
trait Semigroup[A] {
  def combine(x: A, y: A): A
}

trait Monoid[A] extends Semigroup[A] {
  def empty: A
}
```

```scala
import cats.Semigroup
import cats.syntax.semigroup._ // for |+|
```

### Functor

[`cats.Functor`](http://typelevel.org/cats/api/cats/Functor.html)

Informally, a functor is anything with a `map` method.  
Here's a simplified version of the definition:

```scala
package cats
trait Functor[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
}
```

> *Functor Laws*
>
> Identity: call `map` with the identity function is the same as doing nothing:
> ```
> fa.map(a => a) == fa
> ```
> Composition: `mapping` with two functions `f` and `g` is the same as `mapping` with `f` and then `mapping` with `g`:
> ```
> fa.map(g(f(_))) == fa.map(f).map(g)
> ```

We obtain instances using the standard `Functor.apply` method on the companion object.

`Functor` provides the `lift` method, which converts a function of type `A => B` to one that operates over a functor and
has type `F[A] => F[B]`:

```scala
import cats.Functor
import cats.syntax.functor._ // for map
```

### Contravariant

[`cats.Contravariant`](http://typelevel.org/cats/api/cats/Contravariant.html)

The *contravariant functor* provides an operation called `contramap` that
represents "prepending" and operation to a chain.

```scala
trait Contravariant[F[_]] {
  def contramap[A, B](fa: F[A])(f: B => A): F[B]
}
```

We can summon instances of `Contravariant` using the `Contravariant.apply` method.

```scala
import cats.Contravariant
import cats.syntax.contravariant._ // for contramap
```

### Invariant

[`cats.Invariant`](https://typelevel.org/cats/api/cats/Invariant.html)

*Invariant functors* implement a method called `imap` that is informally equivalent to a combination of `map` and
`contramap`. If `map` generates new type class instances by appending a function to a chain, and `contramap` generates
them by prepending an operation to a chain, `imap` generates them via a pair of bidirectional transformations.

```scala
trait Invariant[F[_]] {
  def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
}
```

```scala
import cats.Invariant
import cats.syntax.invariant._ // for imap
```

Cats provides an instance of `Invariant` for `Monoid`.  
Imagine we want to produce a `Monoid` for Scala's `Symbol` type:

```scala
import cats.Monoid
import cats.instances.string._ // for Monoid
import cats.syntax.invariant._ // for imap

implicit val symbolMonoid: Monoid[Symbol] = Monoid[String].imap(Symbol.apply)(_.name)
```

### Monad

[`cats.Monad`](https://typelevel.org/cats/api/cats/Monad.html)

> Informally, a monad is anything with a constructor and a `flatMap` method.

> A monad is a mechanism for sequencing computations.

> A monad is an implementation of one of the minimal sets of monadic combinators, satisfying the laws of associativity
> and identity.

Here is a simplified version of the `Monad` type class in Cats:

```scala
trait Monad[F[_]] {
  def pure[A](value: A): F[A] // fpinscala‘s `unit`

  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
}
```
> *Monad Laws*
>
> *Left identity*: calling `pure` and transforming the result with `func` is the same as calling `func`:
> ```
> pure(a).flatMap(func) == func(a)
> ```
> *Right identity*: passing `pure` to `flatMap` is the same as doing nothing:
> ```
> m.flatMap(pure) == m
> ```
> *Associativity*: `flatMapping` over two functions `f` and `g` is the same as `flatMapping` over `f` and then
`flatMapping` over `g`:
> ```
> m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))
> ```

Every monad is also a functor:

```
def map[A, B](value: F[A])(func: A => B): F[B] = flatMap(value)(a => pure(func(a)))
```

`Monad` extends two other type classes: `FlatMap`, which provides the `flatMap` method, and `Applicative`, which
provides `pure`. `Applicative` also extends `Functor`.

```scala
import cats.Monad
import cats.syntax.flatMap._ // for flatMap
import cats.syntax.functor._ // for map
import cats.syntax.applicative._ // for pure
```

We can define a `Monad` for a custom type by providing implementations of three methods: `flatMap`, `pure` and a method
we haven't seen yet called `tailRecM`. Here is an implementation of `Monad` for `Option` as an example:

```scala
import cats.Monad
import scala.annotation.tailrec

val optionMonad = new Monad[Option] {
  def flatMap[A, B](opt: Option[A])(fn: A => Option[B]): Option[B] = opt flatMap fn

  def pure[A](opt: A): Option[A] = Option(opt)

  @tailrec
  def tailRecM[A, B](a: A)(fn: A => Option[Either[A, B]]): Option[B] = fn(a) match {
    case None => None
    case Some(Left(a1)) => tailRecM(a1)(fn)
    case Some(Right(b)) => Some(b)
  }
}
```

The `tailRecM` method is an optimisation used in Cats to limit the amount of stack space consumed by nested calls to
`flatMap`. The method should recursively call itself until the result of `fn` returns a `Right`.

If we can make `tailRecM` tail-recursive, Cats is able to guarantee stack safety in recursive situations such as folding
over large lists.

In some libraries and languages, notably Scalaz and Haskell, `pure` is referred to as `point` or `return` and `flatMap`
is referred to as `bind` or `>>=`. This is purely a difference in terminology.


### Id

`cats.Id`

Here is the definition of `Id` to explain:

```scala
type Id[A] = A
```

Cats provides instances of various type classes for `Id`, including `Functor` and `Monad`.

### Either

```scala
import cats.syntax.either._ // for asLeft asRight
```

`cats.syntax.either` adds some useful extension methods to the `Either` companion object. The `catchOnly` and
`catchNonFatal` methods are great for capturing `Exceptions` as instances of `Either`:

```
Either.catchOnly[NumberFormatException]("foo".toInt)
Either.catchNonFatal(sys.error("Badness"))
```

There are also methods for creating an `Either` from other data types:

```
Either.fromTry(scala.util.Try("foo".toInt))
Either.fromOption[String, Int](None, "Badness") // res3: Either[String, Int] = Left(Badness)
```

We can use `orElse` and `getOrElse` to extract values from the right side or return a default:

```
"Error".asLeft[Int].getOrElse(0)
"Error".asLeft[Int].orElse(2.asRight[String])
```

The `ensure` method allows us to check whether the right-hand value satisfies a predicate:

```
-1.asRight[String].ensure("Must be non-negative!")(_ > 0)
```

The `recover` and `recoverWith` methods provide similar error handling to their namesakes on `Future`:

```
"error".asLeft[Int].recover { case str: String => -1 }
"error".asLeft[Int].recoverWith { case str: String => Right(-1) }
```

There are `leftMap` and `bimap` methods to complement `map`:

```
"foo".asLeft[Int].leftMap(_.reverse)
6.asRight[String].bimap(_.reverse, _ * 7)
"bar".asLeft[Int].bimap(_.reverse, _ * 7)
```

The `swap` method lets us exchange left for right:
```
123.asRight[String].swap
```

Finally, Cats adds a host of conversion methods: `toOption`, `toList`, `toTry`, `toValidated`, and so on.

### MonadError

[`cats.MonadError`](https://typelevel.org/cats/api/cats/MonadError.html)

Cats provides an additional type class called `MonadError` that abstracts over `Either`-like data types that are used
for error handling. `MonadError` provides extra operations for raising and handling errors.

Here is a simplified version of the definition of `MonadError`:
```
trait MonadError[F[_], E] extends Monad[F] {
  // Lift an error into the `F` context:
  def raiseError[A](e: E): F[A]
  
  // Handle an error, potentially recovering from it:
  def handleErrorWith[A](fa: F[A])(f: E => F[A]): F[A]

  // Handle all errors, recovering from them:
  def handleError[A](fa: F[A])(f: E => A): F[A]

  // Test an instance of `F`,
  // failing if the predicate is not satisfied:
  def ensure[A](fa: F[A])(e: E)(f: A => Boolean): F[A]
}
```

In reality, `MonadError` extends another type class called `ApplicativeError`.

`MonadError` is defined in terms of two type parameters:

* `F` is the type of the monad;
* `E` is the type of error contained within `F`.

Here's an example where we instantiate the type class for `Either`:

```
import cats.MonadError
import cats.instances.either._ // for MonadError

type ErrorOr[A] = Either[String, A]

val monadError = MonadError[ErrorOr, String]
```

```scala
import cats.syntax.applicative._ // for pure
import cats.syntax.applicativeError._ // for raiseError handleError
import cats.syntax.monadError._ // for ensure
```

Cats provides instances of `MonadError` for numerous data types including `Either`, `Future`, and `Try`. The instance
for `Either` is customisable to any error type, whereas the instances for `Future` and `Try` always represent errors as
`Throwables`.

### Eval

[`cats.Eval`](https://typelevel.org/cats/api/cats/Eval.html)

`cats.Eval` is a monad that allows us to abstract over different models of evaluation.
`Eval` has three subtypes: `Now`, `Later`, and `Always`.

We can extract the result of an `Eval` using its `value` method:
```scala
import cats.Eval
val now = Eval.now(math.random + 1000)
val later = Eval.later(math.random + 2000)
val always = Eval.always(math.random + 3000)

now.value
later.value
always.value
```

The three behaviours are summarized below:

| Scala | Cats | Properties |
| :--- | :--- | :--- |
| `val` | `Now` | eager, memoized |
| `lazy val` | `Later` | lazy, memoized |
| `def` | `Always` | lazy, not memoized |

`Eval's` `map` and `flatMap` methods add computations to a chain. In this case, however, the chain is stored explicitly
as a list of functions. The functions are't run util we call `Eval's` `value` method to request a result.

While the semantics of the originating `Eval` instances are maintained, mapping functions are always called lazily on
demand (`def` semantics).

`Eval` has a `memoize` method that allows us to memoize a chain of computations.

`Eval.defer` takes an existing instance of `Eval` and defers its evaluation. The `defer` method is trampolined like
`map` and `flatMap`:

```scala
import cats.Eval

def factorial(n: BigInt): Eval[BigInt] =
    if (n == 1) {
      Eval.now(n)
    } else {
      Eval.defer(factorial(n - 1).map(_ * n))
    }
```

### Writer

[`cats.data.Writer`](https://typelevel.org/cats/api/cats/data/package$$Writer$.html)

`cats.data.Writer` is a monad that lets us carry a log along with a computation.

A `Writer[W, A]` carries two values: a *log* of type `W` and a *result* of type `A`. We can create a `Writer` from
values of each type as follows:

```scala
import cats.data.Writer
import cats.instances.vector._ // for Monoid

Writer(Vector("It was the best of times", "it was the worst of times"), 1859)
```
Cats implements `Writer` in terms of another type, `WriterT` (a monad transformer). We can read types like
`WriterT[Id, W, A]` as `Writer[W, A]`:

```
type Writer[W, A] = WriterT[Id, W, A]
```
Cats provides a way of creating `Writers` specifying only the log or the result. If we only have a result we can use the
standard `pure` syntax. To do this we must have a `Monoid[W]` in scope so Cats knows how to produce an empty log:

```
import cats.instances.vector._ // for Monoid
import cats.syntax.applicative._ // for pure

type Logged[A] = Writer[Vector[String], A]

123.pure[Logged]
```

If we have a log and no result we can create a `Writer[Unit]` using the `tell` syntax from `cats.syntax.writer`:

```scala
import cats.syntax.writer._ // for tell

Vector("msg1", "msg2", "msg3").tell
```

If we have both a result and a log, we can either use `Writer.apply` or we can use the `writer` syntax from
`cats.syntax.writer`:

```
import cats.syntax.writer._ // for writer

val a = Writer(Vector("msg1", "msg2", "msg3"), 123)

val b = 123.writer(Vector("msg1", "msg2", "msg3"))
```

We can extract the result and log from a `Writer` using the `value` and `written` methods respectively:

```
val aResult: Int = a.value
val aLog: Vector[String] = a.written
```

We can extract both values at the same time using the `run` method:

```
val (log, result) = b.run
```

The log in a `Writer` is preserved when we `map` of `flatMap` over it.
`flatMap` appends the logs from the source `Writer` and the result of the user's sequencing function. For this reason
it's good practice to use a log type that has an efficient append and concatenate operations, such as a Vector:

```
val writer1 = for {
  a <- 10.pure[Logged]
  _ <- Vector("a", "b", "c").tell
  b <- 32.writer(Vector("x", "y", "z"))
} yield a + b
```
We can transform the log in a `Writer` with the `mapWritten` method:

```
val writer2 = writer1.mapWritten(_.map(_.toUpperCase))
```

We can transform both log and result simultaneously using `bimap` or `mapBoth`. `bimap` takes two function parameters,
one for the log and one for the result. `mapBoth` takes a single function that accepts two parameters:

```
val writer3 = writer1.bimap(log => log.map(_.toUpperCase), res => res * 100) // WriterT[Id, Vector[String], Int]

val writer4 = writer1.mapBoth { (log, res) =>
  val log2 = log.map(_ + "!")
  val res2 = res * 1000
  (log2, res2)
} // cats.Id[(Vector[String], Int)]
```

Finally, we can clear the log with the `reset` method and swap log and result with the `swap` method:

### Reader

[`cats.data.Reader`](https://typelevel.org/cats/api/cats/data/package$$Reader$.html)

`cats.data.Reader` is a monad that allows us to sequence operations that depend on some input. Instances of `Reader`
wrap up functions of one argument, providing us with useful methods for composing them.

`Reader` is implemented in terms of another type called `Kleisli`.

We can create a `Reader[A, B]` from a function `A => B` using the `Reader.apply` constructor, and we can extract the
function again using the `Reader's` `run` method and call it using `apply` as usual:

```scala
import cats.data.Reader

case class Cat(name: String, favoriteFood: String)
// defined class Cat

val catName: Reader[Cat, String] = Reader(cat => cat.name)

catName.run(Cat("Garfield", "lasagne"))
```

`Readers` are most useful in situations where:

* we are constructing a batch program that can easily be represented by a function;
* we need to defer injection of a known parameter or set of parameters;
* we want to be able to test parts of the program in isolation.

### State

[`cats.data.State`](https://typelevel.org/cats/api/cats/data/package$$State$.html)

`cats.data.State` allows us to pass additional state around as part of a computation.

Instances of `State[S, A]` represent functions of type `S => (S, A)`. `S` is the type of the state and `A` is the type
of the result.

```scala
import cats.data.State

val a = State[Int, String] { state => (state, s"The state is $state") }
```

An instance of `State` is a function that does two things:

* transforms an input state to an output state;
* computes a result.

`State` provides three methods--`run`, `runS`, and `runA`--that return different combinations of state and result. Each
method returns an instance of `Eval`, which `State` uses to maintain stack safety. We call the `value` method as usual
to extract the actual result:

Cats provides several convenience constructors for creating primitive steps:

* `get` extracts the state as the result;
* `set` updates the state and returns unit as the result;
* `pure` ignores the state and returns a supplied result;
* `inspect` extracts the state via a transformation function;
* `modify` updates the state using an update function.

### Monad Transformers

Cats defines transformers for a variety of monads, each providing the extra knowledage we need to compose that monad
with others, each named with a `T` suffix:

* `cats.data.OptionT` for `Option`;
* `cats.data.EitherT` for `Either`;
* `cats.data.ReaderT` for `Reader`;
* `cats.data.WriterT` for `Writer`;
* `cats.data.StateT` for `State`;
* `cats.data.IdT` for `Id`.

Here's an example that uses `OptionT` to compose `List` and `Option`:

```scala
import cats.data.OptionT

type ListOption[A] = OptionT[List, A]

import cats.Monad
import cats.instances.list._ // for Monad
import cats.syntax.applicative._ // for pure

val result1: ListOption[Int] = OptionT(List(Option(10)))
val result2: ListOption[Int] = 32.pure[ListOption]

result1.flatMap { (x: Int) =>
  result2.map { (y: Int) =>
    x + y
  }
} // OptionT(List(Some(42))

```

Each monad transformer is a data type, defined in `cats.data`, that allows us to wrap stacks of monads to produce new
monads.

In fact, many monads in Cats are defined by combining a monad transformer with the `Id` monad.

```
type Reader[E, A] = ReaderT[Id, E, A] // = Kleisli[Id, E, A]
type Writer[W, A] = WriterT[Id, W, A]
type State[S, A] = StateT[Id, S, A]
```

We can create transformed monad stacks using the relevant monad transformer's `apply` method or the usual `pure` syntax.

```
val errorStack1 = OptionT[ErrorOr, Int](Right(Some(10)))

val errorStack2 = 32.pure[ErrorOrOption]
```

We can unpack it using its `value` method. This returns the untransformed stack. We can then manipulate the individual
monads in the usual way:

```
errorStack1.value // ErrorOr[Option[Int]]

errorStack2.value.map(_.getOrElse(-1)) // Either[String, Int]
```

Each call to `value` unpacks a *single* monad transformer. We may need more than one call to completely unpack a large
stack.

### [Kleisli](https://typelevel.org/cats/datatypes/kleisli.html)

[`cats.data.Kleisli`](https://typelevel.org/cats/api/cats/data/Kleisli.html)

At its core, `Kleisli[F[_], A, B]` is just a wrapper around the function `A => F[B]`. Depending on the properties of the
`F[_]`, we can do different things with `Kleisli`s . For instance, if `F[_]` has a `FlatMap[F]` instance (we can call
`flatMap` on `F[A]` values), we can compose two `Kleisli`s much like we can two functions.

Below are some more methods on `Kleisli` that can be used so long as the constraint on `F[_]` is satisfied.

| Method | Constraint on `F[_]` |
| :--- | :--- |
| andThen | FlatMap |
| compose | FlatMap |
| flatMap | FlatMap |
| lower | Monad |
| map | Functor |
| traverse | Applicative |

### Semigroupal

[`cats.Semigroupal`](https://typelevel.org/cats/api/cats/Semigroupal.html)

`cats.Semigroupal` is a type class that allows us to combine contexts. Cats provides a `cats.syntax.apply` module that
makes use of `Semigroupal` and `Functor` to allow users to sequence functions with multiple arguments.

```scala
trait Semigroupal[F[_]] {
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]
}
```

The parameters `fa` and `fb` are independent of one another. This is contrast to `flatMap`, which imposes a strict order
on its parameters.

The companion object for `Semigroupal` defines a set of methods on top of `product`. For example, the methods `tuple2`
through `tuple22` generalise `product` to different arities.

The methods `map2` through `map22` apply a user-specified function to the values inside 2 to 22 contexts.

There are also methods `contramap2` through `contramap22` and `imap2` through `imap22`, that require instances of
`Contravariant` and `Invariant` respectively.

> *Semigroupal Laws*
>
> There is only one law for `Semigroupal`: the `product` method must be associative.
> ```
> product(a, product(b, c)) == product(product(a, b), c)
> ```

```scala
import cats.syntax.apply._ // for tupled and mapN
```

We can use the same trick on tuples of up to 22 values. Cats defines a separate `tupled` method for each arity:

```
(Option(123), Option("abc"), Option(true)).tupled
```

Cats' apply syntax provides a method called `mapN` that accepts an implicit `Functor` and a function of the correct
arity to combine the values:

```
case class Cat(name: String, born: Int, color: String)

(
  Option("Garfield"),
  Option(1978),
  Option("orange & black")
).mapN(Cat.apply) // Option[Cat]
```

Apply syntax also has `contramapN` and `imapN` methods that accept `Contravariant` and `Invariant` functors. For
example, we can combine `Monoids` using `Invariant`. Here's an example:
```scala
import cats.Monoid
import cats.instances.int._ // for Monoid
import cats.instances.invariant._ // for Semigroupal
import cats.instances.list._ // for Monoid
import cats.instances.string._ // for Monoid
import cats.syntax.apply._ // for imapN

case class Cat(name: String, yearOfBirth: Int, favoriteFoods: List[String])

val tupleToCat: (String, Int, List[String]) => Cat = Cat.apply _

val catToTuple: Cat => (String, Int, List[String]) = cat => (cat.name, cat.yearOfBirth, cat.favoriteFoods)

implicit val catMonoid: Monoid[Cat] = (
  Monoid[String],
  Monoid[Int],
  Monoid[List[String]]
).imapN(tupleToCat)(catToTuple) // wtf ...

```

### Parallel

[`cats.Parallel`](https://typelevel.org/cats/typeclasses/parallel.html)

`Parallel` allows us to take a type that has a monad instance and convert it to some related type that instead has an 
applicative (or semigroupal) instance. This related type will have some useful alternate semantics.

```
trait Parallel[M[_]] {
  type F[_]

  def applicative: Applicative[F]
  def monad: Monad[M]
  def parallel: ~>[M, F] // ~> is a type alias for `FunctionK` and is what performs the conversion from `M` to `F`
}
```

```scala
import cats.syntax.parallel._ // for parTupled

type ErrorOr[A] = Either[Vector[String], A]
val error1: ErrorOr[Int] = Left(Vector("Error  1"))
val error2: ErrorOr[Int] = Left(Vector("Error  2"))

(error1, error2).parTupled // Left(Vector("Error 1", "Error 2"))
```

There are many syntax methods provided by `Parallel` for methods on `Semigroupal` and related types, but the most 
commonly used is `parMapN`. Here's an example of `parMapN` in an error handling situation.

```
val success1: ErrorOr[Int] = Right(1)
val success2: ErrorOr[Int] = Right(2)
val addTwo = (x: Int, y: Int) => x + y

(error1, error2).parMapN(addTwo) // Left(Vector("Error 1", "Error 2"))
(success1, success2).parMapN(addTwo) // Right(3)
```

### Apply

[`cats.Apply`](https://typelevel.org/cats/api/cats/Apply.html)

`cats.Apply` extends `Semigroupal` and `Functor` and adds an `ap` method that applies a parameter to a function within a
context. 

```
trait Apply[F[_]] extends Semigroupal[F] with Functor[F] {
  def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
  
  def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] = ap(map(fa)(a => (b: B) => (a, b)))(fb)
}
```


### Applicative

[`cats.Applicative`](https://typelevel.org/cats/api/cats/Applicative.html)

`Applicative` extends `Apply`, the source of the `pure` method.

``` 
trait Applicative[F[_]] extends Apply[F] {
  def pure[A](a: A): F[A]
}
```

`Applicative` is related to `Apply` as `Monoid` is related to `Semigroup`.

### Validated

Cat's provides a data type called `Validated` that has an instance of `Semigroupal` but no instance of `Monad`. The
implementation of `product` is therefore free to accumulate errors:

```scala
import cats.Semigroupal
import cats.data.Validated
import cats.instances.list._ // for Monoid

type AllErrorsOr[A] = Validated[List[String], A]

Semigroupal[AllErrorsOr].product(
  Validated.invalid(List("Error 1")),
  Validated.invalid(List("Error 2"))
) // AllErrorsOr[(Nothing, Nothing)] = Invalid(List(Error 1, Error 2))
```

`Validated` has two subtypes, `Validated.Valid` and `Validated.Invalid`, that correspond loosely to `Right` and `Left`.
There are a lot of ways to create instances of these types. We can create them directly using their apply methods:

```
val v = Validated.Valid(123)
val i = Validated.Invalid(List("Badness"))
```

However, it is often easier to use the `valid` and `invalid` smart constructors, which widen the return type to
`Validated`:

```
val v = Validated.valid[List[String], Int](123)
val i = Validated.invalid[List[String], Int](List("Badness"))
```

As a third option we can import the `valid` and `invalid` extension methods from `cats.syntax.validated`:

```scala
import cats.syntax.validated._

123.valid[List[String]]
List("Badness").invalid[Int]
```

As a fourth option we can use `pure` and `raiseError` from `cats.syntax.applicative` and `cats.syntax.applicativeError`
respectively:

```
import cats.syntax.applicative._ // for pure
import cats.syntax.applicativeError._ // for raiseError

type ErrorsOr[A] = Validated[List[String], A]

123.pure[ErrorsOr]

List("Badness").raiseError[ErrorsOr, Int]
```
Finally, there are helper methods to create instances of `Validated` from different sources. We can create them from
`Exceptions`, as well as instances of `Try`, `Either`, and `Option`:

```
Validated.catchOnly[NumberFormatException]("foo".toInt)

Validated catchNonFatal(sys.error("Badness"))

Validated.fromTry(scala.util.Try("foo".toInt))

Validated.fromEither[String, Int](Left("Badness"))

Validated.fromOption[String, Int](None, "Badness")
```

We can combine instances of `Validated` using any of the methods or syntax described for `Semigroupal`.

All of these techniques require an instance of `Semigroupal` to be in scope. As with `Either`, we need to fix the error
type to create a type constructor with the correct number of parameters for `Semigroupal`:

```
type AllErrorsOr[A] = Validated[String, A]
```

`Validated` accumulates errors using a `Semigroup`, so we need one of those in scope to summon the `Semigroupal`.  
Once we import a `Semigroup` for the error type, everything works as expected:

```
import cats.instances.string._ // for Semigroup

Semigroupal[AllErrorsOr]
```

```
import cats.syntax.apply._ // for tupled

(
  "Error 1".invalid[Int],
  "Error 2".invalid[Int],
).tupled

import cats.instances.vector._ // for Semigroupal
(
  Vector(404).invalid[Int],
  Vector(500).invalid[Int]
).tupled
```

The `cats.data` package also provides the `NonEmptyList` and `NonEmptyVector` types that prevent us failing without at
least one error:

```
import cats.data.NonEmptyVector

(
  NonEmptyVector.of("Error 1").invalid[Int],
  NonEmptyVector.of("Error 2").invalid[Int]
).tupled
```

`Validated` comes with a suite of methods that closely resemble those available for `Either`, including the methods from
`cats.syntax.either`. We can use `map`, `leftMap`, and `bimap` to transform the values inside the valid and invalid
sides:

```
123.valid.map(_ * 100)
"?".invalid.leftMap(_.toString)
123.valid[String].bimap(_ + "!", _ * 100)
"?".invalid[Int].bimap(_ + "!, _ * 100)
```

We can't `flatMap` because `Validated` isn't a monad. However, Cats does provide a stand-in for `flatMap` called
`andThen`. The type signature of `andThen` is identical to that of `flatMap`, but it has different name because it is
not a lawful implementation with respect to the monad laws:

```
32.valid.andThen { a =>
  10.valid.map { b =>
    a + b
  }
}
```

If we want to do more than just `flatMap`, we can convert back and forth between `Validated` and `Either` using the
`toEither` and `toValidated` methods. Note that `toValidated` comes from [`cats.syntax.either`]:

```
import cats.syntax.either._ // for toValidated

"Badness".invalid[Int].toEither
"Badness".invalid[Int].toEither.toValidated
```

As with `Either`, we can use the `ensure` method to fail with a specified error if a predicate dose not hold:

```
123.valid[String].ensure("Negative!")(_ > 0)
```

Finally, we can call `getOrElse` or `fold` to extract values from the `Valid` and `Invalid` cases:

```
"fail".invalid[Int].getOrElse(0)
"fail".invalid[Int].fold(_ + "!!!", _.toString)
```

### Foldable

[`cats.Foldable`](https://typelevel.org/cats/api/cats/Foldable.html)

`Foldable` abstracts the familiar `foldLeft` and `foldRight` operations.

```scala
import cats.Foldable
import cats.instances.list._ //for Foldable

val ints = List(1, 2, 3)

Foldable[List].foldLeft(ints, 0)(_ + _)
```

`Foldable` defines `foldRight` differently to `foldLeft`, in terms of the `Eval` monad:

```
def foldRight[A, B](fa: F[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B]
```

Using `Eval` means folding is always stack safe.

`Foldable` provides us with a host of useful methods defined on top of `foldLeft`. Many of these are facsimiles of
familiar methods from the standard library: `find`, `exists`, `forall`, `toList`, `isEmpty`, `nonEmpty`, and so on:

```
Foldable[Option].nonEmpty(Option(42))
Foldable[List].find(List(1, 2, 3))(_ % 2 == 0)
```

Cats provides two methods that make use of `Monoids`:

* `combineAll` (add its alias `fold`) combines all elements in the sequence using their `Monoid`;
* `foldMap` maps a user-supplied function over the sequence and combines the result using a `Monoid`.

```
import cats.instances.int._ // for Monoid

Foldable[List].combineAll(List(1, 2, 3))

import cats.instances.string._ // for Monoid

Foldable[List].foldMap(List(1, 2, 3))(_.toString)
```

Finally, we can compose `Foldables` to support deep traversal of nested sequences:

```
import cats.instances.vector._ // for Monoid

val ints = List(Vector(1, 2, 3), Vector(4, 5, 6))

(Foldable[List] compose Foldable[Vector]).combineAll(ints) // 21
```

Syntax:

```
import cats.syntax.foldable._ // for combineAll and foldMap

List(1, 2, 3).combineAll

List(1, 2, 3).foldMap(_.toString)
```
### Traverse

[`cats.Traverse`](https://typelevel.org/cats/api/cats/Traverse.html)

`Traverse` is a higher-level abstraction that uses `Applicatives` to iterate with less pain than folding.

```
trait Traverse[F[_]] {
  def traverse[G[_]: Applicative, A, B](inputs: F[A])(func: A => G[B]): G[F[B]]

  def sequence[G[_]: Applicative, B](inputs: F[G[B]]): G[F[B]] = traverse(inputs)(identity)
```

We can summon instances as usual using `Traverse.apply` and use the `traverse` and `sequence` methods.

```
import cats.syntax.traverse._ // for sequence and traverse
```

## Cats Effect

[Cats Effect](https://typelevel.org/cats-effect/)

### Ref

[`cats.effect.Ref`](https://typelevel.org/cats-effect/docs/std/ref)

A Ref instance wraps some given data and implements methods to manipulate that data in a safe manner. 
When some fiber is running one of those methods, any other call to any method of the Ref instance will be blocked.

### Deferred

[`cats.effect.Deferred`](https://typelevel.org/cats-effect/docs/std/deferred)

A deferred instances are created empty, and can be filled only once. 
If some fiber tries to read the element from an empty Deferred then it will be semantically blocked 
until some other fills (completes) it.

## Monocle

[Monocle](https://www.optics.dev/Monocle/) Optics library for Scala

## Kind Projector

If you frequently find yourself defining multiple type aliases when building monad stacks, you may want to try the
[Kind Projector](https://github.com/typelevel/kind-projector) compiler plugin. Kind Projector enhances Scala's type
syntax to make it easier to define partially applied type constructors.

## ScalaCheck

[ScalaCheck](https://github.com/typelevel/scalacheck)

See *Functional Programming in Scala* chapter 8 ***Property-based testing***

```scala
import org.scalacheck.Gen
val intList = Gen.listOf(Gen.choose(0, 100))

import org.scalacheck.Prop._ // forAll
val prop = forAll(intList)(ns => ns.reverse.reverse == ns) &&
  forAll(intList)(ns => ns.headOption == ns.reverse.lastOption)
val failingProp = forAll(intList)(ns => ns.reverse == ns)

prop.check
failingProp.check
```

## Proof Assistant

Coq

Agda
