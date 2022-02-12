package com.psinder.database.config

import com.psinder.database.DatabaseTest
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.litote.kmongo.Id
import org.litote.kmongo.eq
import org.litote.kmongo.newId
import strikt.api.expectThat
import strikt.assertions.hasSize
import strikt.assertions.isEmpty

class TransactionallyTest : DatabaseTest() {
    init {
        describe("rollback transaction") {

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

            it("should rollback delete when exception occurred") {
                db.getCollection<Person>().insertOne(Person(newId(), "Joe"))

                runCatching {
                    transactionally.call { session ->
                        db.getCollection<Person>().deleteOne(session, Person::name eq "Joe")
                        throw Error()
                    }
                }

                expectThat(db.getCollection<Person>().find().toList())
                    .hasSize(1)
            }
        }

        describe("without transaction") {

            it("should save everything before exception") {
                runCatching {
                    db.getCollection<Person>().insertOne(Person(newId(), "Joe"))
                    throw Error()
                    @Suppress("UNREACHABLE_CODE")
                    db.getCollection<Person>().insertOne(Person(newId(), "Doe"))
                }

                expectThat(db.getCollection<Person>().find().toList())
                    .hasSize(1)
            }
        }
    }
}

@Serializable
data class Person(@Contextual val id: Id<Person>, val name: String)
