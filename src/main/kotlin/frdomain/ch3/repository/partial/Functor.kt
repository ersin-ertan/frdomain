package frdomain.ch3.repository.partial

// Todo Will use Arrows implementation

//interface Functor<F<_>> {
//
//    fun <A, B>map(a: F<A>)(f: A -> B): F<B>
//}
//
//object Functor {
//    fun apply<F<_>: Functor>: Functor<F> =
//    implicitly<Functor<F>>
//
//    inline fun ListFunctor: Functor<List> = Functor<List> {
//        fun <A,B>map(a: List<A>)(f: A -> B): List<B> = a map f
//    }
//
//    implicit fun OptionFunctor: Functor<Option> = new Functor<Option> {
//        fun map<A, B>(a: Option<A>)(f: A -> B): Option<B> = a map f
//    }
//
//    implicit fun Tuple2Functor<A1>: Functor<({type f<x> = (A1, x)})#f> = new Functor<({type f<x> = (A1, x)})#f> {
//        fun map<A, B>(a: (A1, A))(f: A -> B): (A1, B) = (a._1, f(a._2))
//    }
//
//    implicit fun Function1Functor<A1>: Functor<({type f<x> = Function1<A1, x>})#f> = new Functor<({type f<x> = Function1<A1, x>})#f> {
//        fun map<A, B>(fa: A1 -> A)(f: A -> B) = fa andThen f
//    }
//
//}
//
//
//    typealias Tup<A> = Pair<String, A>
//
//object FunctorTest {
//
//    val x = List(1,2,3,4)
//    val f: Int -> Int = _ + 1
//
//    Functor<List>.map(x)(f) // List(2,3,4,5)
//
//    val l = List(("a", 10), ("b", 20))
//    Functor<List>.map(l)(t -> Functor<({type f<x> = (String, x)})#f>.map(t)(f)) // List<(String, Int)> = List((a,11), (b,21))
//
//    // import Syntax._
//
//    List(1,2,3) map f // List(2,3,4)
//    l.map(e -> Functor<({type f<x> = (String, x)})#f>.map(e)(f))  // List<(String, Int)> = List((a,11), (b,21))
//
//    l.map(e -> Functor<Tup>.map(e)(f)) // List<Tup<Int>> = List((a,11), (b,21))
//}