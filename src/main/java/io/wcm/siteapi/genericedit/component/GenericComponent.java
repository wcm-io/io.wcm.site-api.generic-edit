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
package io.wcm.siteapi.genericedit.component;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.osgi.annotation.versioning.ProviderType;

import io.wcm.siteapi.genericedit.component.value.ComponentValue;
import io.wcm.siteapi.genericedit.component.value.GenericValue;
import io.wcm.siteapi.genericedit.component.value.LinkValue;
import io.wcm.siteapi.genericedit.component.value.MediaValue;
import io.wcm.siteapi.genericedit.component.value.RichTextValue;
import io.wcm.siteapi.genericedit.component.value.SimpleValue;

/**
 * Generic view of all properties of a sling model instance.
 */
@ProviderType
public interface GenericComponent {

  /**
   * @return All simple properties
   */
  @NotNull
  List<GenericProperty<SimpleValue>> getSimpleProperties();

  /**
   * @return All link properties
   */
  @NotNull
  List<GenericProperty<LinkValue>> getLinkProperties();

  /**
   * @return All media properties
   */
  @NotNull
  List<GenericProperty<MediaValue>> getMediaProperties();

  /**
   * @return All rich text properties
   */
  @NotNull
  List<GenericProperty<RichTextValue>> getRichTextProperties();

  /**
   * @return All nested component properties
   */
  @NotNull
  List<GenericProperty<ComponentValue>> getComponentProperties();

  /**
   * @return All properties
   */
  @NotNull
  List<GenericProperty<GenericValue>> getAllProperties();

  /**
   * @return Component is a container component with child components
   */
  boolean isContainer();

  /**
   * @return Component is a experience fragment
   */
  boolean isExperienceFragment();

  /**
   * @return Component contains at least one valid property (of any type)
   */
  boolean isValid();

}
