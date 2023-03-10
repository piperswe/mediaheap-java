/*
 * Copyright (C) 2006 Google Inc.
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

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.internal.Errors;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Apply this filter in web.xml above all other filters (typically), to all requests where you plan
 * to use servlet scopes. This is also needed in order to dispatch requests to injectable filters
 * and servlets:
 *
 * <pre>
 *  &lt;filter&gt;
 *    &lt;filter-name&gt;guiceFilter&lt;/filter-name&gt;
 *    &lt;filter-class&gt;<b>com.google.inject.servlet.GuiceFilter</b>&lt;/filter-class&gt;
 *  &lt;/filter&gt;
 *
 *  &lt;filter-mapping&gt;
 *    &lt;filter-name&gt;guiceFilter&lt;/filter-name&gt;
 *    &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 *  &lt;/filter-mapping&gt;
 *  </pre>
 * <p>
 * This filter must appear before every filter that makes use of Guice injection or servlet scopes
 * functionality. Typically, you will only register this filter in web.xml and register any other
 * filters (and servlets) using a {@link ServletModule}.
 *
 * @author crazybob@google.com (Bob Lee)
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class GuiceFilter implements Filter {
    static final ThreadLocal<Context> localContext = new ThreadLocal<>();
    private static final String MULTIPLE_INJECTORS_WARNING =
            "Multiple Servlet injectors detected. This is a warning "
                    + "indicating that you have more than one "
                    + GuiceFilter.class.getSimpleName()
                    + " running "
                    + "in your web application. If this is deliberate, you may safely "
                    + "ignore this message. If this is NOT deliberate however, "
                    + "your application may not work as expected.";
    private static final Logger LOGGER = Logger.getLogger(GuiceFilter.class.getName());
    static volatile FilterPipeline pipeline = new DefaultFilterPipeline();
    /**
     * Used to inject the servlets configured via {@link ServletModule}
     */
    static volatile WeakReference<ServletContext> servletContext =
            new WeakReference<ServletContext>(null);
    /**
     * We allow both the static and dynamic versions of the pipeline to exist.
     */
    private final FilterPipeline injectedPipeline;

    public GuiceFilter() {
        // Use the static FilterPipeline
        this(null);
    }

    @Inject
    GuiceFilter(FilterPipeline filterPipeline) {
        injectedPipeline = filterPipeline;
    }

    //VisibleForTesting
    @Inject
    static void setPipeline(FilterPipeline pipeline) {

        // This can happen if you create many injectors and they all have their own
        // servlet module. This is legal, caveat a small warning.
        if (GuiceFilter.pipeline instanceof ManagedFilterPipeline) {
            LOGGER.warning(MULTIPLE_INJECTORS_WARNING);
        }

        // We overwrite the default pipeline
        GuiceFilter.pipeline = pipeline;
    }

    //VisibleForTesting
    static void reset() {
        pipeline = new DefaultFilterPipeline();
        localContext.remove();
    }

    static HttpServletRequest getOriginalRequest(Key<?> key) {
        return getContext(key).getOriginalRequest();
    }

    static HttpServletRequest getRequest(Key<?> key) {
        return getContext(key).getRequest();
    }

    static HttpServletResponse getResponse(Key<?> key) {
        return getContext(key).getResponse();
    }

    static ServletContext getServletContext() {
        return servletContext.get();
    }

    private static Context getContext(Key<?> key) {
        Context context = localContext.get();
        if (context == null) {
            throw new OutOfScopeException(
                    "Cannot access scoped ["
                            + Errors.convert(key)
                            + "]. Either we are not currently inside an HTTP Servlet request, or you may"
                            + " have forgotten to apply "
                            + GuiceFilter.class.getName()
                            + " as a servlet filter for this request.");
        }
        return context;
    }

    @Override
    public void doFilter(
            final ServletRequest servletRequest,
            final ServletResponse servletResponse,
            final FilterChain filterChain)
            throws IOException, ServletException {

        final FilterPipeline filterPipeline = getFilterPipeline();

        Context previous = GuiceFilter.localContext.get();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest originalRequest =
                (previous != null) ? previous.getOriginalRequest() : request;
        try {
            RequestScoper.CloseableScope scope = new Context(originalRequest, request, response).open();
            try {
                //dispatch across the servlet pipeline, ensuring web.xml's filterchain is honored
                filterPipeline.dispatch(servletRequest, servletResponse, filterChain);
            } finally {
                scope.close();
            }
        } catch (IOException e) {
            throw e;
        } catch (ServletException e) {
            throw e;
        } catch (Exception e) {
            Throwables.propagate(e);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        final ServletContext servletContext = filterConfig.getServletContext();

        // Store servlet context in a weakreference, for injection
        GuiceFilter.servletContext = new WeakReference<>(servletContext);

        // In the default pipeline, this is a noop. However, if replaced
        // by a managed pipeline, a lazy init will be triggered the first time
        // dispatch occurs.
        FilterPipeline filterPipeline = getFilterPipeline();
        filterPipeline.initPipeline(servletContext);
    }

    @Override
    public void destroy() {

        try {
            // Destroy all registered filters & servlets in that order
            FilterPipeline filterPipeline = getFilterPipeline();
            filterPipeline.destroyPipeline();

        } finally {
            reset();
            servletContext.clear();
        }
    }

    private FilterPipeline getFilterPipeline() {
        // Prefer the injected pipeline, but fall back on the static one for web.xml users.
        return (null != injectedPipeline) ? injectedPipeline : pipeline;
    }

    static class Context implements RequestScoper {
        final HttpServletRequest originalRequest;
        final HttpServletRequest request;
        final HttpServletResponse response;

        // Synchronized to prevent two threads from using the same request
        // scope concurrently.
        final Lock lock = new ReentrantLock();

        Context(
                HttpServletRequest originalRequest,
                HttpServletRequest request,
                HttpServletResponse response) {
            this.originalRequest = originalRequest;
            this.request = request;
            this.response = response;
        }

        HttpServletRequest getOriginalRequest() {
            return originalRequest;
        }

        HttpServletRequest getRequest() {
            return request;
        }

        HttpServletResponse getResponse() {
            return response;
        }

        @Override
        public CloseableScope open() {
            lock.lock();
            final Context previous = localContext.get();
            localContext.set(this);
            return new CloseableScope() {
                @Override
                public void close() {
                    localContext.set(previous);
                    lock.unlock();
                }
            };
        }
    }
}
