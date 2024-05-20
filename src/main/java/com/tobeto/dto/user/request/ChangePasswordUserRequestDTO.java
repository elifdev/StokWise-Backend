package com.tobeto.dto.user.request;

import lombok.Data;

@Data
public class ChangePasswordUserRequestDTO {
	private String oldPassword;
	private String newPassword;
}
