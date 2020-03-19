


import com.typesafe.config.ConfigValueFactory
import conf.CplusCmoinsConf
import org.scalatest._

import org.mockito.MockitoSugar._
import org.mockito.ArgumentMatchersSugar._
import CPlusCMoinsUtils._

import scala.util.{Failure, Success}

class CPlusCMoinUtilSpec extends FlatSpec with Matchers with ApplicationConfForSpec{

  "CPlusCMoinUtil" should "choose a Correct Random Number To Guess" in {
    // Nominal case
    val confFilled = appConf.withValue("cpcm.defaultRange",
      ConfigValueFactory.fromAnyRef(1))
    implicit val conf  = new CplusCmoinsConf(confFilled)
    chooseRandomNumberToGuess should be(Success(NumberToGuess(0)))

    // Error case
    val confNotFilled = appConf
    implicit val confEmpty  = new CplusCmoinsConf(confNotFilled)
    chooseRandomNumberToGuess()(confEmpty) should be a 'Failure
  }

  it should "choose a initialize First Answer Id" in {
    // Nominal case
    val confFilled = appConf.withValue("cpcm.firstAnswerId",
      ConfigValueFactory.fromAnyRef(1))
    implicit val conf  = new CplusCmoinsConf(confFilled)
    initializeFirstAnswerId should be(Success(1))

    // Error case
    val confNotFilled = appConf
    implicit val confEmpty  = new CplusCmoinsConf(confNotFilled)
    initializeFirstAnswerId()(confEmpty) should be a 'Failure
  }

    it should "increment answer Id correctly" in {
    updateAnswerId(Success(3)) should be(Success(4))
    updateAnswerId(Failure(new Exception("Cannot proceed to equality comparaison"))) should be a 'Failure
  }

  it should "tell if it's equal" in {
    val correctAnswer = Answer(3, 1)
    val wrongAnswer = Answer(4, 1)
    val numberToGuess = NumberToGuess(3)

    isAnswerEqualNumberToGuess(correctAnswer, numberToGuess) should be(Success(true))
    isAnswerEqualNumberToGuess(wrongAnswer, numberToGuess) should be(Success(false))
    isAnswerEqualNumberToGuess(null, numberToGuess) should be a 'Failure
  }

  it should "tell if it's higher" in {
    val correctAnswer = Answer(4, 1)
    val wrongAnswer = Answer(3, 1)
    val numberToGuess = NumberToGuess(3)

    isAnswerHigherThanNumberToGuess(correctAnswer, numberToGuess) should be(Success(true))
    isAnswerHigherThanNumberToGuess(wrongAnswer, numberToGuess) should be(Success(false))
    isAnswerHigherThanNumberToGuess(null, numberToGuess) should be a 'Failure
  }

  it should "tell if it's lower" in {
    val correctAnswer = Answer(2, 1)
    val wrongAnswer = Answer(3, 1)
    val numberToGuess = NumberToGuess(3)

    isAnswerLowerThanNumberToGuess(correctAnswer, numberToGuess) should be(Success(true))
    isAnswerLowerThanNumberToGuess(wrongAnswer, numberToGuess) should be(Success(false))
    isAnswerLowerThanNumberToGuess(null, numberToGuess) should be a 'Failure
  }

  it should "check if the answer is valid" in {
    val correctAnswer = Answer(3, 1)
    val highAnswer = Answer(4, 1)
    val lowerAnswer = Answer(5, 1)
    val numberToGuess = NumberToGuess(3)

    isAnswerValid(correctAnswer, numberToGuess) should be(Success(true))
    isAnswerValid(highAnswer, numberToGuess) should be(Success(false))
    isAnswerValid(lowerAnswer, numberToGuess) should be(Success(false))
    isAnswerLowerThanNumberToGuess(null, numberToGuess) should be a 'Failure
  }

  it should "collect answer and analyse it" in {
    implicit val ioUtil = mock[IoUtil]

    val answerId = Success(1)
    val numberToGuess = 2
    val correctinput = numberToGuess
    val wrongInupt = numberToGuess + 1
    val failureInput = null

    when(ioUtil.askUser()).thenReturn(Success(numberToGuess))

    collectAnswerAndAnalyseIt(answerId, Success(NumberToGuess(correctinput))) should be(Success(true))
    collectAnswerAndAnalyseIt(answerId, Success(NumberToGuess(wrongInupt))) should be(Success(false))
    collectAnswerAndAnalyseIt(answerId, failureInput) should be a 'Failure
  }

}