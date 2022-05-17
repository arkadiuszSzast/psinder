package com.psinder.account.subscribers

import com.eventstore.dbclient.ReadStreamOptions
import com.psinder.account.AccountAggregate
import com.psinder.account.accountModule
import com.psinder.account.activation.commands.GenerateAccountActivationLinkHandler
import com.psinder.account.activation.commands.GenerateAccountActivationTokenHandler
import com.psinder.account.config.JwtConfig
import com.psinder.account.config.MailConfig
import com.psinder.account.create
import com.psinder.account.support.DatabaseAndEventStoreTest
import com.psinder.auth.principal.AuthorizedAccountAbilityProvider
import com.psinder.auth.principal.CanDoAnythingAbilityProvider
import com.psinder.events.streamName
import com.psinder.events.toEventData
import com.psinder.kediatr.kediatrTestModule
import com.psinder.mail.FakeSendMailCommandHandler
import com.psinder.mail.MailSender
import com.psinder.mail.MailSendingError
import com.psinder.mail.MailSentResult
import com.psinder.mail.RecordingMailSender
import com.psinder.mail.events.MailSendingErrorEvent
import com.psinder.mail.events.MailSentSuccessfullyEvent
import com.psinder.shared.EmailAddress
import com.psinder.shared.config.ApplicationConfig
import com.psinder.test.utils.faker
import com.trendyol.kediatr.CommandBus
import io.kotest.core.test.TestCase
import io.ktor.server.testing.withApplication
import io.traxter.eventstoredb.StreamName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module
import pl.brightinventions.codified.enums.codifiedEnum
import strikt.api.expectThat
import strikt.assertions.first
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isEqualTo

private val testingModules = module {
    single { CanDoAnythingAbilityProvider() } bind AuthorizedAccountAbilityProvider::class
    single {
        RecordingMailSender {
            when (it.to) {
                EmailAddress.create("invalid@mail.com") ->
                    MailSentResult.Error(it.id.cast(), MailSendingError("Invalid mail address"))
                else -> MailSentResult.Success(it.id.cast())
            }
        }
    } bind MailSender::class
    single { GenerateAccountActivationLinkHandler(ApplicationConfig, get(), get()) }
    single { GenerateAccountActivationTokenHandler(JwtConfig, get()) }
    single { FakeSendMailCommandHandler(get(), get()) }
}.plus(kediatrTestModule)

class ActivationMailSenderSubscriberKtTest : DatabaseAndEventStoreTest(testingModules) {
    private val commandBus = get<CommandBus>()
    private val mailSender = get<RecordingMailSender>()

    override fun beforeEach(testCase: TestCase) {
        mailSender.clear()
        super.beforeEach(testCase)
    }

    init {

        describe("ActivationMailSenderSubscriber") {

            it("should send activation email") {
                withApplication {
                    val application = this.application
                    launch {
                        // arrange
                        val accountAggregateCreatedEvent = AccountAggregate.Events.create(
                            faker.accountModule.emailAddress(),
                            faker.accountModule.personalData(),
                            faker.accountModule.role().codifiedEnum(),
                            faker.accountModule.hashedPassword(),
                            faker.accountModule.timeZone()
                        )

                        // act
                        application.activationMailSenderSubscriber(eventStoreDb, commandBus, MailConfig)
                        eventStoreDb.appendToStream(
                            accountAggregateCreatedEvent.streamName,
                            accountAggregateCreatedEvent.toEventData(),
                        )
                        delay(600)

                        // assert
                        val sendingMailsResults = mailSender.getAll().values
                        expectThat(sendingMailsResults) {
                            hasSize(1)
                            and { first().isA<MailSentResult.Success>() }
                        }
                        val mailId = (sendingMailsResults.single() as MailSentResult.Success).mailId
                        val sendingMailsEvents = eventStoreDb.readStream(
                            StreamName("mail-$mailId"),
                            ReadStreamOptions.get().resolveLinkTos()
                        ).events.map { it.event }
                        expectThat(sendingMailsEvents) {
                            hasSize(1)
                            and { get { first().eventType }.isEqualTo(MailSentSuccessfullyEvent.fullEventType.get()) }
                        }
                    }
                }
            }

            it("should not send activation email") {
                withApplication {
                    val application = this.application
                    launch {
                        // arrange
                        val accountAggregateCreatedEvent = AccountAggregate.Events.create(
                            EmailAddress.create("invalid@mail.com"),
                            faker.accountModule.personalData(),
                            faker.accountModule.role().codifiedEnum(),
                            faker.accountModule.hashedPassword(),
                            faker.accountModule.timeZone()
                        )

                        // act
                        application.activationMailSenderSubscriber(eventStoreDb, commandBus, MailConfig)
                        eventStoreDb.appendToStream(
                            accountAggregateCreatedEvent.streamName,
                            accountAggregateCreatedEvent.toEventData(),
                        )
                        delay(600)

                        // assert
                        val sendingMailsResults = mailSender.getAll().values
                        expectThat(sendingMailsResults) {
                            hasSize(1)
                            and { first().isA<MailSentResult.Error>() }
                        }
                        val mailId = (sendingMailsResults.single() as MailSentResult.Error).mailId
                        val sendingMailsEvents = eventStoreDb.readStream(
                            StreamName("mail-$mailId"),
                            ReadStreamOptions.get().resolveLinkTos()
                        ).events.map { it.event }
                        expectThat(sendingMailsEvents) {
                            hasSize(1)
                            and { get { first().eventType }.isEqualTo(MailSendingErrorEvent.fullEventType.get()) }
                        }
                    }
                }
            }
        }
    }
}
