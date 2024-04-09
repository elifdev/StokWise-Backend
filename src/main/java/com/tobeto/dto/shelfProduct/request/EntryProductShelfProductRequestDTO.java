package com.tobeto.dto.shelfProduct.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryProductShelfProductRequestDTO {

	private int productId;
	private int count;

}