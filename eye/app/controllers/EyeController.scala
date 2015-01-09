package controllers

import org.springframework.social.twitter.api.Tweet
import play.api.mvc._
import todayiam.watchdog.Searcher


class EyeController(searcher: Searcher) extends Controller {
  def index = Action {
    Ok(views.html.index("Welcome to the TodayIAm home page!"))
  }

  def matches(originalId: Long) = Action { implicit request =>
    val original: Tweet = searcher.getById(originalId)
    val related: Seq[Tweet] = request.queryString.getOrElse("id", None) match {
      case ids: Seq[String] => ids.map({ case s: String => searcher.getById(s.toLong)})
      case _ => scala.collection.JavaConversions.asScalaBuffer(searcher.findRelated(original)).toList
    }
    Ok(views.html.matches(original, related))
  }
}