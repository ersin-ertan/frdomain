package frdomain.ch3.repository.reader


class Reader<R, A>(val run: (R) -> A) {

    fun <B> map(f: (A) -> B): Reader<R, B> = Reader({ r -> f(run(r)) })
    fun <B> flatMap(f: (A) -> Reader<R, B>): Reader<R, B> = Reader({ r -> f(run(r)).run(r) })
}