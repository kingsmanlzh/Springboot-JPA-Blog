package com.cos.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//게터,셋터 자동 설정
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T> {//클래스에 제너릭T를 걸어주고...
	
	  int status;
	  T data;

}
