package com.psinder.auth

import pl.brightinventions.codified.enums.CodifiedEnum

interface HasRole {
    val role: CodifiedEnum<Role, String>
}
