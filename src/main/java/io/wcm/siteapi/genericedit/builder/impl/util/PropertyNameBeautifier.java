/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2023 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.siteapi.genericedit.builder.impl.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Beautifies a property name to a better human-readable form.
 * Example: "myFirstProperty" -> "My First Property".
 */
public final class PropertyNameBeautifier {

  private PropertyNameBeautifier() {
    // static methods only
  }

  /**
   * @param name Property name
   * @return Beautified property name
   */
  public static @NotNull String apply(@NotNull String name) {
    if (StringUtils.contains(name, ":")) {
      return apply(StringUtils.substringAfter(name, ":"));
    }

    boolean lastCharLowerCase = false;
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);
      if (i == 0) {
        c = Character.toUpperCase(c);
      }
      if (Character.isUpperCase(c) || Character.isDigit(c)) {
        if (lastCharLowerCase) {
          result.append(' ');
          lastCharLowerCase = false;
        }
      }
      else if (Character.isLowerCase(c)) {
        lastCharLowerCase = true;
      }
      else if (!Character.isAlphabetic(c)) {
        lastCharLowerCase = false;
      }
      result.append(c);
    }
    return result.toString();
  }

}
