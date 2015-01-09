import config.SpringConfiguration
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import play.api.Application

/**
 * Author: Alexander Ivkin
 * Date: 11/09/14
 */
object Global extends play.api.GlobalSettings {
  private val ctx: AnnotationConfigApplicationContext = new AnnotationConfigApplicationContext(classOf[SpringConfiguration])

  override def getControllerInstance[A](clazz: Class[A]): A = {
    ctx.getBean(clazz)
  }

  override def onStart(app: Application) {
    super.onStart(app)
//    ctx.scan("controllers", "models")
  }

  override def onStop(app: Application) {
    ctx.close()
    super.onStop(app)
  }
}