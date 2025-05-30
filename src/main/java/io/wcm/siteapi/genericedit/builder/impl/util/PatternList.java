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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * List of regex patterns to match against.
 */
public class PatternList {

  private static final Logger log = LoggerFactory.getLogger(PatternList.class);

  private final List<Pattern> patterns;

  /**
   * @param patterns Compiled patterns
   */
  public PatternList(Pattern @NotNull... patterns) {
    this.patterns = Arrays.asList(patterns);
  }

  /**
   * @param patternStrings Pattern strings. Invalid patterns are ignored (with warning).
   */
  public PatternList(String @NotNull... patternStrings) {
    this.patterns = compilePatterns(patternStrings);
  }

  /**
   * @param value Value to match against patterns
   * @return true if at least one pattern matches
   */
  public boolean matches(@NotNull String value) {
    return patterns.stream().anyMatch(pattern -> pattern.matcher(value).matches());
  }

  /**
   * Compiles all regular expressions.
   * @param patternStrings Pattern strings
   * @return Compiled regular expressions
   */
  private static @NotNull List<Pattern> compilePatterns(@NotNull String[] patternStrings) {
    List<Pattern> patterns = new ArrayList<>();
    for (String patternString : patternStrings) {
      try {
        patterns.add(Pattern.compile(patternString));
      }
      catch (PatternSyntaxException ex) {
        log.warn("Ignoring invalid regular expression '{}': {}", patternString, ex.getMessage());
      }
    }
    return patterns;
  }

}
