package com.psinder.account.activation.commands

import com.psinder.account.config.JwtConfig
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.database.RecordingEventStoreDB
import com.psinder.kediatr.kediatrTestModule
import com.psinder.shared.config.ApplicationConfig
import com.psinder.test.utils.withKoin
import io.kotest.core.spec.style.DescribeSpec
import io.traxter.eventstoredb.EventStoreDB
import org.koin.dsl.bind
import org.koin.dsl.module
import org.litote.kmongo.newId
import strikt.api.expectThat
import strikt.assertions.isNotNull
import strikt.assertions.startsWith

class GenerateAccountActivationLinkHandlerTest : DescribeSpec() {

    private val neededModules = module {
        single { CanDoAnythingAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
        single { RecordingEventStoreDB() } bind EventStoreDB::class
        single { GenerateAccountActivationLinkHandler(ApplicationConfig, get(), get()) }
        single { GenerateAccountActivationTokenHandler(JwtConfig, get()) }
    }.plus(kediatrTestModule)

    init {

        describe("GenerateAccountActivateLink") {

            it("should generate link") {
                withKoin(neededModules) {
                    // arrange
                    val handler = get<GenerateAccountActivationLinkHandler>()
                    val command = GenerateAccountActivationLinkCommand(newId())

                    // act
                    val result = handler.handleAsync(command)

                    // assert
                    expectThat(result) {
                        and { get { link.toString() }.startsWith("${ApplicationConfig.webClientAppUrl}account/activate") }
                        and { get { link.parameters["token"] }.isNotNull() }
                    }
                }
            }
        }
    }
}
