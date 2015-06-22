/*
 * Copyright (C) 2015  Karl Bennett
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

        final String requestString = toString(request);

        System.out.println(requestString);

        return new ModelAndView("page").addObject("request", requestString);
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
