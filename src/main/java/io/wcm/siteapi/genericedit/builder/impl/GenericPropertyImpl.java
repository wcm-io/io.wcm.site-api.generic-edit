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
package io.wcm.siteapi.genericedit.builder.impl;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.wcm.siteapi.genericedit.component.GenericProperty;
import io.wcm.siteapi.genericedit.component.value.GenericValue;

class GenericPropertyImpl<T extends GenericValue> implements GenericProperty<T> {

  private final String name;
  private String label;
  private final List<T> values;

  GenericPropertyImpl(@NotNull String name, @NotNull List<T> values) {
    this.name = name;
    this.values = Collections.unmodifiableList(values);
  }

  @Override
  public @NotNull String getName() {
    return name;
  }

  @Override
  public @NotNull String getLabel() {
    if (this.label == null) {
      return this.name;
    }
    return this.label;
  }

  void setLabel(@NotNull String label) {
    this.label = label;
  }

  @Override
  public boolean isSingleValue() {
    return values.size() == 1;
  }

  @Override
  public @Nullable T getValue() {
    if (values.isEmpty()) {
      return null;
    }
    else {
      return values.get(0);
    }
  }

  @Override
  public @NotNull List<T> getValues() {
    return values;
  }

  @Override
  public boolean isValid() {
    return values.stream().anyMatch(GenericValue::isValid);
  }

  @SuppressWarnings("null")
  boolean is(Class<? extends GenericValue> type) {
    if (values.isEmpty()) {
      return false;
    }
    return values.stream().allMatch(item -> type.isAssignableFrom(item.getClass()));
  }

}
