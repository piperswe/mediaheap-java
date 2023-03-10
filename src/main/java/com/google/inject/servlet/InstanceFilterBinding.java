/*
 * Copyright (C) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.inject.servlet;

import jakarta.servlet.Filter;

/**
 * A binding to a single instance of a filter.
 *
 * @author sameb@google.com
 * @since 3.0
 */
public interface InstanceFilterBinding extends ServletModuleBinding {

    /**
     * Returns the filter instance that will be used.
     */
    Filter getFilterInstance();
}
