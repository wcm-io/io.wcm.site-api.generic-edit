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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PropertyNameBeautifierTest {

  @Test
  void testBeautifyPropertyName() {
    assertEquals("Link", PropertyNameBeautifier.apply("link"));
    assertEquals("Prop 12", PropertyNameBeautifier.apply("prop12"));
    assertEquals("Headless Camel Case", PropertyNameBeautifier.apply("headlessCamelCase"));
    assertEquals("Link URL", PropertyNameBeautifier.apply("wcmio:linkURL"));
    assertEquals("Already Beautified", PropertyNameBeautifier.apply("Already Beautified"));
  }

}
