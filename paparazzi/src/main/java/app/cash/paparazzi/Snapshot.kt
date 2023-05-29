/*
 * Copyright (C) 2019 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.cash.paparazzi

import dev.drewhamilton.poko.Poko
import java.io.File
import java.util.Date
import java.util.Locale

@Poko
public class Snapshot(
  public val name: String?,
  public val testName: TestName,
  public val timestamp: Date,
  public val tags: List<String> = listOf(),
  public val file: String? = null
) {
  public fun copy(
    name: String? = this.name,
    testName: TestName = this.testName,
    timestamp: Date = this.timestamp,
    tags: List<String> = this.tags,
    file: String? = this.file
  ): Snapshot = Snapshot(name, testName, timestamp, tags, file)
}

internal fun Snapshot.toFileName(
  delimiter: String = "_",
  extension: String
): String {
  val formattedLabel = if (name != null) {
    "$delimiter${name.toLowerCase(Locale.US).replace("\\s".toRegex(), delimiter)}"
  } else {
    ""
  }
  return "${testName.packageName}${delimiter}${testName.className}${delimiter}${testName.methodName}$formattedLabel.$extension"
}

internal fun Snapshot.goldenFile(goldenImagesDirectory: File, frame: Int? = null): File {
  return if (frame == null) {
    File(goldenImagesDirectory, toFileName("_", "png"))
  } else {
    val name = if (name == null) "$frame" else "$name $frame"
    File(goldenImagesDirectory, copy(name = name).toFileName("_", "png"))
  }
}
