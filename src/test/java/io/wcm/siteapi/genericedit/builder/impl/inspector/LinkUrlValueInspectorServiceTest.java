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
package io.wcm.siteapi.genericedit.builder.impl.inspector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.siteapi.genericedit.component.value.LinkValue;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class LinkUrlValueInspectorServiceTest {

  private AemContext context = new AemContext();

  private LinkUrlValueInspectorService underTest;

  @BeforeEach
  void setUp() {
    underTest = context.registerInjectActivateService(LinkUrlValueInspectorService.class);
  }

  @Test
  void testValid() {
    assertValid("https://wcm.io");
    assertValid("https://wcm.io/page1/page2.html");
    assertValid("/content/dam/folder1/image.jpg");
  }

  @Test
  void testInvalid() {
    assertInvalid("wurstbrot");
    assertInvalid("https://wcm.io/page1/${template}.html");
    assertInvalid("/content/dam/folder1");
    assertInvalid("/content/dam/folder1/image{.width}.jpg");
    assertInvalid(null);
  }

  private void assertValid(@Nullable String value) {
    LinkValue result = underTest.inspectValue("text", value, new Object());
    assertNotNull(result, "LinkValue not null");
    assertTrue(result.isValid(), "LinkValue is valid");
    assertEquals(value, result.getUrl(), "LinkValue URL");
  }

  private void assertInvalid(@Nullable String value) {
    LinkValue result = underTest.inspectValue("text", value, new Object());
    assertNull(result, "LinkValue null");
  }

}
