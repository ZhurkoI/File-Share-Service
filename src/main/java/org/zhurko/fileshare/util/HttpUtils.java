package org.zhurko.fileshare.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpUtils {

    private static final String JSON_CONTENT_TYPE = "application/json";
    private final static String RESPONSE_MESSAGE_TEMPLATE = "{\"message\": \"%s\"}";

    public static void configureResponse(HttpServletResponse response, int code,
                                         String body) throws IOException {
        response.setStatus(code);
        response.setContentType(JSON_CONTENT_TYPE);
        response.getWriter().println(body);
    }

    public static String compileMessage(String message) {
        return String.format(RESPONSE_MESSAGE_TEMPLATE, message);
    }
}
