package frdomain.ch4.patterns.monoids.foldable

import java.math.BigDecimal
import java.util.*

sealed class TransactionType {
    object DR : TransactionType()
    object CR : TransactionType()
}

sealed class Currency {
    object USD : Currency()
    object JPY : Currency()
    object AUD : Currency()
    object INR : Currency()
}
typealias Amount = BigDecimal

class Money(m: Map<Currency, Amount>) {
//    fun toBaseCurrency: Amount = ???
}

class Transaction(val txid: String, val accountNo: String, val date: Date, val amount: Money, val txnType: TransactionType, val status: Boolean)

class Balance(val b: Money)

interface Analytics<Transaction, Balance, Money> {
//    fun maxDebitOnDay(txns: List<Transaction>)(implicit m: Monoid<Money>): Money
//    fun sumBalances(bs: List<Balance>)(implicit m: Monoid<Money>): Money
}

object AnalyticsO : Analytics<Transaction, Balance, Money> /*: Utils */ {

//            final val baseCurrency = Currency.USD
//
//    private fun valueOf(txn: Transaction): Money  {
//        if (txn.status) txn.amount
//        else MoneyAdditionMonoid.op(txn.amount, Money(Map(baseCurrency -> BigDecimal(100))))
//    }
//
//    private fun creditBalance(bal: Balance): Money = {
//        if (bal.b.toBaseCurrency > 0) bal.b else zeroMoney
//    }
//
//    fun maxDebitOnDay(txns: List<Transaction>)(implicit m: Monoid<Money>): Money = {
//        mapReduce(txns.filter(_.txnType == DR))(valueOf)
//    }
//
//    fun sumBalances(bs: List<Balance>)(implicit m: Monoid<Money>): Money = {
//        mapReduce(bs)(creditBalance)
//    }
}