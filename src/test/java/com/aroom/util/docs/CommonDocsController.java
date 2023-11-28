package com.aroom.util.docs;

import com.aroom.global.response.ApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Api 에서 에러 관련을 고지할 목적으로 만든 API 입니다. 실제 API로는 제공이 되지 않습니다.
 */
@RestController
class CommonDocsController {

    @PostMapping("/common/docs")
    public ApiResponse<Void> sample(@RequestBody @Valid SampleRequest request) {
        return new ApiResponse<>(LocalDateTime.now(), "공백이어서는 안됩니다.", null);
    }

    public record SampleRequest(
        @NotBlank(message = "공백이어서는 안됩니다.")
        @JsonProperty("name")
        String name,
        @Email(message = "이메일 양식을 다시 확인해주세요")
        @JsonProperty("email")
        String email
    ) {
    }
}
