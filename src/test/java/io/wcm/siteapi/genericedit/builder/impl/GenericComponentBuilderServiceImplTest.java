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

import static com.adobe.cq.wcm.core.components.commons.link.Link.PN_LINK_TARGET;
import static com.adobe.cq.wcm.core.components.commons.link.Link.PN_LINK_URL;
import static com.adobe.cq.wcm.core.components.models.ExperienceFragment.PN_FRAGMENT_VARIATION_PATH;
import static com.adobe.cq.wcm.core.components.models.Teaser.NN_ACTIONS;
import static com.adobe.cq.wcm.core.components.models.Teaser.PN_ACTIONS_ENABLED;
import static com.adobe.cq.wcm.core.components.models.Teaser.PN_ACTION_LINK;
import static com.adobe.cq.wcm.core.components.models.Teaser.PN_ACTION_TEXT;
import static com.adobe.cq.wcm.core.components.models.Teaser.PN_DESCRIPTION_FROM_PAGE;
import static com.adobe.cq.wcm.core.components.models.Teaser.PN_TITLE_FROM_PAGE;
import static com.adobe.cq.xf.ExperienceFragmentsConstants.PN_XF_VARIANT_TYPE;
import static com.day.cq.commons.DownloadResource.PN_REFERENCE;
import static com.day.cq.commons.jcr.JcrConstants.JCR_DESCRIPTION;
import static com.day.cq.commons.jcr.JcrConstants.JCR_PRIMARYTYPE;
import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;
import static com.day.cq.wcm.api.NameConstants.NT_COMPONENT;
import static io.wcm.siteapi.genericedit.testcontext.ComponentAssertionUtil.assertComponentProperties;
import static io.wcm.siteapi.genericedit.testcontext.ComponentAssertionUtil.assertLinkProperties;
import static io.wcm.siteapi.genericedit.testcontext.ComponentAssertionUtil.assertMediaProperties;
import static io.wcm.siteapi.genericedit.testcontext.ComponentAssertionUtil.assertRichTextProperties;
import static io.wcm.siteapi.genericedit.testcontext.ComponentAssertionUtil.assertSimpleProperties;
import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.Strings;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.adobe.cq.wcm.core.components.models.Download;
import com.adobe.cq.wcm.core.components.models.ExperienceFragment;
import com.adobe.cq.wcm.core.components.models.Teaser;
import com.adobe.cq.wcm.core.components.models.Title;
import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.msm.api.LiveRelationshipManager;

import io.wcm.siteapi.genericedit.builder.ComponentValidatorService;
import io.wcm.siteapi.genericedit.builder.GenericComponentBuilderService;
import io.wcm.siteapi.genericedit.component.GenericComponent;
import io.wcm.siteapi.genericedit.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
@ExtendWith(MockitoExtension.class)
class GenericComponentBuilderServiceImplTest {

  private AemContext context = AppAemContext.newAemContext();

  private GenericComponentBuilderService underTest;
  private Page page;

  @Mock
  private LiveRelationshipManager liveRelationshipManager;

  @BeforeEach
  void setUp() throws Exception {
    context.registerService(LiveRelationshipManager.class, liveRelationshipManager);

    underTest = context.getService(GenericComponentBuilderService.class);
    page = context.create().page("/content/site1/page1");
  }

