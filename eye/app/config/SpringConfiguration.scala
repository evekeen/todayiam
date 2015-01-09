package config

import java.io.FileNotFoundException

import controllers.EyeController
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.core.io.FileSystemResource
import org.springframework.social.twitter.api.Twitter
import org.springframework.social.twitter.api.impl.TwitterTemplate
import todayiam.watchdog.{KeywordsAnalyzer, KeywordsAnalyzerImpl, Searcher, SearcherImpl}

/**
 * Author: Alexander Ivkin
 * Date: 11/09/14
 */

object SpringConfiguration {
  @Bean def propertyPlaceholderConfigurer: PropertyPlaceholderConfigurer = {
    val propertyPlaceholderConfigurer: PropertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer
    val location: FileSystemResource = new FileSystemResource("todayiam.properties")
    if (!location.exists) throw new FileNotFoundException("todayiam.properties not found in working directory: " + System.getProperty("user.dir"))
    propertyPlaceholderConfigurer.setLocation(location)
    propertyPlaceholderConfigurer
  }
}

@Configuration
class SpringConfiguration {

  @Value("${consumerKey}") private val consumerKey: String = null
  @Value("${consumerSecret}") private val consumerSecret: String = null
  @Value("${accessToken}") private val accessToken: String = null
  @Value("${accessTokenSecret}") private val accessTokenSecret: String = null

  val propertiesFilename: String = "todayiam.properties"

  @Bean def twitter: Twitter =  new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret)

  @Bean def keywordAnalyzer: KeywordsAnalyzer = new KeywordsAnalyzerImpl()

  @Bean def searcher: Searcher = new SearcherImpl(twitter, keywordAnalyzer)

  @Bean def eyeController: EyeController = new EyeController(searcher)

}
