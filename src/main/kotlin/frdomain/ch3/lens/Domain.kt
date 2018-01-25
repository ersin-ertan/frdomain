package frdomain.ch3.lens

// Todo what are the correct types for App's val custAddrNoLens compose<>

class Address(val no: String, val street: String, val city: String, val state: String, val zip: String) {
    fun copy(no: String = this.no, street: String = this.street, city: String = this.city, state: String = this.state,
             zip: String = this.zip): Address = Address(no, street, city, state, zip)
}

class Customer(val id: Int, val name: String, val address: Address) {
    fun copy(id: Int = this.id, name: String = this.name, address: Address = this.address): Customer = Customer(id, name, address)
}


interface AddressLenses {

    // using get() because "Property initializers not allowed in interface"

    val noLens get() = Lens<Address, String>({ it.no }, { o, v -> o.copy(no = v) })

    val streetLens get() = Lens<Address, String>({ it.street }, { o, v -> o.copy(street = v) })

    val cityLens
        get() = Lens<Address, String>({ it.city }, { o, v -> o.copy(city = v) })

    val stateLens get() = Lens<Address, String>({ it.state }, { o, v -> o.copy(state = v) })

    val zipLens get() = Lens<Address, String>({ it.zip }, { o, v -> o.copy(zip = v) })
}

interface CustomerLenses {

    val nameLens get() = Lens<Customer, Int>({ it.id }, { o, v -> o.copy(id = v) })

    val addressLens get() = Lens<Customer, Address>({ it.address }, { o, v -> o.copy(address = v) })
}

object App : AddressLenses, CustomerLenses {

    val a = Address(no = "B-12", street = "Monroe Street", city = "Denver", state = "CO", zip = "80231")
    val c = Customer(12, "John D Cook", a)


//    val custAddrNoLens = addressLens.get.compose<>{ noLens.get }
//
//    init { // put these calls in init
//        custAddrNoLens.get(c)
//        custAddrNoLens.set(c, "B675")
//    }


}