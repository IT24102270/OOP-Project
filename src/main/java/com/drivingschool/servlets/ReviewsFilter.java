package com.drivingschool.servlets;

import com.drivingschool.util.FileHandler;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/jsp/allReviews.jsp")
public class ReviewsFilter implements Filter {
    private FileHandler reviewFileHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String reviewFilePath = filterConfig.getServletContext().getRealPath("/WEB-INF/data/reviews.txt");
        reviewFileHandler = new FileHandler(reviewFilePath);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpRequest.setAttribute("reviews", reviewFileHandler.readAllReviews());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}