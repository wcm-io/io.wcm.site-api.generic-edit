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

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.wcm.siteapi.genericedit.component.value.SimpleValue;

class SimpleValueImpl implements SimpleValue {

  private final Object value;

  SimpleValueImpl(@NotNull Object value) {
    this.value = value;
  }

  @Override
  public @NotNull Object getValue() {
    return this.value;
  }

  @Override
  public boolean isString() {
    return (value instanceof String) && StringUtils.isNotBlank((String)value);
  }

  @Override
  public @Nullable String getStringValue() {
    if (isString()) {
      return (String)value;
    }
    return null;
  }

  @Override
  public boolean isNumber() {
    return (value instanceof Number);
  }

  @Override
  public @Nullable Number getNumberValue() {
    if (isNumber()) {
      return (Number)value;
    }
    return null;
  }

  @Override
  public boolean isBoolean() {
    return (value instanceof Boolean);
  }

  @Override
  public @Nullable Boolean getBooleanValue() {
    if (isBoolean()) {
      return (Boolean)value;
    }
    return null;
  }

  @Override
  public boolean isDate() {
    return (value instanceof Date) || (value instanceof Calendar);
  }

  @Override
  public @Nullable Date getDateValue() {
    if (value instanceof Date) {
      return (Date)value;
    }
    else if (value instanceof Calendar) {
      return ((Calendar)value).getTime();
    }
    return null;
  }

  @Override
  public boolean isValid() {
    return isString() || isNumber() || isBoolean() || isDate();
  }

  @Override
  public String toString() {
    return value.toString();
  }

}
