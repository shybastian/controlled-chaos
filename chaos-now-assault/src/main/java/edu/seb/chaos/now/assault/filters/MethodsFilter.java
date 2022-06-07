/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.seb.chaos.now.assault.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.StaticMethodMatcher;

import java.lang.reflect.Method;

@Slf4j
public class MethodsFilter extends StaticMethodMatcher {

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        log.info("Applied Advice on method: {}, from class: {}", method.getName(), targetClass.getName());
        String name = method.getName();
        return !name.matches("init") && !name.matches("destroy");
    }

    public static MethodsFilter INSTANCE = new MethodsFilter();
}
