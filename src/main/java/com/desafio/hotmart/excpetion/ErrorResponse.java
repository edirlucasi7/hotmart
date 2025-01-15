package com.desafio.hotmart.excpetion;

import java.time.LocalDateTime;

public record ErrorResponse(String errorMessage, int status, String reasonPhrase, String path, LocalDateTime timestamp)  { }