  @Test
  @SuppressWarnings("null")
  void testTitle() {
    Resource resource = context.currentResource(context.create().resource(page, "title",
        JCR_TITLE, "My Title",
        PN_LINK_URL, "https://myhost",
        PN_LINK_TARGET, "_blank",
        "type", "h2",
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/title/v3/title"));
    Title title = context.request().adaptTo(Title.class);

    GenericComponent component = underTest.build(title, resource);

    assertSimpleProperties(component,
        "text", "My Title",
        "type", "h2",
        "linkDisabled", false);
    assertLinkProperties(component,
        "link", Map.of("href", "https://myhost", "target", "_blank"));
    assertMediaProperties(component);
    assertRichTextProperties(component);
    assertComponentProperties(component);

    assertEquals(4, component.getAllProperties().size());
    assertTrue(component.isValid());

    assertFalse(component.isContainer());
    assertFalse(component.isExperienceFragment());
  }

  @Test
  @SuppressWarnings("null")
  void testTeaserWithImageAndSingleLink() {
    Asset asset = context.create().asset("/content/dam/test.jpg", 10, 10, "image/jpeg");
    context.create().resource("/libs/core/wcm/components/teaser/v2/teaser",
        JCR_PRIMARYTYPE, NT_COMPONENT,
        "imageDelegate", "core/wcm/components/image/v3/image");
    Resource resource = context.currentResource(context.create().resource(page, "teaser",
        JCR_TITLE, "Teaser Title",
        JCR_DESCRIPTION, "<p>description</p>",
        PN_LINK_URL, "https://myhost",
        PN_LINK_TARGET, "_blank",
        PN_REFERENCE, asset.getPath(),
        PN_TITLE_FROM_PAGE, false,
        PN_DESCRIPTION_FROM_PAGE, false,
        PN_ACTIONS_ENABLED, false,
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/teaser/v2/teaser"));
    Teaser teaser = context.request().adaptTo(Teaser.class);

    GenericComponent component = underTest.build(teaser, resource);

    assertSimpleProperties(component,
        "title", "Teaser Title",
        "actionsEnabled", false,
        "imageLinkHidden", false,
        "titleLinkHidden", false);
    assertLinkProperties(component,
        "link", Map.of("href", "https://myhost", "target", "_blank"));
    assertMediaProperties(component,
        "imagePath", "/content/site1/page1/_jcr_content/teaser.coreimg.jpeg");
    assertRichTextProperties(component,
        "description", "<p>description</p>");
    assertComponentProperties(component);

    assertTrue(component.isValid());
  }

  @Test
  @SuppressWarnings("null")
  void testTeaserWithMultipleActionLinks() {
    context.create().resource("/libs/core/wcm/components/teaser/v2/teaser",
        JCR_PRIMARYTYPE, NT_COMPONENT,
        "imageDelegate", "core/wcm/components/image/v3/image");
    Resource resource = context.currentResource(context.create().resource(page, "teaser",
        JCR_TITLE, "Teaser Title",
        PN_ACTIONS_ENABLED, true,
        PN_TITLE_FROM_PAGE, false,
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/teaser/v2/teaser"));
    context.create().resource(resource, NN_ACTIONS + "/action1",
        PN_ACTION_TEXT, "Action 1",
        PN_ACTION_LINK, "https://myhost");
    context.create().resource(resource, NN_ACTIONS + "/action2",
        PN_ACTION_TEXT, "Action 2",
        PN_ACTION_LINK, page.getPath());
    Teaser teaser = context.request().adaptTo(Teaser.class);

    GenericComponent component = underTest.build(teaser, resource);

    assertSimpleProperties(component,
        "title", "Teaser Title",
        "actionsEnabled", true,
        "imageLinkHidden", false,
        "titleLinkHidden", false);
    assertLinkProperties(component);
    assertMediaProperties(component);
    assertRichTextProperties(component);
    assertComponentProperties(component,
        "actions", List.<Consumer<GenericComponent>>of(
            (item) -> {
              assertSimpleProperties(item, "title", "Action 1");
              assertLinkProperties(item,
                  "link", Map.of("href", "https://myhost"));
            },
            (item) -> {
              assertSimpleProperties(item, "title", "Action 2");
              assertLinkProperties(item,
                  "link", Map.of("href", "/content/site1/page1.html"));
            }));

    assertTrue(component.isValid());
  }

  @Test
  @SuppressWarnings("null")
  void testDownload() {
    Asset asset = context.create().asset("/content/dam/test.jpg", 10, 10, "image/jpeg");
    Resource resource = context.currentResource(context.create().resource(page, "download",
        JCR_TITLE, "My Download",
        PN_REFERENCE, asset.getPath(),
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/download/v2/download"));
    Download download = context.request().adaptTo(Download.class);

    GenericComponent component = underTest.build(download, resource);

    assertSimpleProperties(component,
        "title", "My Download",
        "filename", "test.jpg",
        "extension", "jpg",
        "size", "633 bytes");
    assertLinkProperties(component,
        "url", Map.of("href", "/content/dam/test.jpg.coredownload.jpg", "target", "_blank"));
    assertMediaProperties(component);
    assertRichTextProperties(component);
    assertComponentProperties(component);

    assertTrue(component.isValid());
  }

  @Test
  @SuppressWarnings("null")
  void testExperienceFragment_valid() {
    Page xfPage = context.create().page("/content/experience-fragments/sample", null,
        PN_XF_VARIANT_TYPE, "web");
    context.create().resource(xfPage, "root");

    Resource resource = context.currentResource(context.create().resource(page, "experienceFragment",
        PN_FRAGMENT_VARIATION_PATH, "/content/experience-fragments/sample",
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/experiencefragment/v2/experiencefragment"));
    ExperienceFragment experienceFragment = context.request().adaptTo(ExperienceFragment.class);

    GenericComponent component = underTest.build(experienceFragment, resource);

    assertSimpleProperties(component,
        "classNames", "aem-xf",
        "configured", true,
        "localizedFragmentVariationPath", "/content/experience-fragments/sample/jcr:content");
    assertLinkProperties(component);
    assertMediaProperties(component);
    assertRichTextProperties(component);
    assertComponentProperties(component);

    assertTrue(component.isValid());
  }

  @Test
  @SuppressWarnings("null")
  void testExperienceFragment_invalid() {
    Resource resource = context.currentResource(context.create().resource(page, "experienceFragment",
        PN_FRAGMENT_VARIATION_PATH, "/content/experience-fragments/invalid",
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/experiencefragment/v2/experiencefragment"));
    ExperienceFragment experienceFragment = context.request().adaptTo(ExperienceFragment.class);

    GenericComponent component = underTest.build(experienceFragment, resource);

    assertSimpleProperties(component,
        "classNames", "aem-xf empty",
        "configured", false);
    assertLinkProperties(component);
    assertMediaProperties(component);
    assertRichTextProperties(component);
    assertComponentProperties(component);

    assertFalse(component.isValid());
  }

  @Test
  @SuppressWarnings("null")
  void testValidation_Valid() {
    context.registerService(ComponentValidatorService.class,
        modelInstance -> Strings.CS.equals(((Title)modelInstance).getText(), "Valid"));

    Resource resource = context.currentResource(context.create().resource(page, "title",
        JCR_TITLE, "Valid",
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/title/v3/title"));
    Title title = context.request().adaptTo(Title.class);

    GenericComponent component = underTest.build(title, resource);
    assertTrue(component.isValid());
  }

  @Test
  @SuppressWarnings("null")
  void testValidation_Invalid() {
    // register three serves, two of them always return true
    context.registerService(ComponentValidatorService.class,
        modelInstance -> true);
    context.registerService(ComponentValidatorService.class,
        modelInstance -> Strings.CS.equals(((Title)modelInstance).getText(), "Valid"));
    context.registerService(ComponentValidatorService.class,
        modelInstance -> true);

    Resource resource = context.currentResource(context.create().resource(page, "title",
        JCR_TITLE, "Invalid",
        PROPERTY_RESOURCE_TYPE, "core/wcm/components/title/v3/title"));
    Title title = context.request().adaptTo(Title.class);

    GenericComponent component = underTest.build(title, resource);
    assertFalse(component.isValid());
  }

}
