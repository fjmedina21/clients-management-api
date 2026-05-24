package com.myCompany.middlewares;

import com.myCompany.models.apiResponses.ErrorApiResponse;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.UUID;

@Provider
@Priority(Priorities.USER)
public class GlobalErrorHandler implements ExceptionMapper<Exception> {

    private static final Logger logger = Logger.getLogger(GlobalErrorHandler.class);

    @Override
    public Response toResponse(Exception ex) {
        int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String errorType = Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();

        var error = new ErrorApiResponse(statusCode, errorType);

        String traceId = UUID.randomUUID().toString();
        String detail = "%s : %s.".formatted(traceId, ex.getMessage());


        logger.error(detail, ex);

        // Posible ampliacion: guardar el error en una base de datos o sistema de monitoreo con el traceId para facilitar la investigacion del error
        // notificar al equipo de desarrollo o soporte tecnico del error con el traceId para que puedan investigar y resolver el problema

        return Response
                .status(statusCode)
                .type(MediaType.APPLICATION_JSON)
                .entity(error)
                .build();
    }
}
