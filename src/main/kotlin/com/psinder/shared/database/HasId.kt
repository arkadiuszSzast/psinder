package com.psinder.shared.database

import org.litote.kmongo.Id

internal interface HasId<T> {
    val id: Id<T>
}
