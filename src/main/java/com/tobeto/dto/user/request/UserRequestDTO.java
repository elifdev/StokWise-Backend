package com.tobeto.dto.user.request;

import java.util.List;

import com.tobeto.dto.role.response.RoleResponseDTO;

import lombok.Data;

@Data
public class UserRequestDTO {
	private String email;
	private String password;
	private List<RoleResponseDTO> roles;
}
