package com.tobeto.dto.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductShelfResponseDTO {
	private String name;
	private String category;
	private int count;
}
