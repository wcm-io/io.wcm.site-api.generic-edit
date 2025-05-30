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

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.factory.ModelFactory;
import org.jetbrains.annotations.Nullable;
import org.osgi.annotation.versioning.ProviderType;

import com.adobe.cq.export.json.ComponentExporter;

import io.wcm.siteapi.genericedit.builder.GenericComponentBuilderService;
import io.wcm.siteapi.genericedit.component.GenericComponent;

/**
 * Gets a generic view of the Sling Model attached to the component to be edited in Generic Edit Mode.
 */
@Model(adaptables = SlingHttpServletRequest.class)
@ProviderType
public class GenericComponentModel {

  @SlingObject
  private SlingHttpServletRequest request;
  @SlingObject
  private Resource resource;
  @OSGiService
  private ModelFactory modelFactory;
  @OSGiService
  private GenericComponentBuilderService componentBuilder;

  private GenericComponent component;

  @PostConstruct
  private void init() {
    ComponentExporter model = ModelFromRequest.createModelInstance(request, modelFactory);
    if (model != null) {
      component = componentBuilder.build(model, resource);
    }
  }

  /**
   * @return Component
   */
  public @Nullable GenericComponent getComponent() {
    return this.component;
  }

}
