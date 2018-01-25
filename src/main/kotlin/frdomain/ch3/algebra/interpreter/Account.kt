package frdomain.ch3.algebra.interpreter

import arrow.core.None
import arrow.core.Option
import frdomain.ch3.algebra.interpreter.common.today
import java.math.BigDecimal
import java.util.*

typealias Amount = BigDecimal

object common {

    val today
        get() = Calendar.getInstance().time

}

class Balance(val amount: Amount = BigDecimal.ZERO)

class Account(val no: String, val name: String, val dateOfOpening: Date = today, val dateOfClosing: Option<Date> = None,
              val balance: Balance = Balance()) {

    fun copy(no: String = this.no, name: String = this.name, dateOfOpening: Date = this.dateOfOpening,
             dateOfClosing: Option<Date> = this.dateOfClosing, balance: Balance = this.balance) = Account(no, name, dateOfOpening, dateOfClosing, balance)
}