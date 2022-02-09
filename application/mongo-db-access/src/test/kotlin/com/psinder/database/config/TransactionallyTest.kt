package com.psinder.database.config

import com.psinder.database.utils.DatabaseTest
import kotlinx.serialization.Serializable
import com.psinder.database.utils.kmongoModule
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import strikt.api.expectThat
import strikt.assertions.isEmpty

class TransactionallyTest : DatabaseTest(kmongoModule) {
    init {
        describe("rollback transaction")
        {

            it("should rollback insert when exception occurred") {
                runCatching {
                    transactionally.call { session ->
                        db.getCollection<Person>().insertOne(session, Person(newId(), "Joe"))
                        throw Error()
                        @Suppress("UNREACHABLE_CODE")
                        db.getCollection<Person>().insertOne(session, Person(newId(), "Doe"))
                    }
                }

                expectThat(db.getCollection<Person>().find().toList())
                    .isEmpty()

            }
        }
    }
}

@Serializable
data class Person(val id: Id<Person>, val name: String)