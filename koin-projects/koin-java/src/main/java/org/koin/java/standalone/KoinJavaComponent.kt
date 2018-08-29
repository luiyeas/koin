/*
 * Copyright 2017-2018 the original author or authors.
 *
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
 */
package org.koin.java.standalone

import org.koin.core.KoinContext
import org.koin.core.parameter.ParameterDefinition
import org.koin.core.parameter.emptyParameterDefinition
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext

/**
 * Koin Java Helper - inject/get into Java code
 *
 * @author @fredy-mederos
 * @author Arnaud Giuliani
 */
object KoinJavaComponent : KoinComponent {

    /**
     * Retrieve given dependency lazily
     * @param clazz - dependency class
     * @param name - bean canonicalName / optional
     * @param module - module path / optional
     * @param parameters - dependency parameters / optional
     */
    @JvmOverloads
    @JvmStatic
    fun <T : Any> inject(
        clazz: Class<T>,
        name: String = "",
        module: String? = null,
        parameters: ParameterDefinition = emptyParameterDefinition()
    ): Lazy<T> {
        return lazy { get(clazz, name, module, parameters) }
    }

    /**
     * Retrieve given dependency
     * @param clazz - dependency class
     * @param name - bean canonicalName / optional
     * @param module - module path / optional
     * @param parameters - dependency parameters / optional
     */
    @JvmOverloads
    @JvmStatic
    fun <T : Any> get(
        clazz: Class<T>,
        name: String = "",
        module: String? = null,
        parameters: ParameterDefinition = emptyParameterDefinition()
    ): T {
        return (StandAloneContext.koinContext as KoinContext).get(
            name,
            clazz.kotlin,
            module,
            parameters
        )
    }

    /**
     * Release module instances from its path
     *
     * @param path - module's path
     */
    @JvmStatic
    fun release(path: String) {
        (StandAloneContext.koinContext as KoinContext).release(path)
    }

    /**
     * inject lazily given property
     * @param key - key property
     */
    @JvmOverloads
    @JvmStatic
    fun <T> property(key: String, defaultValue: T? = null): Lazy<T?> {
        return lazy { getProperty(key, defaultValue) }
    }

    /**
     * Retrieve given property
     * @param key - key property
     */
    @Suppress("UNCHECKED_CAST")
    @JvmOverloads
    @JvmStatic
    fun <T> getProperty(key: String, defaultValue: T? = null): T? {
        val koinContext = (StandAloneContext.koinContext as KoinContext)
        return koinContext.propertyResolver.properties[key] as T? ?: defaultValue
    }
}