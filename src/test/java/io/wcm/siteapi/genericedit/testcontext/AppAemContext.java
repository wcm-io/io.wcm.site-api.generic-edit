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
package io.wcm.siteapi.genericedit.testcontext;

import static com.adobe.cq.wcm.core.components.testing.mock.ContextPlugins.CORE_COMPONENTS;
import static io.wcm.testing.mock.wcmio.sling.ContextPlugins.WCMIO_SLING;
import static org.apache.sling.testing.mock.caconfig.ContextPlugins.CACONFIG;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;

import io.wcm.siteapi.genericedit.GenericEditConfig;
import io.wcm.siteapi.genericedit.builder.impl.GenericComponentBuilderServiceImpl;
import io.wcm.siteapi.genericedit.builder.impl.inspector.CoreComponentLinkValueInspectorService;
import io.wcm.siteapi.genericedit.builder.impl.inspector.CoreComponentMediaValueInspectorService;
import io.wcm.siteapi.genericedit.builder.impl.inspector.LinkUrlValueInspectorService;
import io.wcm.siteapi.genericedit.builder.impl.inspector.RichTextValueInspectorService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextCallback;
import io.wcm.testing.mock.wcmio.caconfig.MockCAConfig;

/**
 * Sets up {@link AemContext} for unit tests.
 */
public final class AppAemContext {

  private AppAemContext() {
    // static methods only
  }

  /**
   * @return {@link AemContext}
   */
  public static AemContext newAemContext() {
    return new AemContextBuilder()
      .plugin(CACONFIG, CORE_COMPONENTS, WCMIO_SLING)
      .afterSetUp(SETUP_CALLBACK)
      .build();
  }

  /**
   * Custom set up rules required in all unit tests.
   */
  private static final AemContextCallback SETUP_CALLBACK = new AemContextCallback() {

    @Override
    public void execute(@NotNull AemContext context) throws IOException {

      // context path strategy
      MockCAConfig.contextPathStrategyAbsoluteParent(context, 1, 2, 3);

      // register services
      context.registerInjectActivateService(GenericEditConfig.class);
      context.registerInjectActivateService(CoreComponentLinkValueInspectorService.class);
      context.registerInjectActivateService(CoreComponentMediaValueInspectorService.class);
      context.registerInjectActivateService(LinkUrlValueInspectorService.class);
      context.registerInjectActivateService(RichTextValueInspectorService.class);
      context.registerInjectActivateService(GenericComponentBuilderServiceImpl.class);
    }
  };

}
