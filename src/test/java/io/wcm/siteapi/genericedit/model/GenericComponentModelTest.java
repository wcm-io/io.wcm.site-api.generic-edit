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

import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;
import static io.wcm.siteapi.genericedit.testcontext.ComponentAssertionUtil.assertSimpleProperties;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.wcm.api.Page;

import io.wcm.siteapi.genericedit.component.GenericComponent;
import io.wcm.siteapi.genericedit.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class GenericComponentModelTest {

  private AemContext context = AppAemContext.newAemContext();

  private Page page;

  @BeforeEach
  void setUp() throws Exception {
    page = context.create().page("/content/site1/page1");
  }

  @Test
  @SuppressWarnings("null")
  void testTitle() {
    context.currentResource(context.create().resource(page, "title",
        JCR_TITLE, "My Title",
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/title/v3/title"));

    GenericComponentModel underTest = context.request().adaptTo(GenericComponentModel.class);

    assertNotNull(underTest);

    GenericComponent component = underTest.getComponent();
    assertNotNull(component);

    assertSimpleProperties(component,
        "text", "My Title",
        "linkDisabled", false);
  }

}
