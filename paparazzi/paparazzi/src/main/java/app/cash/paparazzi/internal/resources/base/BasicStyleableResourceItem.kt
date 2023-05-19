/*
 * Copyright (C) 2023 Square, Inc.
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
package app.cash.paparazzi.internal.resources.base

import app.cash.paparazzi.internal.resources.ResourceSourceFile
import com.android.ide.common.rendering.api.AttrResourceValue
import com.android.ide.common.rendering.api.StyleableResourceValue
import com.android.ide.common.resources.ResourceRepository
import com.android.resources.ResourceType
import com.android.resources.ResourceVisibility

/**
 * Ported from: [BasicStyleableResourceItem.java](https://cs.android.com/android-studio/platform/tools/base/+/18047faf69512736b8ddb1f6a6785f58d47c893f:resource-repository/main/java/com/android/resources/base/BasicStyleableResourceItem.java)
 *
 * Resource item representing a styleable resource.
 */
class BasicStyleableResourceItem(
  name: String,
  sourceFile: ResourceSourceFile,
  visibility: ResourceVisibility,
  attrs: List<AttrResourceValue>
) : BasicValueResourceItemBase(ResourceType.STYLEABLE, name, sourceFile, visibility), StyleableResourceValue {
  private val attrs: List<AttrResourceValue> = attrs.toList()

  override fun getAllAttributes(): List<AttrResourceValue> = attrs

  override fun equals(obj: Any?): Boolean {
    if (this === obj) return true
    if (!super.equals(obj)) return false
    val other = obj as BasicStyleableResourceItem
    return attrs == other.attrs
  }

  companion object {
    /**
     * For an attr reference that doesn't contain formats tries to find an attr definition the reference is pointing to.
     * If such attr definition belongs to this resource repository and has the same description and group name as
     * the attr reference, returns the attr definition. Otherwise returns the attr reference passed as the parameter.
     */
    fun getCanonicalAttr(attr: AttrResourceValue, repository: ResourceRepository): AttrResourceValue {
      if (attr.formats.isEmpty()) {
        val items = repository.getResources(attr.namespace, ResourceType.ATTR, attr.name)
        val item = items.filterIsInstance<AttrResourceValue>().find { it.description == attr.description && it.groupName == attr.groupName }
        if (item != null) {
          return item
        }
      }
      return attr
    }
  }
}
