package frdomain.ch3.repository

import arrow.core.Option
import arrow.data.Try

interface Repository<A, IdType> {
    fun query(id: IdType): Try<Option<A>>
    fun store(a: A): Try<A>
}