package com.psinder.auth.role

import pl.brightinventions.codified.enums.CodifiedEnum

interface HasRole {
    val role: CodifiedEnum<Role, String>
}
