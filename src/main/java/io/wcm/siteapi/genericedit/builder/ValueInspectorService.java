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
package io.wcm.siteapi.genericedit.builder;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ConsumerType;

import io.wcm.siteapi.genericedit.component.value.GenericValue;
import io.wcm.sling.commons.caservice.ContextAwareService;

/**
 * Allows to customize the inspection process which turns properties in Sling Models instance
 * to specific ones e.g. for links, media and rich text.
 */
@ConsumerType
public abstract class ValueInspectorService<T extends GenericValue> implements ContextAwareService {

  /**
   * Inspect the value of given property to check if a specific value representation can be extracted.
   * @param key Property key
   * @param rawValues Raw property values
   * @param instance Instance property is read from for further inspection
   * @return List of matching values that are found. Returns empty list if not value was found.
   */
  @NotNull
  @SuppressWarnings("null")
  public List<T> inspect(@NotNull String key, @NotNull List<Object> rawValues,
      @NotNull Object instance) {
    return rawValues.stream()
      .map(rawValue -> inspectValue(key, rawValue, instance))
      .filter(Objects::nonNull)
      .collect(Collectors.toList());
  }

  /**
   * Inspects raw value and tries to convert it to the target type.
   * @param key Property key
   * @param rawValue Raw property value
   * @param instance Instance property is read from for further inspection
   * @return Target type instance or null
   */
  protected abstract @Nullable T inspectValue(@NotNull String key, @NotNull Object rawValue,
      @NotNull Object instance);

}
