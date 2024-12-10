package tn.enova.Filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UserIdFilter extends HttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String method = request.getMethod();
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
            String userId = request.getHeader("auth-user-id");
            if (userId != null) {
                request.setAttribute("auth-user-id", userId);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing header auth-user-id ");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}