package com.psinder.database

import org.litote.kmongo.Id

interface HasId<T> {
    val id: Id<T>
}
