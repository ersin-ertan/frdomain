package frdomain.ch3.smartconstructor

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import arrow.data.Failure
import arrow.data.Success
import arrow.data.Try
import frdomain.ch3.smartconstructor.common.today
import java.math.BigDecimal
import java.util.*

// Todo AbcAccount class constructorns were changed from private to internal

typealias Amount = BigDecimal

object common {

    val today = Calendar.getInstance().time
}


class Balance(amount: Amount = BigDecimal.ZERO)

sealed class Account {
    abstract val no: String
    abstract val name: String
    abstract val dateOfOpen: Option<Date>
    abstract val dateOfClose: Option<Date>
    abstract val balance: Balance

    /**
     * In order to ensure that the user cannot use `apply` and `copy` as well, we need
     * to delegate both of them to the smart constructor. Still there can be some hairy issues
     * as in http://stackoverflow.com/questions/19462598/scala-case-class-implementation-of-smart-constructors
     **/

    class CheckingAccount
    internal constructor(override val no: String, override val name: String, override val dateOfOpen: Option<Date>,
                         override val dateOfClose: Option<Date> = None, override val balance: Balance = Balance()) : Account() {

        fun copy(no: String = this.no, name: String = this.name, dateOfOpen: Option<Date> = this.dateOfOpen,
                 dateOfClose: Option<Date> = this.dateOfClose, balance: Balance = this.balance) =
                AccountO.checkingAccount(no, name, dateOfOpen, dateOfClose, balance)

        fun apply(no: String = this.no, name: String = this.name, dateOfOpen: Option<Date> = this.dateOfOpen,
                  dateOfClose: Option<Date> = this.dateOfClose, balance: Balance = this.balance) =
                AccountO.checkingAccount(no, name, dateOfOpen, dateOfClose, balance)
    }

    class SavingsAccount
    internal constructor(override val no: String, override val name: String, val rateOfInterest: Amount,
                         override val dateOfOpen: Option<Date>, override val dateOfClose: Option<Date> = None, override val balance: Balance = Balance()) : Account() {

        fun copy(no: String = this.no, name: String = this.name, rateOfInterest: Amount = this.rateOfInterest, dateOfOpen: Option<Date> = this.dateOfOpen,
                 dateOfClose: Option<Date> = this.dateOfClose, balance: Balance = this.balance) =
                AccountO.savingsAccount(no, name, rateOfInterest, dateOfOpen, dateOfClose, balance)

        fun apply(no: String = this.no, name: String = this.name, rateOfInterest: Amount = this.rateOfInterest,
                  dateOfOpen: Option<Date> = this.dateOfOpen, dateOfClose: Option<Date> = this.dateOfClose,
                  balance: Balance = this.balance) =
                AccountO.savingsAccount(no, name, rateOfInterest, dateOfOpen, dateOfClose, balance)
    }
}

object AccountO {
    fun checkingAccount(no: String, name: String, openDate: Option<Date>, closeDate: Option<Date>, balance: Balance): Try<Account> =
            closeDateCheck(openDate, closeDate).map { d ->
                Account.CheckingAccount(no, name, Some(d.first), d.second, balance)
            }


    fun savingsAccount(no: String, name: String, rate: BigDecimal, openDate: Option<Date>, closeDate: Option<Date>,
                       balance: Balance): Try<Account> {

        println("in smart")
        return closeDateCheck(openDate, closeDate).map { (d, optD) ->
            // switched if logic
            if (rate > BigDecimal.ZERO) Account.SavingsAccount(no, name, rate, Some(d), optD, balance)
            else throw Exception("Interest rate $rate must be > 0")
        }
    }

    private fun closeDateCheck(openDate: Option<Date>, closeDate: Option<Date>): Try<Pair<Date, Option<Date>>> =
            with(openDate.getOrElse { today }) {

                return closeDate.map { cd ->
                    if (cd.before(this)) Failure<Pair<Date, Option<Date>>>(Exception("Close date <$cd> cannot be earlier than open date <$this>"))
                    else Success(Pair(this, Some(cd)))
                }.getOrElse { Success(Pair(this, closeDate)) }
            }
}