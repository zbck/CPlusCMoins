


import CPlusCMoinsMain._

import org.mockito.MockitoSugar._
import org.scalatest._

import scala.util.{Failure, Success, Try}

class CPlusCMoinMainSpec extends FlatSpec with Matchers with ApplicationConfForSpec{

  "CPlusCMoinMain" should "run in nominal case" in {
    implicit val ioUtil = mock[IoUtil]

    val answerId = Success(1)
    val numberToGuess = 2
    val correctinput = numberToGuess
    val higherInupt = numberToGuess + 1
    val lowerInput = numberToGuess - 1

    when(ioUtil.askUser()).thenReturn(Success(higherInupt),
      Success(lowerInput),
      Success(correctinput))

    gameLoop(answerId, Success(NumberToGuess(numberToGuess))) should be(Success(()))
    gameLoop(Failure(null), Success(NumberToGuess(numberToGuess))) should be a 'Failure
  }

}