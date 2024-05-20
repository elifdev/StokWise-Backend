package com.tobeto.dto.user.response;

import java.util.List;

import com.tobeto.dto.role.response.RoleResponseDTO;

import lombok.Data;

@Data
public class GetAllUsersResponseDTO {
	private String email;
	private List<RoleResponseDTO> roles;
}
