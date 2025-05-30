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
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.siteapi.genericedit.GenericEditConfig;
import io.wcm.sling.commons.request.RequestPath;

/**
 * Checks if the generic edit mode is active.
 */
@Model(adaptables = SlingHttpServletRequest.class)
@ProviderType
public class GenericEditDetector {

  @SlingObject
  private SlingHttpServletRequest request;
  @OSGiService
  private GenericEditConfig genericEditConfig;

  private boolean genericEdit;

  @PostConstruct
  private void activate() {
    this.genericEdit = RequestPath.hasSelector(request, genericEditConfig.getSelector());
  }

  /**
   * @return true if generic edit mode is active
   */
  public boolean isGenericEdit() {
    return this.genericEdit;
  }

}
