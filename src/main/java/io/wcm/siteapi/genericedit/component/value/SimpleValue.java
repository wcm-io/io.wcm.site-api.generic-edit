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
package io.wcm.siteapi.genericedit.component.value;

import java.util.Date;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * Represents a simple value of primitive type.
 */
@ConsumerType
public interface SimpleValue extends GenericValue {

  /**
   * @return Value object
   */
  @NotNull
  Object getValue();

  /**
   * @return true if value is a string
   */
  boolean isString();

  /**
   * @return String value
   */
  @Nullable
  String getStringValue();

  /**
   * @return true if value is a number
   */
  boolean isNumber();

  /**
   * @return true if value is number
   */
  @Nullable
  Number getNumberValue();

  /**
   * @return true if value is a boolean
   */
  boolean isBoolean();

  /**
   * @return Boolean value
   */
  @Nullable
  Boolean getBooleanValue();

  /**
   * @return true if value is a date (or calendar)
   */
  boolean isDate();

  /**
   * @return Date value
   */
  @Nullable
  Date getDateValue();

}
