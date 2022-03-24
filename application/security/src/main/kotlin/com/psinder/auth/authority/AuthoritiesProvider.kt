package com.psinder.auth.authority

import com.psinder.auth.role.Role
import pl.brightinventions.codified.enums.CodifiedEnum
import pl.brightinventions.codified.enums.codifiedEnum

interface AuthoritiesProvider {
    val authorities: Authorities
}

interface Authorities : Map<CodifiedEnum<Role, String>, List<Authority>> {
    companion object {
        fun create(authorities: Map<CodifiedEnum<Role, String>, List<Authority>>): Authorities = Simple(authorities)
        fun create(vararg authorities: Pair<CodifiedEnum<Role, String>, List<Authority>>): Authorities =
            Simple(authorities.toMap())
    }
}

private class Simple(private val authorities: Map<CodifiedEnum<Role, String>, List<Authority>>) :
    Map<CodifiedEnum<Role, String>, List<Authority>> by authorities, Authorities

fun authoritiesFor(
    role: Role,
    customize: AuthoritiesListBuilder.() -> Unit
): Pair<CodifiedEnum<Role, String>, List<Authority>> = authoritiesFor(role.codifiedEnum(), customize)

fun authoritiesFor(
    role: CodifiedEnum<Role, String>,
    customize: AuthoritiesListBuilder.() -> Unit
): Pair<CodifiedEnum<Role, String>, List<Authority>> {
    return Pair(role, AuthoritiesListBuilder().apply(customize).build())
}
