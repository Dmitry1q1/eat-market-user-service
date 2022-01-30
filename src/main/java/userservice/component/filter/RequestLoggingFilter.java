package userservice.component.filter;


import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.ContentCachingResponseWrapper;
import userservice.component.filter.buffer.BufferedServletRequestWrapper;
import userservice.entity.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Scanner;

public class RequestLoggingFilter implements Filter {

    private final Logger logger = Logger.getLogger(RequestLoggingFilter.class.getName());

    private String extractRequestBody(HttpServletRequest request) {
        Scanner s;
        try {
            s = new Scanner(request.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return "";
    }

    private String extractRequestParameters(HttpServletRequest req) {
        StringBuilder pars = new StringBuilder("\n");
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            pars.append("\t").append(paramName).append(":");
            String[] paramValues = req.getParameterValues(paramName);
            int n = paramValues.length;
            for (int i = 0; i < n; i++) {
                pars.append(paramValues[i]);
                if (i != n - 1) {
                    pars.append(", ");
                }
            }
            pars.append("\n");
        }
        return pars.toString();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest wrappedRequest;
        try {
             wrappedRequest = new BufferedServletRequestWrapper((HttpServletRequest) servletRequest);
            String params = extractRequestParameters(wrappedRequest);
            String body = extractRequestBody(wrappedRequest);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info("Request from User: " + authentication.getName() + "\tRole: " +
                    wrappedRequest.getMethod() + " : " + wrappedRequest.getRequestURI() +
                    "\nParameters: " + params +
                    "\nBody: " + body);
        } catch (IOException | AuthenticationException e) {
            logger.warn("Can't logging this request\n");
            logger.warn(e.getMessage());
            wrappedRequest = (HttpServletRequest) servletRequest;
        }
        ContentCachingResponseWrapper responseCacheWrapperObject =
                new ContentCachingResponseWrapper((HttpServletResponse) servletResponse);
        filterChain.doFilter(wrappedRequest, responseCacheWrapperObject);

        byte[] responseArray = responseCacheWrapperObject.getContentAsByteArray();
        String responseStr = new String(responseArray, responseCacheWrapperObject.getCharacterEncoding())
                .replace(",", ",\n\t")
                .replace("{\"", "{\n\t\"")
                .replace("\"}", "\"\n}");
        logger.info("Response Code:" + responseCacheWrapperObject.getStatus() +
                " Type:" + responseCacheWrapperObject.getContentType() +
                "\nBody: " + responseStr +
                "\n################################################################################################\n");
        responseCacheWrapperObject.copyBodyToResponse();
    }
}
