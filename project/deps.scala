import sbt._

object v {
  val akka = "2.6.1"
  val alpakka = "1.1.2"
}

object deps {
  // logging
  val scalaLogging            = "com.typesafe.scala-logging"        %% "scala-logging"                 % "3.9.2"
  val loggingBackend          = "org.slf4j"                         %  "slf4j-log4j12"                 % "1.7.16"
  // misc
  val typesafeConfig          = "com.typesafe"                      %  "config"                        % "1.3.4"
  // akka
  val akkaCore                = "com.typesafe.akka"                 %% "akka-actor-typed"              % v.akka
  val akkaStream              = "com.typesafe.akka"                 %% "akka-stream"                   % v.akka
  val akkaHttp                = "com.typesafe.akka"                 %% "akka-http"                     % "10.1.11"
  // alpakka
  val alpakkaKinesis          = "com.lightbend.akka"                %% "akka-stream-alpakka-kinesis"   % v.alpakka
  val alpakkaSse              = "com.lightbend.akka"                %% "akka-stream-alpakka-sse"       % v.alpakka


  // set of dependencies
  val akka = Seq(akkaCore, akkaHttp, akkaStream)
  val alpakka = Seq(alpakkaSse, alpakkaKinesis)
  val logging = Seq(scalaLogging, loggingBackend)
}

