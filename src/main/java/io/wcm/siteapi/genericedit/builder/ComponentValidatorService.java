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

import org.osgi.annotation.versioning.ConsumerType;

import io.wcm.siteapi.genericedit.component.GenericComponent;
import io.wcm.sling.commons.caservice.ContextAwareService;

/**
 * Allows custom validation checks for model instances.
 * By default, the valid state of a {@link GenericComponent} is evaluated by checking for any valid property
 * or a container component. Implementing this services allows to hook in additional custom checks,
 * e.g. by calling a project-specific <code>isValid()</code> method.
 */
@ConsumerType
public interface ComponentValidatorService extends ContextAwareService {

  /**
   * Checks of the given model instance is valid.
   * @param modelInstance Model instance
   * @return true if valid, false if invalid.
   */
  boolean isValid(Object modelInstance);

}
