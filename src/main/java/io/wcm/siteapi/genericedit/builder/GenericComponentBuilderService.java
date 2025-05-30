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
package io.wcm.siteapi.genericedit.builder;

import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.siteapi.genericedit.component.GenericComponent;

/**
 * Builds a generic view of a Sling Model instance reflecting all properties found in it's interface.
 * Special properties are detected via {@link ValueInspectorService} implementations and mapped to generic
 * link, media, rich text and component values - or simple values for all other properties.
 */
@ProviderType
public interface GenericComponentBuilderService {

  /**
   * Build generic view of model instance.
   * @param modelInstance Model instance
   * @param resource Related resource
   * @return Generic component
   */
  @NotNull
  GenericComponent build(@NotNull Object modelInstance, @NotNull Resource resource);

}
