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

import static com.adobe.cq.wcm.core.components.commons.link.Link.PN_LINK_TARGET;
import static com.adobe.cq.wcm.core.components.commons.link.Link.PN_LINK_URL;
import static com.adobe.cq.wcm.core.components.models.Download.PN_ACTION_TEXT;
import static com.adobe.cq.wcm.core.components.models.Teaser.PN_TITLE_FROM_PAGE;
import static com.day.cq.commons.DownloadResource.PN_REFERENCE;
import static com.day.cq.commons.jcr.JcrConstants.JCR_DESCRIPTION;
import static com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE;
import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;
import static com.day.cq.wcm.api.NameConstants.NT_COMPONENT;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.cq.wcm.core.components.models.Download;
import com.adobe.cq.wcm.core.components.models.Teaser;
import com.adobe.cq.wcm.core.components.models.Title;
import com.day.cq.wcm.api.Page;

import io.wcm.siteapi.genericedit.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class PropertyIntrospectorTest {

  private AemContext context = AppAemContext.newAemContext();

  private Page page;

  @BeforeEach
  void setUp() {
    page = context.create().page("/content/site1/page1");
  }

  @Test
  @SuppressWarnings("null")
  void testTitleComponent() {
    context.currentResource(context.create().resource(page, "title",
        JCR_TITLE, "My Title",
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/title/v3/title"));

    Title title = context.request().adaptTo(Title.class);

    Map<String, List<Object>> properties = PropertyIntrospector.from(title).getPropertiesMap();

    assertEquals(Map.of(
        ":type", List.of("core/wcm/components/title/v3/title"),
        "text", List.of("My Title"),
        "linkDisabled", List.of(false),
        "id", List.of("title-3edee7add1")), properties);
  }

  @Test
  @SuppressWarnings("null")
  void testTeaserComponent() {
    context.create().resource("/libs/core/wcm/components/teaser/v2/teaser",
        JCR_PRIMARYTYPE, NT_COMPONENT);
    context.currentResource(context.create().resource(page, "teaser",
        JCR_TITLE, "Teaser Title",
        PN_LINK_URL, "https://myhost",
        PN_LINK_TARGET, "_blank",
        PN_TITLE_FROM_PAGE, false,
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/teaser/v2/teaser"));
    Teaser teaser = context.request().adaptTo(Teaser.class);

    Map<String, List<Object>> properties = PropertyIntrospector.from(teaser).getPropertiesMap();

    assertEquals(new TreeMap<>(Map.of(
        ":type", List.of("core/wcm/components/teaser/v2/teaser"),
        "actions", List.of(),
        "actionsEnabled", List.of(true),
        "id", List.of("teaser-40311d8650"),
        "imageLinkHidden", List.of(false),
        "link", List.of(teaser.getLink()),
        "title", List.of("Teaser Title"),
        "titleLinkHidden", List.of(false))), properties);
  }

  @Test
  @SuppressWarnings("null")
  void testDownloadComponent() throws IOException {
    try (InputStream is = new ByteArrayInputStream(new byte[] {
        0x01, 0x02, 0x03
    })) {
      context.create().asset("/content/dam/sample.pdf", is, "application/pdf");
    }

    context.create().resource("/libs/core/wcm/components/download/v2/download",
        JCR_PRIMARYTYPE, NT_COMPONENT);
    context.currentResource(context.create().resource(page, "download",
        PN_ACTION_TEXT, "Download Title",
        JCR_DESCRIPTION, "<p>Download</p>",
        PN_REFERENCE, "/content/dam/sample.pdf",
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/download/v2/download"));
    Download teaser = context.request().adaptTo(Download.class);

    Map<String, List<Object>> properties = PropertyIntrospector.from(teaser).getPropertiesMap();

    assertEquals(new TreeMap<>(Map.of(
        ":type", List.of("core/wcm/components/download/v2/download"),
        "actionText", List.of("Download Title"),
        "description", List.of("<p>Download</p>"),
        "extension", List.of("pdf"),
        "filename", List.of("sample.pdf"),
        "format", List.of(""),
        "size", List.of("3 bytes"),
        "url", List.of("/content/dam/sample.pdf.coredownload.pdf"),
        "id", List.of("download-82407088ff"))), properties);
  }

}
