package com.aroom.global.error;

import com.aroom.global.response.ApiResponse;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ApiResponse<Void>> handleApplicationException(ServiceException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
			.body(new ApiResponse<>(LocalDateTime.now(), ex.getErrorCode().getSimpleMessage(), null));
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<Void>> handleDataAccessException(DataAccessException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ApiResponse<>(LocalDateTime.now(), "알 수 없는 오류입니다. 다음에 시도해주세요.", null));
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<Void>> handleInternalServerError(RuntimeException ex) {
		log.error(ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ApiResponse<>(LocalDateTime.now(), "알 수 없는 오류입니다. 다음에 시도해주세요.", null));
	}

	@ExceptionHandler
	public ResponseEntity<ApiResponse<Void>> handleBindValidationError(BindException ex) {
		log.error(ex.getMessage(), ex);

		String bindExceptionMessage = ex.getAllErrors().stream()
			.map(DefaultMessageSourceResolvable::getDefaultMessage)
			.collect(Collectors.joining(","));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ApiResponse<>(LocalDateTime.now(), bindExceptionMessage, null));
	}
}
