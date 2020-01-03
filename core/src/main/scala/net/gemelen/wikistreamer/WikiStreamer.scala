package net.gemelen.wikistreamer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri}
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.stream._
import akka.stream.Attributes
import akka.stream.scaladsl._
import akka.stream.alpakka.kinesisfirehose.KinesisFirehoseFlowSettings
import akka.stream.alpakka.kinesisfirehose.scaladsl.KinesisFirehoseSink
import akka.stream.alpakka.sse.scaladsl.EventSource

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseAsync
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseAsyncClientBuilder
import com.amazonaws.services.kinesisfirehose.model.Record

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.duration.Duration
import java.nio.ByteBuffer

object WikiStreamer extends App {

  implicit val system = ActorSystem("wikistreamer")

  /*
    AWS credentials provider chain that looks for credentials in this order:
      Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_ACCESS_KEY (RECOMMENDED since they are recognized by all the AWS SDKs and CLI except for .NET), or AWS_ACCESS_KEY and AWS_SECRET_KEY (only recognized by Java SDK)
      Java System Properties - aws.accessKeyId and aws.secretKey
      Credential profiles file at the default location (~/.aws/credentials) shared by all AWS SDKs and the AWS CLI
      Credentials delivered through the Amazon EC2 container service if AWS_CONTAINER_CREDENTIALS_RELATIVE_URI" environment variable is set and security manager has permission to access the variable,
      Instance profile credentials delivered through the Amazon EC2 metadata service
      Web Identity Token credentials from the environment or container.
  */
  implicit val amazonKinesisFirehoseAsync: AmazonKinesisFirehoseAsync =
    AmazonKinesisFirehoseAsyncClientBuilder.defaultClient()

  system.registerOnTermination(amazonKinesisFirehoseAsync.shutdown())

  val uri = Uri("https://stream.wikimedia.org/v2/stream/recentchange")
  val send: HttpRequest => Future[HttpResponse] = Http().singleRequest(_)
  val retryDelay = 1 second

  val kinesisSettings = KinesisFirehoseFlowSettings
    .create()
    .withParallelism(1)
    .withMaxBatchSize(500)
    .withMaxRecordsPerSecond(5000)
    .withMaxBytesPerSecond(4000000)
    .withMaxRetries(5)
    .withBackoffStrategy(KinesisFirehoseFlowSettings.Exponential)
    .withRetryInitialTimeout(100 millis)

  val transform: ServerSentEvent => Record = (sse: ServerSentEvent) => (
    new Record().withData(ByteBuffer.wrap(sse.data.getBytes()))
  )

  val logAttributes = Attributes.logLevels(
    onElement = Attributes.LogLevels.Info,
    onFinish = Attributes.LogLevels.Info,
    onFailure = Attributes.LogLevels.Error)

  val rawSource = EventSource(uri, send, None, retryDelay)
  val source = rawSource
    .log(name = "wiki")
    .addAttributes(logAttributes)
    .map(transform)
 
  val sink = KinesisFirehoseSink("wiki", kinesisSettings)

  val pipeline = source.toMat(sink)(Keep.right)

  pipeline.run()
} 

