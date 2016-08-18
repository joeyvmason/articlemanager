package com.joeyvmason.articlemanager.web.domain;

import com.joeyvmason.articlemanager.core.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
class GlobalControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ErrorResponse handleException(HttpServletRequest request, Exception e) {
        logger.warn("Unhandled exception when hitting url {}", getFullURL(request), e);
        return new ErrorResponse("Error occurred. Try again later or contact support@joeyvmason.com.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody ErrorResponse handleNotFoundException(HttpServletRequest request, NotFoundException e) {
        logger.warn("Not found exception when hitting url {}", getFullURL(request), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    @ExceptionHandler(UnauthorizedException.class)
    public @ResponseBody ErrorResponse handleUnauthorizedException(HttpServletRequest request, UnauthorizedException e) {
        if (request.getRequestURI().equalsIgnoreCase("/auth/me")) {
            logger.debug("User request for authorization status (/auth/me) resulted in UNAUTHORIZED");
        } else {
            logger.debug("Request failed because user not authorized to access url {}", getFullURL(request));
        }

        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    @ExceptionHandler(ForbiddenException.class)
    public @ResponseBody ErrorResponse handleForbiddenException(HttpServletRequest request, ForbiddenException e) {
        logger.debug("Request failed because User({}) forbidden to access url {}", e.getUser().getId(), getFullURL(request), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody ErrorResponse handleBadRequestException(HttpServletRequest request, BadRequestException e) {
        if ("Invalid credentials".equalsIgnoreCase(e.getMessage())) {
            logger.debug("Received login request with bad credentials");
        } else {
            logger.debug("Received bad request at url {}", getFullURL(request), e);
        }

        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNoHandlerFoundException(Exception e) {
        logger.warn("No handler found", e);
        return "dist/404.html";
    }

    private String getFullURL(HttpServletRequest request) {
        try {
            StringBuffer requestURL = request.getRequestURL();
            String queryString = request.getQueryString();

            if (queryString == null) {
                return requestURL.toString();
            } else {
                return requestURL.append('?').append(queryString).toString();
            }
        } catch (Exception e) {
            logger.warn("Unable to get full URL", e);
            return null;
        }
    }
}
