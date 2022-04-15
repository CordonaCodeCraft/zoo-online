package tech.cordona.zooonline.config.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ReadConcern
import com.mongodb.ReadPreference
import com.mongodb.TransactionOptions
import com.mongodb.WriteConcern
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.MongoTemplate


@Configuration
class MongoClientConfig : AbstractMongoClientConfiguration() {

	@Value("\${spring.data.mongodb.uri}")
	lateinit var mongoURI: String

	@Bean
	override fun mongoClient() = ConnectionString(mongoURI)
		.let { connection ->
			MongoClientSettings.builder()
				.applyConnectionString(connection)
				.build()
		}
		.let { settings -> MongoClients.create(settings) }

	@Bean
	fun transactionManager(mongoTemplate: MongoTemplate) = TransactionOptions.builder()
		.readConcern(ReadConcern.MAJORITY)
		.readPreference(ReadPreference.primary())
		.writeConcern(WriteConcern.MAJORITY.withJournal(true))
		.build()
		.let { txnOptions -> MongoTransactionManager(mongoTemplate.mongoDatabaseFactory, txnOptions) }

	override fun getDatabaseName() = "zoo_online"
	override fun getMappingBasePackages() = setOf("tech.cordona")
}