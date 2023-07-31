package com.borymskyi.exchangeratesapp.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * This class provides the ability to create an instance that contains the
 * exception information for the client. This is an implementation of RFC 7808.
 *
 * @author Dmytrii Borymskyi
 *
 * @version 1.0
 *
 * @see <a href="https://datatracker.ietf.org/doc/html/rfc7807">Problem Details for HTTP APIs</a>
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@RequiredArgsConstructor
public class ResponseException {

    String title;

    int status;

    String detail;

    //URI reference of the occurrence
    String instance;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime time = LocalDateTime.now();
}
