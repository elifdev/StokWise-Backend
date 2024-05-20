package com.tobeto.dto.shelf.response;

import java.util.List;
import java.util.UUID;

import com.tobeto.dto.product.response.ProductShelfResponseDTO;

import lombok.Data;

@Data
public class ShelfProductResponseDTO {
	private UUID id;
	private int capacity;
	private List<ProductShelfResponseDTO> products;
}