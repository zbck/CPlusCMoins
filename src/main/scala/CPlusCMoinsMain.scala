
import conf.{ApplicationConf, CplusCmoinsConf}

import scala.util.{Failure, Success, Try}

// TODO : Add logger
object CPlusCMoinsMain extends  ApplicationConf {

  def main(args: Array[String]): Unit = {
    // Initialisation : MetaData
    implicit val conf = new CplusCmoinsConf(appConf)
    implicit val defaultIo = IoUtil.defaultIoUtil

    // Initialisation : Game
    val answerId = CPlusCMoinsUtils.initializeFirstAnswerId
    val numberToGuess = CPlusCMoinsUtils.chooseRandomNumberToGuess
    gameLoop(answerId, numberToGuess)
  }

  // Heart of the game. Ask player to answer and act in consequences
  def gameLoop(answerIdTry: Try[Int], numberToGuessTry: Try[NumberToGuess])(implicit ioUtil: IoUtil) : Try[Unit] ={
    (for{
      isAnswerAGoodGuess <- CPlusCMoinsUtils.collectAnswerAndAnalyseIt(answerIdTry, numberToGuessTry)
      _ <- CPlusCMoinsUtils.giveOtherTryIfNeeded(isAnswerAGoodGuess, answerIdTry, numberToGuessTry)
    } yield ()) match{
      case Success(()) => Success(())
      case Failure(e) => Failure(new Exception(e))
      case e =>
        println(e)
        Failure(new Exception("Cannot run Game"))
    }
  }
}