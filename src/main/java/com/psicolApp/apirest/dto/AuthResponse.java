package com.psicolApp.apirest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Getters y setters
@Builder //contruir Objs
@AllArgsConstructor 
@NoArgsConstructor
public class AuthResponse {
    String token;

}