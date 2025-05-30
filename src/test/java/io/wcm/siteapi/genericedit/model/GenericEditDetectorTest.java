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
package io.wcm.siteapi.genericedit.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.siteapi.genericedit.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class GenericEditDetectorTest {

  private AemContext context = AppAemContext.newAemContext();

  @BeforeEach
  void setUp() throws Exception {
    context.currentPage(context.create().page("/content/site1/page1"));
  }

  @Test
  @SuppressWarnings("null")
  void testWithoutSelector() {
    GenericEditDetector underTest = context.request().adaptTo(GenericEditDetector.class);
    assertFalse(underTest.isGenericEdit());
  }

  @Test
  @SuppressWarnings("null")
  void testWithSelector() {
    context.requestPathInfo().setSelectorString("generic-edit");

    GenericEditDetector underTest = context.request().adaptTo(GenericEditDetector.class);
    assertTrue(underTest.isGenericEdit());
  }

}
