package conf

import com.typesafe.config.ConfigFactory

trait ApplicationConf {

  val appConf = ConfigFactory.load()

}
