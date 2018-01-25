package frdomain.ch3.lens

import arrow.syntax.function.andThen

class Lens<O, V>(
        val get: (O) -> V,
        val set: (O, V) -> O
)

object LensO {
    fun <Outer, Inner, Value> compose(outer: Lens<Outer, Inner>, inner: Lens<Inner, Value>) = Lens<Outer, Value>(
            get = outer.get.andThen(inner.get),
            set = { obj, value -> outer.set(obj, inner.set(outer.get(obj), value)) }
    )
}