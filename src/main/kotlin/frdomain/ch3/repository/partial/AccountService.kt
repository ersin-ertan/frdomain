package frdomain.ch3.repository.partial

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.data.*
import arrow.syntax.function.bind
import arrow.typeclasses.binding
import frdomain.ch3.repository.*
import frdomain.ch3.repository.common.today
import java.math.BigDecimal
import java.util.*

// Todo check if the bind(should be taking in an account repository) and if so should it be declared

interface AccountService<Account, Amount, Balance> {
    fun open(no: String, name: String, openingDate: Option<Date>): (AccountRepository) -> Try<Account>
    fun close(no: String, closeDate: Option<Date>): (AccountRepository) -> Try<Account>
    fun debit(no: String, amount: Amount): (AccountRepository) -> Try<Account>
    fun credit(no: String, amount: Amount): (AccountRepository) -> Try<Account>
    fun balance(no: String): (AccountRepository) -> Try<Balance>
}

object AccountServiceO : AccountService<Account, Amount, Balance> {

    override fun open(no: String, name: String, openingDate: Option<Date>) = { repo: AccountRepository ->
        with(repo.query(no)) {
            when (this) {
                is Success -> when (value) {
                    is Some -> Failure(Exception("Already existing account with no $no"))
                    is None ->
                        if (no.isEmpty() || name.isEmpty()) Failure(Exception("Account no or name cannot be blank"))
                        else if (openingDate.getOrElse { today }.before(today)) Failure(Exception("Cannot open account in the past"))
                        else repo.store(Account(no, name, openingDate.getOrElse { today }))

                }
                is Failure -> Failure(Exception("Failed to open account $no: $name", exception))
            }
        }
    }

    override fun close(no: String, closeDate: Option<Date>) = { repo: AccountRepository ->
        with(repo.query(no)) {
            when (this) {
                is Success -> {
                    val acc = value
                    when (acc) {
                        is Some ->
                            if (closeDate.getOrElse { today }.before(acc.t.dateOfOpening))
                                Failure(Exception("Close date $closeDate cannot be before opening date ${acc.t.dateOfOpening}"))
                            else repo.store(acc.t.copy(dateOfClosing = closeDate))

                        is None -> Failure(Exception("Account not found with $no"))
                    }
                }
                is Failure -> Failure(Exception("Fail in closing account $no", exception))
            }
        }
    }

    override fun debit(no: String, amount: Amount) = { repo: AccountRepository ->
        with(repo.query(no)) {
            when (this) {
                is Success -> {
                    val acc = value
                    when (acc) {
                        is Some -> if (acc.t.balance.amount < amount) Failure(Exception("Insufficient balance"))
                        else repo.store(acc.t.copy(balance = Balance(acc.t.balance.amount - amount)))
                        is None -> Failure(Exception("Account not found with $no"))
                    }
                }
                is Failure -> Failure(Exception("Fail in debit from $no amount $amount", exception))
            }

        }
    }

    override fun credit(no: String, amount: Amount) = { repo: AccountRepository ->
        with(repo.query(no)) {
            when (this) {
                is Success -> {
                    val acc = value
                    when (acc) {
                        is Some -> repo.store(acc.t.copy(balance = Balance(acc.t.balance.amount + amount)))
                        is None -> Failure(Exception("Account not found with $no"))
                    }
                }
                is Failure -> Failure(Exception("Fail in credit from $no amount $amount", exception))
            }
        }
    }

    override fun balance(no: String) = { repo: AccountRepository ->
        repo.balance(no)
    }
}


object App {

    val r = AccountRepositoryInMemoryO // this was not here in scala example

    fun op(no: String) = Try.monad().binding {
        val a = AccountServiceO.credit(no, BigDecimal.valueOf(100)).bind(r)
        val b = AccountServiceO.credit(no, BigDecimal.valueOf(300)).bind(r)
        val c = AccountServiceO.debit(no, BigDecimal.valueOf(160)).bind(r)
        val d = AccountServiceO.balance(no).bind(r)
        yields(d)
    }.ev()
}