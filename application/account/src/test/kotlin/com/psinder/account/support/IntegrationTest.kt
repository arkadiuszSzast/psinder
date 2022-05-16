package com.psinder.account.support

import aws.sdk.kotlin.services.s3.S3Client
import com.eventstore.dbclient.EventStoreDBClient
import com.eventstore.dbclient.EventStoreDBPersistentSubscriptionsClient
import com.psinder.aws.HasS3ClientProvider
import com.psinder.aws.createBuckets
import com.psinder.aws.removeAllBuckets
import com.psinder.aws.s3.BucketName
import com.psinder.database.HasDatabaseAndTransactionally
import com.psinder.database.HasEventStoreClient
import com.psinder.database.transactionally.Transactionally
import com.psinder.integrationTestModules
import com.psinder.mail.HasMailSender
import com.psinder.mail.RecordingMailSender
import com.psinder.shared.provider.Provider
import io.kotest.common.runBlocking
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.traxter.eventstoredb.EventStoreDB
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.inject
import org.litote.kmongo.coroutine.CoroutineDatabase

abstract class IntegrationTest(
    private val neededModules: List<Module>,
    private val buckets: List<BucketName> = emptyList()
) : KoinTest, HasDatabaseAndTransactionally, HasEventStoreClient, HasS3ClientProvider, HasMailSender, DescribeSpec() {
    override val transactionally: Transactionally by inject()
    override val db: CoroutineDatabase by inject()
    override val client: EventStoreDBClient by inject()
    override val persistedSubscriptionClient: EventStoreDBPersistentSubscriptionsClient by inject()
    override val eventStoreDb: EventStoreDB by inject()
    override val s3ClientProvider: Provider<S3Client> by inject()
    override val mailSender: RecordingMailSender by inject()

    override fun beforeEach(testCase: TestCase) {
        runBlocking {
            mailSender.clear()
            db.dropAllCollections()
            s3ClientProvider.removeAllBuckets()
            s3ClientProvider.createBuckets(buckets)
        }
    }

    override fun afterSpec(spec: Spec) {
        stopKoin()
    }

    init {
        startKoin {
            modules(integrationTestModules + neededModules)
        }
    }
}
