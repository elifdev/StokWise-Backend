package com.tobeto.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.role.response.RoleResponseDTO;
import com.tobeto.entities.user.Role;
import com.tobeto.service.RoleService;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@Autowired
	@Qualifier("requestMapper")
	private ModelMapper requestMapper;

	@Autowired
	@Qualifier("responseMapper")
	private ModelMapper responseMapper;

	@GetMapping("/role/getAll")
	public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
		List<Role> roles = roleService.getAllRoles();
		List<RoleResponseDTO> rolesDTOs = new ArrayList<>();
		roles.forEach(r -> {
			rolesDTOs.add(responseMapper.map(r, RoleResponseDTO.class));
		});
		return ResponseEntity.ok(rolesDTOs);
	}
}
