package dev.yuri.addresses_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErroDto {
    private final String mensagem;
    private final int status;
}
