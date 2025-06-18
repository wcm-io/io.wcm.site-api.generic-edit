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
package io.wcm.siteapi.genericedit.builder.impl.util;

import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 * Detects all properties of given class and returns a Map with the values for each property.
 * Properties marked with Jackson JSON ignore annotations are skipped.
 */
public final class PropertyIntrospector {

  private final Map<String, List<Object>> propertiesMap;

  private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder().build();
  private static final JavaType MAP_TYPE = OBJECT_MAPPER.getTypeFactory()
      .constructMapType(Map.class, String.class, Object.class);

  private static final Set<String> DEFAULT_IGNORE_METHODS = Set.of("toString", "hashCode", "getClass");
  private static final List<String> DEFAULT_GETTER_PREFIXES = List.of("get", "is");

  private static final Logger log = LoggerFactory.getLogger(PropertyIntrospector.class);

  private PropertyIntrospector(@NotNull Map<String, List<Object>> propertiesMap) {
    this.propertiesMap = propertiesMap;
  }

  /**
   * @return Map with property names and value list for each property.
   */
  public Map<String, List<Object>> getPropertiesMap() {
    return this.propertiesMap;
  }

  /**
   * Gets allowed property names by mapping it using Jackson and get the resulting list of properties.
   * @param instance Instance
   * @return Non-ignored property names
   */
  private static Set<String> getAllowedPropertyNames(@NotNull Object instance) {
    Map<String, Object> map = OBJECT_MAPPER.convertValue(instance, MAP_TYPE);
    return map.keySet();
  }

  /**
   * @return Get properties from sling model via map. The map contains a list of values per key.
   */
  private static @NotNull Map<String, List<Object>> buildPropertiesMap(@NotNull Object instance,
      @NotNull Set<String> allowedProperties) {
    Class<?> clazz = instance.getClass();
    List<Method> getterMethods = Stream.of(clazz.getMethods())
        .filter(method -> method.getParameterTypes().length == 0 && method.getReturnType() != void.class)
        .filter(method -> !DEFAULT_IGNORE_METHODS.contains(method.getName()))
        .collect(Collectors.toList());
    Map<String, List<Object>> result = new TreeMap<>();
    for (Method method : getterMethods) {
      String propertyName = toPropertyName(method, clazz);
      if (allowedProperties.contains(propertyName)) {
        try {
          Object value = method.invoke(instance);
          if (value != null) {
            ValueList valueList = ValueList.from(value);
            result.put(propertyName, valueList.get());
          }
        }
        catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException ex) {
          log.warn("Unable to introspect {}#{}", instance.getClass().getName(), method.getName(), ex);
        }
      }
    }
    return result;
  }

  /**
   * Converts method name to property name. If method has a JsonProperty defined, that is returned.
   * @param method Method
   * @param clazz Class
   * @return Property name
   */
  private static @NotNull String toPropertyName(@NotNull Method method, Class<?> clazz) {
    String name = getJsonPropertyNameAllDeclarations(method, clazz);
    if (name == null) {
      name = method.getName();
      for (String getterPrefix : DEFAULT_GETTER_PREFIXES) {
        if (StringUtils.startsWith(name, getterPrefix)) {
          name = Introspector.decapitalize(StringUtils.substringAfter(name, getterPrefix));
        }
      }
    }
    return name;
  }

  /**
   * Checks for JsonProperty annotation in given method, and all related super classes/interface which
   * implement the same method and may have the annotation set.
   * @param method Method
   * @param clazz Class
   * @return Property name is an annotation was found
   */
  private static @Nullable String getJsonPropertyNameAllDeclarations(@NotNull Method method, Class<?> clazz) {
    for (Class<?> checkClass : getAllTypes(clazz)) {
      try {
        Method checkMethod = checkClass.getDeclaredMethod(method.getName());
        String propertyName = getJsonPropertyName(checkMethod);
        if (propertyName != null) {
          return propertyName;
        }
      }
      catch (NoSuchMethodException ex) {
        // ignore
      }
    }
    return null;
  }

  /**
   * Get all classes for interfaces, interface extensions, super classes and their interfaces for this class.
   * @param clazz Class
   * @return All related classes
   */
  private static @NotNull List<Class<?>> getAllTypes(Class<?> clazz) {
    List<Class<?>> allTypes = new ArrayList<>();
    allTypes.add(clazz);
    for (Class<?> interfaze : clazz.getInterfaces()) {
      allTypes.addAll(getAllTypes(interfaze));
    }
    Class<?> superClass = clazz.getSuperclass();
    if (superClass != null && superClass != Object.class) {
      allTypes.addAll(getAllTypes(superClass));
    }
    return allTypes;
  }

  /**
   * Gets property name from JsonProperty annotation of method.
   * @param method Method
   * @return Property name or null
   */
  @SuppressWarnings("null")
  private static @Nullable String getJsonPropertyName(@NotNull Method method) {
    JsonProperty jsonProperty = method.getAnnotation(JsonProperty.class);
    if (jsonProperty != null && StringUtils.isNotEmpty(jsonProperty.value())) {
      return jsonProperty.value();
    }
    return null;
  }

  /**
   * Create property introspector from instance.
   * @param instance Object instance
   * @return Property introspector
   */
  public static PropertyIntrospector from(Object instance) {
    Map<String, List<Object>> propertiesMap = buildPropertiesMap(instance, getAllowedPropertyNames(instance));
    return new PropertyIntrospector(propertiesMap);
  }

}
