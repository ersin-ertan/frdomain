package frdomain.ch3.algebra

import arrow.core.Option
import arrow.core.Tuple3
import arrow.data.Try
import arrow.data.ev
import arrow.data.monad
import arrow.typeclasses.binding
import java.util.*

interface AccountService<Account, Amount, Balance> {
    fun open(no: String, name: String, openingDate: Option<Date>): Try<Account>
    fun close(account: Account, closeDate: Option<Date>): Try<Account>
    fun debit(account: Account, amount: Amount): Try<Account>
    fun credit(account: Account, amount: Amount): Try<Account>
    fun balance(account: Account): Try<Balance>

    fun transfer(from: Account, to: Account, amount: Amount): Try<Tuple3<Account, Account, Amount>> =
            Try.monad().binding {
                val a = debit(from, amount).bind()
                val b = credit(to, amount).bind()
                yields(Tuple3(a, b, amount))
            }.ev()
}