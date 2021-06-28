package com.skenario.assignment.dto;

import lombok.*;
import org.reactivestreams.Publisher;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T> {

    @Singular("data")
    private Iterable<T> data = new ArrayList<>();
    @Builder.Default
    private int code = 200;

}
