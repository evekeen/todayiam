package controllers

import org.springframework.social.twitter.api.Tweet
import play.api.mvc._
import todayiam.watchdog.Searcher


class EyeController(searcher: Searcher) extends Controller {
  def index = Action {
    Ok(views.html.index("Welcome to the TodayIAm home page!"))
  }

  def matches(originalId: Long) = Action { implicit request =>
    Ok(views.html.matches(searcher.getById(originalId), findRelated(originalId, request.queryString.getOrElse("id", None))))
  }

  def findRelated(originalId: Long, relatedIds: Any): Seq[Tweet] = {
    relatedIds match {
      case ids: Seq[Any] => ids.map({ case s: String => searcher.getById(s.toLong)})
      case _ => scala.collection.JavaConversions.asScalaBuffer(searcher.findRelated(originalId)).toList
    }
  }
}