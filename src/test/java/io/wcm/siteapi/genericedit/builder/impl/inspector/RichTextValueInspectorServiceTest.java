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

import io.wcm.siteapi.genericedit.component.value.RichTextValue;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class RichTextValueInspectorServiceTest {

  private AemContext context = new AemContext();

  private RichTextValueInspectorService underTest;

  @BeforeEach
  void setUp() {
    underTest = context.registerInjectActivateService(RichTextValueInspectorService.class);
  }

  @Test
  void testValid() {
    assertValid("<p>Some text</p>");
    assertValid(" <p>Some<br/>\ntext</p>  ");
    assertValid("\n\n<p>Some \n<a href=\"https://myhost\">text</a></p>\n\n");
  }

  @Test
  void testInvalid() {
    assertInvalid("Some text");
    assertInvalid("<p>Some text");
    assertInvalid("");
    assertInvalid(null);
  }

  private void assertValid(@Nullable String value) {
    RichTextValue result = underTest.inspectValue("text", value, new Object());
    assertNotNull(result, "RichTextValue not null");
    assertTrue(result.isValid(), "RichTextValue is valid");
    assertEquals(value, result.getText(), "RichTextValue text");
  }

  private void assertInvalid(@Nullable String value) {
    RichTextValue result = underTest.inspectValue("text", value, new Object());
    assertNull(result, "RichTextValue null");
  }

}
