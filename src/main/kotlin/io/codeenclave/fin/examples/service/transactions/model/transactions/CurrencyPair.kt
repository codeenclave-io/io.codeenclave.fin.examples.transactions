package io.codeenclave.fin.examples.service.transactions.model.transactions

typealias Currency = String
typealias CurrencyPair = Pair<String,String>

private val currencies : Collection<String> = setOf(
    "EUR",
    "USD",
    "GBP",
    "JPY",
    "MXN",
    "RMB"
)

private val currencyPairs : Collection<CurrencyPair> = setOf(
    Pair("EUR", "USD"),
    Pair("EUR", "CHF"),
    Pair("EUR", "GBP"),
    Pair("EUR", "JPY"),
    Pair("EUR", "MXN"),
    Pair("EUR", "RMB"),

    Pair("USD", "EUR"),
    Pair("USD", "CHF"),
    Pair("USD", "GBP"),
    Pair("USD", "JPY"),
    Pair("USD", "MXN"),
    Pair("USD", "RMB"),

    Pair("GBP", "USD"),
    Pair("GBP", "CHF"),
    Pair("GBP", "EUR"),
    Pair("GBP", "JPY"),
    Pair("GBP", "MXN"),
    Pair("GBP", "RMB"),
)

fun getCurrency() : Currency {
    return currencies.random()
}

fun getCurrencyPair() : CurrencyPair {
    return currencyPairs.random()
}