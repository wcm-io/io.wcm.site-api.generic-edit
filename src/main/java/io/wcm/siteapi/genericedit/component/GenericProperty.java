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
package io.wcm.siteapi.genericedit.component;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.siteapi.genericedit.component.value.GenericValue;

/**
 * Represents a property. The value can be a single value, or a list of values.
 * @param <T> Property value type
 */
@ProviderType
public interface GenericProperty<T extends GenericValue> {

  /**
   * @return Property name
   */
  @NotNull
  String getName();

  /**
   * @return Property label
   */
  @NotNull
  String getLabel();

  /**
   * @return Returns true if property has only one value.
   */
  boolean isSingleValue();

  /**
   * @return Single value (first value).
   */
  @Nullable
  T getValue();

  /**
   * @return List of values
   */
  @NotNull
  List<T> getValues();

  /**
   * @return true if property contains at least one valid value
   */
  boolean isValid();

}
