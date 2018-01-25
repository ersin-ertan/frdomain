package frdomain.ch3.repository.reader

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.data.Failure
import arrow.data.Success
import arrow.data.Try
import frdomain.ch3.repository.Account
import frdomain.ch3.repository.AccountRepository
import frdomain.ch3.repository.Amount
import frdomain.ch3.repository.Balance
import frdomain.ch3.repository.common.today
import java.util.*

interface AccountService<Account, Amount, Balance> {
    fun open(no: String, name: String, openingDate: Option<Date>): Reader<AccountRepository, Try<Account>>
    fun close(no: String, closeDate: Option<Date>): Reader<AccountRepository, Try<Account>>
    fun debit(no: String, amount: Amount): Reader<AccountRepository, Try<Account>>
    fun credit(no: String, amount: Amount): Reader<AccountRepository, Try<Account>>
    fun balance(no: String): Reader<AccountRepository, Try<Balance>>
}

object AccountServiceO : AccountService<Account, Amount, Balance> {

    override fun open(no: String, name: String, openingDate: Option<Date>) = Reader({ repo: AccountRepository ->
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
    })

    override fun close(no: String, closeDate: Option<Date>) = Reader({ repo: AccountRepository ->
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
    })

    override fun debit(no: String, amount: Amount) = Reader({ repo: AccountRepository ->
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
    })

    override fun credit(no: String, amount: Amount) = Reader({ repo: AccountRepository ->
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
    })

    override fun balance(no: String) = Reader({ repo: AccountRepository -> repo.balance(no) })
}
