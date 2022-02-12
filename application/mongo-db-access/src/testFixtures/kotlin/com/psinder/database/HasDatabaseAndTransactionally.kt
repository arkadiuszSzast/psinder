package com.psinder.database

import com.psinder.database.transactionally.Transactionally
import org.litote.kmongo.coroutine.CoroutineDatabase

interface HasDatabaseAndTransactionally {
    val transactionally: Transactionally
    val db: CoroutineDatabase
}
