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

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.factory.InvalidAdaptableException;
import org.apache.sling.models.factory.InvalidModelException;
import org.apache.sling.models.factory.MissingElementsException;
import org.apache.sling.models.factory.ModelClassException;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.models.factory.ValidationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;

final class ModelFromRequest {

  private static final Logger log = LoggerFactory.getLogger(ModelFromRequest.class);

  private ModelFromRequest() {
    // static method only
  }

  /**
   * Checks if the model that is associated with the current request implements the {@link ComponentExporter} interface.
   * @param request Request
   * @param modelFactory Model factory
   * @return Model instance associated with the current request (if it implements {@link ComponentExporter}).
   *         Otherwise null.
   */
  static @Nullable ComponentExporter createModelInstance(@NotNull SlingHttpServletRequest request, @NotNull ModelFactory modelFactory) {
    try {
      Object instance = modelFactory.getModelFromRequest(request);
      if (instance instanceof ComponentExporter) {
        return (ComponentExporter)instance;
      }
      return null;
    }
    catch (MissingElementsException | InvalidAdaptableException | ModelClassException | ValidationException | InvalidModelException ex) {
      log.warn("Unable to get model associated with current request for: {}", request.getResource().getPath(), ex);
      return null;
    }
  }

}
