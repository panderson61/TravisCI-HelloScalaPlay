package repositories

import models.repository.MongoDbObject
import models.repository.Post
import play.Play.application
import reactivemongo.api.commands.{UpdateWriteResult, WriteResult}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.reflect.runtime.universe._
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
//import models.{Aggregation, AggregationResult, TempHierarchy, Test1, _}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, _}
import reactivemongo.api.{Cursor, MongoConnectionOptions, MongoDriver, ReadPreference}
import util.ImplicitConversions._

//USE THIS IF WE USE PLAY SERIALIZERS
//import play.api.libs.json._
//import play.modules.reactivemongo.json.collection._
//import play.modules.reactivemongo.json._

object PostingServiceRepository  {
  val driver = new MongoDriver
  val conOpts = MongoConnectionOptions(nbChannelsPerNode = 10)
  val connection = driver.connection(nodes = List(application.configuration.getString("mongodb.connection")),options = conOpts)
  val db = connection("posting")

  private def getCollection[T <: MongoDbObject : TypeTag](): String = {

    typeOf[T].toString match {
      case "models.repository.Post" => "post"
      case _ => throw new Exception("Unknown collection")
    }
  }

  def getObjects[T <: MongoDbObject : TypeTag](filter: BSONDocument)(implicit bsr: BSONDocumentReader[T]): Vector[T] = {
    val query = db[BSONCollection](getCollection[T]).find(filter)
    Await.result(query.cursor[T]().collect[Vector](), Duration.apply(20,"seconds"))
  }

  def upsertMongoObject[T <: MongoDbObject](upsertObject: T)(implicit bsw: BSONDocumentWriter[T]) {
    val upsertObjectFuture = db[BSONCollection](upsertObject.dbCollection).update(selector = BSONDocument("id" -> stringToBSONObjectID(upsertObject.id)), update = upsertObject, upsert = true)
    handleUpdateWriteResult(upsertObjectFuture)
  }

  def deleteMongoObject[T <: MongoDbObject : TypeTag](filter: BSONDocument) {
    val deleteMongoObjectFuture = db[BSONCollection](getCollection[T]).remove(filter)
    handleWriteResult(deleteMongoObjectFuture)
  }

  private def handleUpdateWriteResult(mongoResult: Future[UpdateWriteResult]) {
    mongoResult.onComplete {
      case Failure(e) => throw e
      case Success(lastError) => {
        println("successfully inserted document: " + lastError)
      }
    }
  }

  private def handleWriteResult(mongoResult: Future[WriteResult]) {
    mongoResult.onComplete {
      case Failure(e) => throw e
      case Success(lastError) => {
        println("successfully deleted document: " + lastError)
      }
    }
  }
  /*
    def upsertAggregation(aggregation: Aggregation) {
      val upsertAggregationFuture = db[BSONCollection]("aggregation").update(selector = BSONDocument("_id" -> stringToBSONObjectID(aggregation.id.get)),update = aggregation, upsert = true)
      handleResult(upsertAggregationFuture)
    }

    def getAggregation(filter: BSONDocument): List[Aggregation] = {
      val query = db[BSONCollection]("aggregation").find(filter)
      Await.result(query.cursor[Aggregation]().collect[List](), Duration.apply(20,"seconds"))
    }


    def upsertAggregationResult(aggregationResult: AggregationResult) {
      val upsertAggregationResultFuture = db[BSONCollection]("aggregation_result").update(selector = BSONDocument("_id" -> stringToBSONObjectID(aggregationResult.id.get)),update = aggregationResult, upsert = true)
      handleResult(upsertAggregationResultFuture)
    }

    def getAggregationResult(filter: BSONDocument): List[AggregationResult] = {
      val query = db[BSONCollection]("aggregation_result").find(filter)
      Await.result(query.cursor[AggregationResult]().collect[List](), Duration.apply(20,"seconds"))
    }

    //*** TEMP UNTIL WE HAVE A HIERARCHY SERVICE ***//
    def upsertTempHierarchy(tempHierarchy: TempHierarchy) {
      val upsertTempHierarchyResultFuture = db[BSONCollection]("temp_hierarchy").update(selector = BSONDocument("_id" -> stringToBSONObjectID(tempHierarchy.id.get)),update = tempHierarchy, upsert = true)
      handleResult(upsertTempHierarchyResultFuture)
    }

    def getTempHierarchy(filter: BSONDocument): List[TempHierarchy] = {
      val query = db[BSONCollection]("temp_hierarchy").find(filter)
      Await.result(query.cursor[TempHierarchy]().collect[List](), Duration.apply(20,"seconds"))
    }
    //*** END TEMP UNTIL WE HAVE A HIERARCHY SERVICE ***//

    def upsertTest1(test1: Test1) {
      val upsertTest1 = db[BSONCollection]("test1").update(selector = BSONDocument("_id" -> stringToBSONObjectID(test1.id)),update = BSONDocument("$set" -> test1), upsert = true)
      handleResult(upsertTest1)
    }

    def getTest1(filter: BSONDocument): List[Test1] = {
      val query = db[BSONCollection]("test1").find(filter)
      Await.result(query.cursor[Test1]().collect[List](), Duration.apply(20,"seconds"))
    }
  */

  private def handleResult(mongoResult: Future[UpdateWriteResult]) {
    mongoResult.onComplete {
      case Failure(e) => throw e
      case Success(lastError) => {
        println("successfully inserted document: " + lastError)
      }
    }
  }
}
