/*
 * Copyright 2015 Karl Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scratch.simple.webapp.controller;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Karl Bennett
 */
@Controller
public class PageController {

    @RequestMapping
    public ModelAndView view(HttpServletRequest request) throws IOException, ExecutionException, InterruptedException {
        return new ModelAndView("page").addObject("request", toString(request));
    }

    private static String toString(HttpServletRequest request) throws IOException, ExecutionException, InterruptedException {

        final StringBuilder builder = new StringBuilder();

        appendMethod(builder, request);
        appendHeaders(builder, request);
        appendBody(builder, request);

        final PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor());
        connManager.setDefaultMaxPerRoute(2000);
        connManager.setMaxTotal(2000);
        final CloseableHttpAsyncClient httpclient = HttpAsyncClients.createPipelining(connManager);
        httpclient.start();

        final List<Future<HttpResponse>> futures = new ArrayList<Future<HttpResponse>>(100);
        for (int i = 0; i < 2000; i++) {
            futures.add(httpclient.execute(new HttpGet("http://localhost:8181/rest/users"), null));
            System.out.println("Made request: " + i);
        }

        for (int i = 0; i < 2000; i++) {
            futures.get(i).get();
            System.out.println("Finished request: " + i);
        }

        httpclient.close();

        return builder.toString();
    }

    private static void appendMethod(StringBuilder builder, HttpServletRequest request) {
        builder.append(request.getMethod()).append(" ").append(request.getRequestURI()).append('\n');
    }

    private static void appendHeaders(StringBuilder builder, HttpServletRequest request) {

        final Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {

            final String headerName = headerNames.nextElement();

            builder.append(headerName).append(": ");
            appendValues(builder, request.getHeaders(headerName));
        }
    }

    private static void appendValues(StringBuilder builder, Enumeration<String> values) {

        while (values.hasMoreElements()) {
            final String value = values.nextElement();
            builder.append(value).append(", ");
        }

        builder.append('\n');
    }

    private static void appendBody(StringBuilder builder, HttpServletRequest request) throws IOException {
        builder.append('\n');
        builder.append(IOUtils.toString(request.getInputStream()));
    }


}
