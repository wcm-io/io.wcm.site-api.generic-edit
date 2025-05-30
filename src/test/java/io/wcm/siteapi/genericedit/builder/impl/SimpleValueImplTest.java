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

import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import io.wcm.siteapi.genericedit.component.value.SimpleValue;

class SimpleValueImplTest {

  @Test
  void testStringValue() {
    SimpleValue underTest = new SimpleValueImpl("myvalue");
    assertEquals("myvalue", underTest.getValue());
    assertTrue(underTest.isString());
    assertEquals("myvalue", underTest.getStringValue());
    assertTrue(underTest.isValid());

    assertFalse(underTest.isNumber());
    assertNull(underTest.getNumberValue());
    assertFalse(underTest.isBoolean());
    assertNull(underTest.getBooleanValue());
    assertFalse(underTest.isDate());
    assertNull(underTest.getDateValue());
  }

  @Test
  void testEmptyStringValue() {
    SimpleValue underTest = new SimpleValueImpl("");
    assertEquals("", underTest.getValue());
    assertFalse(underTest.isString());
    assertNull(underTest.getStringValue());
    assertFalse(underTest.isValid());
  }

  @Test
  void testLongValue() {
    SimpleValue underTest = new SimpleValueImpl(55L);
    assertEquals(55L, underTest.getValue());
    assertTrue(underTest.isNumber());
    assertEquals(55L, underTest.getNumberValue());
    assertTrue(underTest.isValid());

    assertFalse(underTest.isString());
    assertNull(underTest.getStringValue());
    assertFalse(underTest.isBoolean());
    assertNull(underTest.getBooleanValue());
    assertFalse(underTest.isDate());
    assertNull(underTest.getDateValue());
  }

  @Test
  void testDoubleValue() {
    SimpleValue underTest = new SimpleValueImpl(1.23d);
    assertEquals(1.23d, underTest.getValue());
    assertTrue(underTest.isNumber());
    assertEquals(1.23d, underTest.getNumberValue());
    assertTrue(underTest.isValid());

    assertFalse(underTest.isString());
    assertFalse(underTest.isBoolean());
    assertFalse(underTest.isDate());
  }

  @Test
  void testBooleanValue() {
    SimpleValue underTest = new SimpleValueImpl(true);
    assertEquals(true, underTest.getValue());
    assertTrue(underTest.isBoolean());
    assertEquals(true, underTest.getBooleanValue());
    assertTrue(underTest.isValid());

    assertFalse(underTest.isString());
    assertFalse(underTest.isNumber());
    assertFalse(underTest.isDate());
  }

  @Test
  void testDateValue() {
    Date date = new Date();

    SimpleValue underTest = new SimpleValueImpl(date);
    assertEquals(date, underTest.getValue());
    assertTrue(underTest.isDate());
    assertEquals(date, underTest.getDateValue());
    assertTrue(underTest.isValid());

    assertFalse(underTest.isString());
    assertFalse(underTest.isBoolean());
    assertFalse(underTest.isNumber());
  }

  @Test
  void testCalendarValue() {
    Calendar calendar = Calendar.getInstance();

    SimpleValue underTest = new SimpleValueImpl(calendar);
    assertEquals(calendar, underTest.getValue());
    assertTrue(underTest.isDate());
    assertEquals(calendar.getTime(), underTest.getDateValue());
    assertTrue(underTest.isValid());

    assertFalse(underTest.isString());
    assertFalse(underTest.isBoolean());
    assertFalse(underTest.isNumber());
  }

  @Test
  void testOtherValue() {
    Object value = new Object();
    SimpleValue underTest = new SimpleValueImpl(value);
    assertEquals(value, underTest.getValue());

    assertFalse(underTest.isValid());

    assertFalse(underTest.isString());
    assertNull(underTest.getStringValue());
    assertFalse(underTest.isNumber());
    assertNull(underTest.getNumberValue());
    assertFalse(underTest.isBoolean());
    assertNull(underTest.getBooleanValue());
    assertFalse(underTest.isDate());
    assertNull(underTest.getDateValue());
  }

}
