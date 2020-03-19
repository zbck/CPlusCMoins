package conf

import com.typesafe.config.Config
import scala.util.Try

class CplusCmoinsConf(conf : Config) {

  // Take conf only for cpcm game
  //val cpcmConf = conf.getConfig("cpcm")

  val DEFAULT_RANGE = Try{conf.getInt("cpcm.defaultRange")}
  val FIRST_ANSWER_ID = Try{conf.getInt("cpcm.firstAnswerId")}
}