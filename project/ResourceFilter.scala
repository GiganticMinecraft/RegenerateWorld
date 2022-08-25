// This file is
// * licensed under GPL v3 (https://github.com/GiganticMinecraft/SeichiAssist/blob/2e2c33af7138b3f0f6137245ba389c7a98f92f23/LICENSE)
// * written in SeichiAssist (https://github.com/GiganticMinecraft/SeichiAssist/blob/dfff893d92cfed7c6c6cd241af060b698c0afe0b/project/ResourceFilter.scala)

import java.io._
import scala.io.Source

object ResourceFilter {
  def replaceTokens(out: File, file: File, map: Map[String, String]): File = {
    out.getParentFile.mkdirs()

    val input = new FileInputStream(file)
    val output = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out))))

    Source.fromInputStream(input).getLines().foreach { line =>
      val replaced =
        map.foldLeft(line) { case (acc, (key, value)) =>
          acc.replaceAll(s"@$key@", value)
        }

      output.println(replaced)
    }

    output.flush()
    output.close()

    out
  }

  import sbt.io.syntax._

  def filterResources(pathsRelativeToBase: Seq[String],
                      replacementMap: Map[String, String],
                      outDirectory: File,
                      resourceDirectory: File): Seq[File] =
    pathsRelativeToBase
      .map { relativeToBase =>
        val outFile = outDirectory / relativeToBase
        val file = resourceDirectory / relativeToBase
        replaceTokens(outFile, file, replacementMap)
      }
}