package com.psinder.auth

import pl.brightinventions.codified.enums.CodifiedEnum

interface AuthoritiesProvider {
    val authorities: Map<CodifiedEnum<Role, String>, List<Authority>>
}
