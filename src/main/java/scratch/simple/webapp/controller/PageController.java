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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author Karl Bennett
 */
@Controller
public class PageController {

    @RequestMapping
    public ModelAndView view(HttpServletRequest request) throws IOException {
        return new ModelAndView("page").addObject("request", toString(request));
    }

    private static String toString(HttpServletRequest request) throws IOException {

        final StringBuilder builder = new StringBuilder();

        appendMethod(builder, request);
        appendHeaders(builder, request);
        appendBody(builder, request);

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
