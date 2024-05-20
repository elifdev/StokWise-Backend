package com.tobeto.dto.signup.request;

import java.util.List;

import com.tobeto.dto.role.response.RoleResponseDTO;

import lombok.Data;

@Data
public class SignupRequestDTO {
	private String email;
	private String password;
	private List<RoleResponseDTO> roles;
}
