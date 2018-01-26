package frdomain.ch3.repository.partial

// Todo Will use Arrows implementation

//object Syntax {
//
//    implicit
//    class FunctorSyntax<F<_>: Functor, A>(val a: F<A>) {
//        fun <B> map(f: (A) -> B) = Functor<F>.map(a)(f)
//    }
//
//    implicit
//    class Function1FunctorSyntax<A1, A>(a: Function1<A1, A>) {
//        fun <B> map(f: (A) -> B) = Functor < (
//                { type f < x > = Function1<A1, x> })#f>.map(a)(f)
//    }
//
//    implicit
//    class MonadSyntax<M<_>: Monad, A>(a: M<A>)
//    {
//        fun <A> unit(a: -> A) = Monad<M>.unit(a)
//
//        fun <B> flatMap(f: (A) -> M<B>) = Monad<M>.flatMap(a)(f)
//    }
//
//    implicit
//    class Function1MonadSyntax<A1, A>(a: Function1<A1, A>) {
//        fun <A> unit(a: -> A) = Monad < ({ type f < x > = Function1<A1, x> })#f>.unit(a)
//
//        fun <B> flatMap(f: (A) -> A1 -> B) = Monad<(
//        { type f < x > = Function1<A1, x> })#f>.flatMap(a)(f)
//    }
//}
