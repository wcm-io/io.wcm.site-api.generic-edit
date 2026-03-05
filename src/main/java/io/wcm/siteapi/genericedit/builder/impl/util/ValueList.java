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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Converts a value which is a collection or array to a list of values.
 * Otherwise a list with the single value is returned.
 * Null values are filtered out.
 */
public final class ValueList {

  private final List<Object> values;

  private ValueList(@NotNull List<Object> values) {
    this.values = values;
  }

  /**
   * @return List of values
   */
  public @NotNull List<Object> get() {
    return Collections.unmodifiableList(values);
  }

  /**
   * @param rawValue Value which is either a single value, or a collection or array
   */
  @SuppressWarnings("unchecked")
  public static ValueList from(@Nullable Object rawValue) {
    List<Object> values = new ArrayList<>();
    if (rawValue != null) {
      if (rawValue instanceof Collection) {
        Collection<Object> coll = (Collection)rawValue;
        coll.stream()
          .filter(Objects::nonNull)
          .forEach(values::add);
      }
      else if (rawValue.getClass().isArray()) {
        int length = Array.getLength(rawValue);
        for (int i = 0; i < length; i++) {
          Object item = Array.get(rawValue, i);
          if (item != null) {
            values.add(item);
          }
        }
      }
      else {
        values.add(rawValue);
      }
    }
    return new ValueList(values);
  }

}
