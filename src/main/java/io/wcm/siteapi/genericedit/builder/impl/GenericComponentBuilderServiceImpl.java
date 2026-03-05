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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import org.apache.sling.api.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.FieldOption;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.adobe.cq.export.json.ComponentExporter;

import io.wcm.siteapi.genericedit.builder.ComponentValidatorService;
import io.wcm.siteapi.genericedit.builder.GenericComponentBuilderService;
import io.wcm.siteapi.genericedit.builder.ValueInspectorService;
import io.wcm.siteapi.genericedit.builder.impl.util.PatternList;
import io.wcm.siteapi.genericedit.builder.impl.util.PropertyIntrospector;
import io.wcm.siteapi.genericedit.builder.impl.util.PropertyNameBeautifier;
import io.wcm.siteapi.genericedit.component.GenericComponent;
import io.wcm.sling.commons.caservice.ContextAwareServiceCollectionResolver;
import io.wcm.sling.commons.caservice.ContextAwareServiceResolver;

/**
 * Maps a sling model instance to generic component view.
 */
@Component(service = GenericComponentBuilderService.class, immediate = true)
@Designate(ocd = GenericComponentBuilderServiceImpl.Config.class)
public class GenericComponentBuilderServiceImpl implements GenericComponentBuilderService {

  @ObjectClassDefinition(
      name = "wcm.io Site API Generic Edit Component Builder Service",
      description = "Configures the process of building component views in generic edit mode.")
  @interface Config {

    @AttributeDefinition(
        name = "Ignore Property Patterns",
        description = "List of regular expressions to match property names that should be ignored.")
    String[] ignorePropertyPatterns() default {
        "^:.+$",
        "^(id|uuid)$",
        "^elementsOrder$",
        "^appliedCssClassNames$",
        "^richText$",
        "wcmio:(link|media)Valid"
    };

    @AttributeDefinition(
        name = "Beautify Property Names",
        description = "Converts property names in a better human-readable format.")
    boolean beautifyPropertyNames() default true;

  }

  @Reference
  private ContextAwareServiceResolver serviceResolver;

  @Reference(cardinality = ReferenceCardinality.MULTIPLE, fieldOption = FieldOption.UPDATE,
      policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY)
  private SortedSet<ServiceReference<ValueInspectorService<?>>> inspectors = new ConcurrentSkipListSet<>(Collections.reverseOrder());
  private ContextAwareServiceCollectionResolver<ValueInspectorService<?>, Void> inspectorResolver;

  @Reference(cardinality = ReferenceCardinality.MULTIPLE, fieldOption = FieldOption.UPDATE,
      policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY)
  private SortedSet<ServiceReference<ComponentValidatorService>> validators = new ConcurrentSkipListSet<>(Collections.reverseOrder());
  private ContextAwareServiceCollectionResolver<ComponentValidatorService, Void> validatorResolver;

  private PatternList ignorePropertyPatterns;
  private boolean beautifyPropertyNames;

  @Activate
  private void activate(Config config) {
    this.inspectorResolver = serviceResolver.getCollectionResolver(this.inspectors);
    this.validatorResolver = serviceResolver.getCollectionResolver(this.validators);
    this.ignorePropertyPatterns = new PatternList(config.ignorePropertyPatterns());
    this.beautifyPropertyNames = config.beautifyPropertyNames();
  }

  @Deactivate
  private void deactivate() {
    this.inspectorResolver.close();
    this.validatorResolver.close();
  }

  @Override
  public @NotNull GenericComponent build(@NotNull Object modelInstance, @NotNull Resource resource) {
    PropertyIntrospector propertyIntrospector = PropertyIntrospector.from(modelInstance);
    Map<String, List<Object>> rawProperties = propertyIntrospector.getPropertiesMap();

    List<GenericPropertyImpl<?>> properties = rawProperties.entrySet().stream()
      .filter(entry -> !ignorePropertyPatterns.matches(entry.getKey()))
      .map(entry -> processProperty(entry.getKey(), entry.getValue(), modelInstance, resource))
      .filter(property -> !property.getValues().isEmpty())
      .collect(Collectors.toList());

    if (this.beautifyPropertyNames) {
      for (GenericPropertyImpl<?> property : properties) {
        property.setLabel(PropertyNameBeautifier.apply(property.getName()));
      }
    }

    // check if any component validator detects an invalid component - otherwise assume validness
    boolean isValid = validatorResolver.resolveAll(resource)
      .map(validator -> validator.isValid(modelInstance))
      .filter(valid -> !valid)
      .findFirst().orElse(true);

    return new GenericComponentImpl(modelInstance, properties, isValid);
  }

  /**
   * Processes a raw property and converts it into a properly typed generic property.
   * @param key Property name
   * @param rawValues Property raw values
   * @param modelInstance Model instance
   * @param resource Related resource
   * @return Generic property
   */
  @SuppressWarnings({
      "null", "unchecked",
      "java:S3740" // ignore usage of generic type for return value
  })
  private GenericPropertyImpl<?> processProperty(@NotNull String key, @NotNull List<Object> rawValues,
      @NotNull Object modelInstance, @NotNull Resource resource) {

    // check all inspectors if given value is a special value
    List<?> values = inspectorResolver.resolveAll(resource)
      .map(inspector -> inspector.inspect(key, rawValues, modelInstance))
      .filter(resultValues -> !resultValues.isEmpty())
      .findFirst().orElse(List.of());

    // no matching inspector found, check for nested components, otherwise transform to simple values
    if (values.isEmpty()) {
      boolean isNestedComponents = rawValues.stream().allMatch(ComponentExporter.class::isInstance);
      if (isNestedComponents) {
        values = rawValues.stream()
          .map(instance -> new ComponentValueImpl(build(instance, resource)))
          .collect(Collectors.toList());
      }
      else {
        values = rawValues.stream()
          .map(SimpleValueImpl::new)
          .collect(Collectors.toList());
      }
    }

    return new GenericPropertyImpl(key, values);
  }

}
