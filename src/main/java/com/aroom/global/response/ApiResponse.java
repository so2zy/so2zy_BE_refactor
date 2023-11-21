package com.aroom.global.response;

import java.time.LocalDateTime;

/**
 * 성공 실패 메시지 모두 일관성 있는 포맷으로 제공하기 위해서 ApiResponse 로 감싸 데이터를 전달합니다.
 * @param timeStamp 응답 일자
 * @param detail    특이사항 메시지
 * @param data      응답 데이터
 * @param <T>       응답 데이터 타입
 */
public record ApiResponse<T>(
    LocalDateTime timeStamp,
    String detail,
    T data) {

}
