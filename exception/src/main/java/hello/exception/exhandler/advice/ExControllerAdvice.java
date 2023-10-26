package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ControllerAdvice
 * - 예외 처리 관심사를 분리
 * - 적용 대상 컨트롤러를 지정하지 않으면 모든 컨트롤러에 적용 됨 (글로벌 적용)
 */

@Slf4j
@RestControllerAdvice(basePackages = "hello.exception.api") // @ControllerAdvice + @ResponseBody
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST) // Status 미지정시 200 상태 코드가 반환 되므로 상태 코드를 재지정
    @ExceptionHandler(IllegalArgumentException.class) // 예외를 잡아서 정상 처리를 진행함
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler // (UserException.class) 생략 가능 - 메서드 파라미터로 전달 되는 예외를 사용
    public ResponseEntity<ErrorResult> userExHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST); // 프로그래밍해서 동적으로 상태 코드 변경 가능
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) { // 위에서 처리 되지 못한 Error는 모두 여기서 처리 됨
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

}
