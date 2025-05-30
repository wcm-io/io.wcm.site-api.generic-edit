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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.service.component.annotations.Component;

import com.adobe.cq.wcm.core.components.commons.link.Link;

import io.wcm.siteapi.genericedit.builder.ValueInspectorService;
import io.wcm.siteapi.genericedit.component.value.LinkValue;

/**
 * Detects Core Components link references.
 */
@Component(service = ValueInspectorService.class)
public class CoreComponentLinkValueInspectorService extends ValueInspectorService<LinkValue> {

  @Override
  protected @Nullable LinkValue inspectValue(@NotNull String key, @Nullable Object rawValue,
      @NotNull Object instance) {
    if (rawValue instanceof Link) {
      return new CoreComponentLinkValueImpl((Link)rawValue);
    }
    return null;
  }

}
