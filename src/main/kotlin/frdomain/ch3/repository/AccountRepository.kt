package frdomain.ch3.repository

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.data.Failure
import arrow.data.Success
import arrow.data.Try
import java.util.*

interface AccountRepository : Repository<Account, String> {

    override fun query(id: String): Try<Option<Account>>

    override fun store(a: Account): Try<Account>

    fun balance(no: String): Try<Balance> = with(query(no)) {
        when (this) {
            is Success -> when (value) {
                is Some -> Success((value as Some).t.balance)
                is None -> Failure<Balance>(Exception("no account exists with no $no"))
            }
            is Failure -> Failure<Balance>(exception)
        }
    }


    fun query(openedOn: Date): Try<Sequence<Account>>
}

interface AccountRepositoryInMemory : AccountRepository {

    // leave out lazy
    val repo get() = mutableMapOf<String, Account>()


    override fun query(id: String): Try<Option<Account>> = Success(Option.fromNullable(repo[id]))

    override fun store(a: Account): Try<Account> {
        val r = repo.plus(a.no to a)
        return Success(a)
    }

    override fun query(openedOn: Date): Try<Sequence<Account>> = Success(repo.values.filter { it.dateOfOpening == openedOn }.asSequence())
}

object AccountRepositoryInMemoryO : AccountRepositoryInMemory