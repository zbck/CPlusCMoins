
import CPlusCMoinsMain.{gameLoop}
import conf.CplusCmoinsConf

import scala.util.{Failure, Success, Try}
import scala.util.Random

object CPlusCMoinsUtils {

  // Pick a random Int from a certain range
  def chooseRandomNumberToGuess()(implicit cplusCmoinsConf: CplusCmoinsConf) : Try[NumberToGuess] ={
    val range = cplusCmoinsConf.DEFAULT_RANGE

    range match{
      case Success(maximumValue) => Try{NumberToGuess(Random.nextInt(maximumValue))}
      case _ => Failure(new Exception(s"Problem with $range value. Probably not filled"))
    }
  }

  // Each answer have an ID. It is + 1 from the previous one
  def initializeFirstAnswerId()(implicit cplusCmoinsConf: CplusCmoinsConf) : Try[Int] ={
    val firstAnswerId = cplusCmoinsConf.FIRST_ANSWER_ID
    firstAnswerId match {
      case Success(answerId) =>Success(answerId)
      case _ => Failure(new Exception("Cannot initialise first Answer ID"))
    }
  }

  // Increment Answer ID
  def updateAnswerId(answerId : Try[Int]) : Try[Int] ={
    answerId match {
      case Success(id) => Try{id + 1}
      case _ => Failure(new Exception("Cannot update Answer ID"))
    }
  }

  // Ask a number. Retry if the answer is incorrect
  def askAnswer(answerId : Int)(implicit fileSystemUtils: IoUtil) : Try[Answer] ={

    println("What is you guess ?")
    println("Enter a number: ")

    fileSystemUtils.askUser() match {
      case Success(someInt) => Success(Answer(someInt, answerId))
      case _ => askAnswer(answerId)
    }
  }

  // Check if numbers are equal
  def isAnswerEqualNumberToGuess(answer: Answer, numberToGuess: NumberToGuess) : Try[Boolean] = {
    Try{answer.numberproposal == numberToGuess.numberToGuess} match {
      case Success(true) =>
        println(s"Great job! You guessed in ${answer.answerId} tries")
        Success(true)
      case Success(false) => Success(false)
      case _ => Failure(new Exception("Cannot proceed to equality comparaison"))
    }
  }

  // Check if answer is greater than number to guess
  def isAnswerHigherThanNumberToGuess(answer: Answer, numberToGuess: NumberToGuess) : Try[Boolean] = {
    Try{answer.numberproposal > numberToGuess.numberToGuess} match {
      case Success(true) =>
        println(s"You are too high")
        Success(true)
      case Success(false) => Success(false)
      case _ => Failure(new Exception("Cannot proceed to equality comparaison"))
    }
  }

  // Check if answer is lower than number to guess
  def isAnswerLowerThanNumberToGuess(answer: Answer, numberToGuess: NumberToGuess) : Try[Boolean] = {
    Try{answer.numberproposal < numberToGuess.numberToGuess} match {
      case Success(true) =>
        println(s"You are too low")
        Success(true)
      case Success(false) => Success(false)
      case _ => Failure(new Exception("Cannot proceed to equality comparaison"))
    }
  }

  // TODO : No need to check the 3 comparaisons each time
  // Check if the answer is the correct guess
  def isAnswerValid(answer: Answer, numberToGuess: NumberToGuess) : Try[Boolean] = {
    for {
      resultOfEquality <- isAnswerEqualNumberToGuess(answer, numberToGuess)
      _ = isAnswerHigherThanNumberToGuess(answer, numberToGuess)
      _ = isAnswerLowerThanNumberToGuess(answer, numberToGuess)
    } yield (resultOfEquality)
  }

  // Return true if the answer is correct
  def collectAnswerAndAnalyseIt(answerIdTry: Try[Int],
                                numberToGuessTry: Try[NumberToGuess])
                               (implicit ioUtil: IoUtil) : Try[Boolean] ={
    for {
      answerId <- answerIdTry
      numberToGuess <- numberToGuessTry
      answer <- CPlusCMoinsUtils.askAnswer(answerId)
      resultOfComparaison <- CPlusCMoinsUtils.isAnswerValid(answer, numberToGuess)
    } yield resultOfComparaison
  }

  def giveOtherTryIfNeeded(isAnswerAGoodGuess: Boolean,
                           answerIdTry: Try[Int],
                           numberToGuessTry: Try[NumberToGuess])
                          (implicit ioUtil: IoUtil) : Try[Unit] ={
    isAnswerAGoodGuess match {
      case true => Success(())
      case false => gameLoop(CPlusCMoinsUtils.updateAnswerId(answerIdTry), numberToGuessTry)
      case _ => Failure(new Exception("Cannot give An other tryGame Crash"))
    }
  }

}
