import scala.util.Try
import scala.io._

trait IoUtil {
  def askUser(): Try[Int]
}

object IoUtil {
  val defaultIoUtil : IoUtil = DefaultIoUtil
}

object DefaultIoUtil extends IoUtil {
  def askUser(): Try[Int] = {
    Try{StdIn.readInt()}
  }
}


