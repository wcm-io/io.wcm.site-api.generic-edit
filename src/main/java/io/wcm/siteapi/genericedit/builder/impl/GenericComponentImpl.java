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

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.adobe.cq.wcm.core.components.models.Container;
import com.adobe.cq.wcm.core.components.models.ExperienceFragment;

import io.wcm.siteapi.genericedit.component.GenericComponent;
import io.wcm.siteapi.genericedit.component.GenericProperty;
import io.wcm.siteapi.genericedit.component.value.ComponentValue;
import io.wcm.siteapi.genericedit.component.value.GenericValue;
import io.wcm.siteapi.genericedit.component.value.LinkValue;
import io.wcm.siteapi.genericedit.component.value.MediaValue;
import io.wcm.siteapi.genericedit.component.value.RichTextValue;
import io.wcm.siteapi.genericedit.component.value.SimpleValue;

class GenericComponentImpl implements GenericComponent {

  private final List<GenericPropertyImpl<?>> allProperties;
  private final List<GenericProperty<SimpleValue>> simpleProperties;
  private final List<GenericProperty<LinkValue>> linkProperties;
  private final List<GenericProperty<MediaValue>> mediaProperties;
  private final List<GenericProperty<RichTextValue>> richTextProperties;
  private final List<GenericProperty<ComponentValue>> componentProperties;
  private final boolean container;
  private final boolean experienceFragment;
  private final boolean isValid;

  @SuppressWarnings("null")
  GenericComponentImpl(@NotNull Object modelInstance, @NotNull List<GenericPropertyImpl<?>> properties, boolean isValid) {
    this.allProperties = properties;
    this.simpleProperties = filterValidProperties(properties, SimpleValue.class);
    this.linkProperties = filterValidProperties(properties, LinkValue.class);
    this.mediaProperties = filterValidProperties(properties, MediaValue.class);
    this.richTextProperties = filterValidProperties(properties, RichTextValue.class);
    this.componentProperties = filterValidProperties(properties, ComponentValue.class);
    this.container = (modelInstance instanceof Container);
    this.experienceFragment = (modelInstance instanceof ExperienceFragment);
    if (this.experienceFragment) {
      this.isValid = isValid && ((ExperienceFragment)modelInstance).isConfigured();
    }
    else {
      this.isValid = isValid;
    }
  }

  @Override
  public boolean isContainer() {
    return this.container;
  }

  @Override
  public boolean isExperienceFragment() {
    return this.experienceFragment;
  }

  @Override
  public @NotNull List<GenericProperty<SimpleValue>> getSimpleProperties() {
    return simpleProperties;
  }

  @Override
  public @NotNull List<GenericProperty<LinkValue>> getLinkProperties() {
    return linkProperties;
  }

  @Override
  public @NotNull List<GenericProperty<MediaValue>> getMediaProperties() {
    return mediaProperties;
  }

  @Override
  public @NotNull List<GenericProperty<RichTextValue>> getRichTextProperties() {
    return richTextProperties;
  }

  @Override
  public @NotNull List<GenericProperty<ComponentValue>> getComponentProperties() {
    return componentProperties;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NotNull List<GenericProperty<GenericValue>> getAllProperties() {
    return (List)allProperties;
  }

  @Override
  public boolean isValid() {
    return isValid && (isContainer() || allProperties.stream().anyMatch(GenericProperty::isValid));
  }

  @SuppressWarnings({ "null", "unchecked" })
  private static <T extends GenericValue> @NotNull List<GenericProperty<T>> filterValidProperties(
      @NotNull List<GenericPropertyImpl<?>> properties, @NotNull Class<T> type) {
    return properties.stream()
        .filter(item -> item.isValid())
        .filter(item -> item.is(type))
        .map(item -> (GenericProperty<T>)item)
        .collect(Collectors.toList());
  }

}
