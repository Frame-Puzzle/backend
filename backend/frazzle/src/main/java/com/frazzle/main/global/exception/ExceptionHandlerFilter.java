package com.frazzle.main.global.exception;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
//요청 한번마다 수행
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            setErrorResponse(e.getErrorCode().getHttpStatus(), response, e);
        } catch (Exception e) {
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex) throws IOException {
        log.error("[ExceptionHandlerFilter] errMsg : "+ ex.getMessage());

        response.setStatus(status.value());
        response.setContentType("application/json; charset-UTF-8");

        response.getWriter().write(
                new ErrorResponse(ex.getMessage())
                        .convertToJson()
        );
    }
}
