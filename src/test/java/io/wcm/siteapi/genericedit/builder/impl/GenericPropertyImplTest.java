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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.wcm.siteapi.genericedit.component.value.SimpleValue;

class GenericPropertyImplTest {

  @Test
  @SuppressWarnings("null")
  void testSingleValue() {
    GenericPropertyImpl<SimpleValue> underTest = new GenericPropertyImpl<SimpleValue>("prop1",
        List.of(new SimpleValueImpl("value1")));

    assertEquals("prop1", underTest.getName());
    assertEquals("prop1", underTest.getLabel());
    assertTrue(underTest.isSingleValue());
    assertEquals("value1", underTest.getValue().getStringValue());
    assertEquals(1, underTest.getValues().size());
    assertTrue(underTest.isValid());
    assertTrue(underTest.is(SimpleValue.class));
  }

  @Test
  @SuppressWarnings("null")
  void testSingleValueWithLabel() {
    GenericPropertyImpl<SimpleValue> underTest = new GenericPropertyImpl<SimpleValue>("prop1",
        List.of(new SimpleValueImpl("value1")));
    underTest.setLabel("label1");

    assertEquals("prop1", underTest.getName());
    assertEquals("label1", underTest.getLabel());
  }

  @Test
  @SuppressWarnings("null")
  void testMultipleValues() {
    GenericPropertyImpl<SimpleValue> underTest = new GenericPropertyImpl<SimpleValue>("prop1",
        List.of(new SimpleValueImpl("value1"), new SimpleValueImpl("value2")));

    assertEquals("prop1", underTest.getName());
    assertEquals("prop1", underTest.getLabel());
    assertFalse(underTest.isSingleValue());
    assertEquals("value1", underTest.getValue().getStringValue());
    assertEquals(2, underTest.getValues().size());
    assertTrue(underTest.isValid());
    assertTrue(underTest.is(SimpleValue.class));
  }

  @Test
  void testNoValue() {
    GenericPropertyImpl<SimpleValue> underTest = new GenericPropertyImpl<SimpleValue>("prop1", List.of());

    assertEquals("prop1", underTest.getName());
    assertEquals("prop1", underTest.getLabel());
    assertFalse(underTest.isSingleValue());
    assertNull(underTest.getValue());
    assertEquals(0, underTest.getValues().size());
    assertFalse(underTest.isValid());
    assertFalse(underTest.is(SimpleValue.class));
  }

}
