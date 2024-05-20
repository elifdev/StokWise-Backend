package com.tobeto.dto.category.response;

import java.util.UUID;

import lombok.Data;

@Data
public class GetAllCategoriesResponseDTO {
	private UUID id;
	private String name;
}
