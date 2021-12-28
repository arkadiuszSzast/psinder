package com.psinder.account

import com.fasterxml.jackson.annotation.JsonCreator

internal data class PersonalData(val name: Name, val surname: Surname? = null, val addressData: AddressData? = null) {
    companion object {
        @JvmStatic
        @JsonCreator
        internal fun create(name: String, surname: String?, addressData: AddressData?): PersonalData {
            return PersonalData(Name.create(name), surname?.let { Surname.create(it) }, addressData)
        }
    }
}

@JvmInline
value class Name private constructor(val value: String) {
    companion object {
        internal fun create(name: String): Name {
            return Name(name.trim().replaceFirstChar { it.uppercase() })
        }
    }
}

@JvmInline
value class Surname private constructor(val value: String) {
    companion object {
        internal fun create(surname: String): Surname {
            return Surname(surname.trim().replaceFirstChar { it.uppercase() })
        }
    }
}
