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
package io.wcm.siteapi.genericedit.testcontext;

import static org.apache.sling.testing.mock.osgi.MapUtil.toMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import io.wcm.siteapi.genericedit.builder.impl.util.ValueList;
import io.wcm.siteapi.genericedit.component.GenericComponent;
import io.wcm.siteapi.genericedit.component.GenericProperty;
import io.wcm.siteapi.genericedit.component.value.GenericValue;

public final class ComponentAssertionUtil {

  private ComponentAssertionUtil() {
    // static methods only
  }

  public static void assertSimpleProperties(GenericComponent component, Object... expectedValues) {
    assertProperties("simple", component.getSimpleProperties(), expectedValues,
        (propertyName, expectedValue, actualValue) -> {
          if (expectedValue instanceof String) {
            assertTrue(actualValue.isString(), "value for '" + propertyName + "' is not a string: " + actualValue);
            assertEquals(expectedValue, actualValue.getStringValue(), "simple string value for '" + propertyName + "'");
          }
          else if (expectedValue instanceof Number) {
            assertTrue(actualValue.isNumber(), "value for '" + propertyName + "' is not a number: " + actualValue);
            assertEquals(expectedValue, actualValue.getNumberValue(), "simple number value for '" + propertyName + "'");
          }
          else if (expectedValue instanceof Boolean) {
            assertTrue(actualValue.isBoolean(), "value for '" + propertyName + "' is not a boolean: " + actualValue);
            assertEquals(expectedValue, actualValue.getBooleanValue(), "simple boolean value for '" + propertyName + "'");
          }
          else if (expectedValue instanceof Date) {
            assertTrue(actualValue.isDate(), "value for '" + propertyName + "' is not a date: " + actualValue);
            assertEquals(expectedValue, actualValue.getDateValue(), "simple date value for '" + propertyName + "'");
          }
          else {
            throw new RuntimeException("Unexpected expected value: " + expectedValue);
          }
        });
  }

  @SuppressWarnings("unchecked")
  public static void assertLinkProperties(GenericComponent component, Object... expectedValues) {
    assertProperties("link", component.getLinkProperties(), expectedValues,
        (propertyName, expectedValue, actualValue) -> {
          if (expectedValue instanceof String) {
            assertEquals(expectedValue, actualValue.getUrl(), "url for '" + propertyName + "'");
          }
          else if (expectedValue instanceof Map) {
            Map<String, Object> htmlAttributes = (Map)expectedValue;
            assertEquals(htmlAttributes.get("href"), actualValue.getUrl(), "link url for '" + propertyName + "'");
            assertEquals(htmlAttributes, actualValue.getHtmlAttributes(), "link html attributes for '" + propertyName + "'");
          }
          else {
            throw new RuntimeException("Unexpected expected value: " + expectedValue);
          }
        });
  }

  public static void assertMediaProperties(GenericComponent component, Object... expectedValues) {
    assertProperties("media", component.getMediaProperties(), expectedValues,
        (propertyName, expectedValue, actualValue) -> {
          if (expectedValue instanceof String) {
            assertEquals(expectedValue, actualValue.getUrl(), "media url for '" + propertyName + "'");
            assertNotNull(actualValue.getMarkup(), "media markup for '" + propertyName + "'");
          }
          else {
            throw new RuntimeException("Unexpected expected value: " + expectedValue);
          }
        });
  }

  public static void assertRichTextProperties(GenericComponent component, Object... expectedValues) {
    assertProperties("richtext", component.getRichTextProperties(), expectedValues,
        (propertyName, expectedValue, actualValue) -> {
          if (expectedValue instanceof String) {
            assertEquals(expectedValue, actualValue.getText(), "rich text for '" + propertyName + "'");
          }
          else {
            throw new RuntimeException("Unexpected expected value: " + expectedValue);
          }
        });
  }

  @SuppressWarnings("unchecked")
  public static void assertComponentProperties(GenericComponent component, Object... expectedValues) {
    assertProperties("component", component.getComponentProperties(), expectedValues,
        (propertyName, expectedValue, actualValue) -> {
          if (expectedValue instanceof Consumer) {
            ((Consumer)expectedValue).accept(actualValue.getInstance());
          }
          else {
            throw new RuntimeException("Unexpected expected value: " + expectedValue);
          }
        });
  }

  private static <T extends GenericValue> void assertProperties(
      String propertyType, List<GenericProperty<T>> properties, Object[] expectedValues,
      TriConsumer<String, Object, T> propertyNameExpectedAndActualValueConsumer) {

    Map<String, Object> expectedValuesMap = toMap(expectedValues);
    SortedSet<String> expectedPropertyNames = new TreeSet<>(expectedValuesMap.keySet());
    SortedSet<String> actualPropertyNames = properties.stream()
      .map(GenericProperty::getName)
      .collect(Collectors.toCollection(TreeSet::new));
    assertEquals(expectedPropertyNames, actualPropertyNames, propertyType + " properties");

    for (Map.Entry<String, Object> expectedValueEntry : expectedValuesMap.entrySet()) {
      String propertyName = expectedValueEntry.getKey();
      GenericProperty<T> property = properties.stream()
        .filter(item -> StringUtils.equals(item.getName(), propertyName))
        .findFirst().orElse(null);
      assertNotNull(property, propertyType + " property '" + propertyName + "' not found.");
      assertTrue(property.isValid(), propertyType + " property '" + propertyName + "' is not valid.");

      List<Object> expectedValueList = ValueList.from(expectedValueEntry.getValue()).get();
      @Nullable
      List<T> actualValueList = property.getValues();
      assertEquals(expectedValueList.size(), actualValueList.size(),
          "Different number of values for " + propertyType + " property '" + propertyName + "'");
      for (int i = 0; i < expectedValueList.size(); i++) {
        StringBuilder itemName = new StringBuilder(propertyName);
        if (expectedValueList.size() > 1) {
          itemName.append("[").append(Integer.toString(i)).append("]");
        }
        propertyNameExpectedAndActualValueConsumer.accept(itemName.toString(),
            expectedValueList.get(i), actualValueList.get(i));
      }
    }
  }


  @FunctionalInterface
  private interface TriConsumer<T, U, V> {

    void accept(T t, U u, V v);
  }

}
