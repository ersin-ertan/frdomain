package frdomain.ch3.algebra.interpreter

import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.data.Failure
import arrow.data.Success
import arrow.data.Try
import frdomain.ch3.algebra.AccountService
import frdomain.ch3.algebra.interpreter.common.today
import java.util.*

object AccountService : AccountService<Account, Amount, Balance> {
    override fun open(no: String, name: String, openingDate: Option<Date>): Try<Account> =
            if (no.isEmpty() || name.isEmpty()) Failure<Account>(Exception("Account no or name cannot be blank"))
            else if (openingDate.getOrElse { today }.before(today)) Failure<Account>(Exception("Cannot open account in the past"))
            else Success(Account(no, name, openingDate.getOrElse { today }))


    override fun close(account: Account, closeDate: Option<Date>): Try<Account> = with(closeDate.getOrElse { today }) {
        if (this.before(account.dateOfOpening))
            Failure<Account>(Exception("Close date $this cannot be before opening date ${account.dateOfOpening}"))
        else Success(account.copy(dateOfClosing = Some(this)))

    }

    override fun debit(account: Account, amount: Amount): Try<Account> =
            if (account.balance.amount < amount) Failure<Account>(Exception("Insufficient balance"))
            else Success(account.copy(balance = Balance(account.balance.amount - amount)))


    override fun credit(account: Account, amount: Amount): Try<Account> =
            Success(account.copy(balance = Balance(account.balance.amount - amount)))

    override fun balance(account: Account): Try<Balance> = Success(account.balance)

}