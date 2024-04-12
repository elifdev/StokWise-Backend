package com.tobeto.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.dto.shelfProduct.response.GetAllProductsFromShelvesResponseDTO;
import com.tobeto.entities.warehouse.Category;
import com.tobeto.entities.warehouse.Product;
import com.tobeto.entities.warehouse.ShelfProduct;
import com.tobeto.exception.ServiceException;
import com.tobeto.exception.ServiceException.ERROR_CODES;
import com.tobeto.repository.warehouse.CategoryRepository;
import com.tobeto.repository.warehouse.ProductRepository;
import com.tobeto.repository.warehouse.ShelfProductRepository;
import com.tobeto.repository.warehouse.ShelfRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ShelfRepository shelfRepository;

	@Autowired
	private ShelfProductRepository shelfProductRepository;

	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public List<GetAllProductsFromShelvesResponseDTO> getAllProductsFromShelves() {
		List<Object[]> products = shelfProductRepository
				.getAllProductsFromShelves();
		List<GetAllProductsFromShelvesResponseDTO> allProducts = new ArrayList<>();

		for (Object[] product : products) {
			int productId = (int) product[0]; // Ürün ID'si
			long unitInStock = (long) product[1]; // Ürün adedi
			String name = (String) product[2]; // Ürün adı

			GetAllProductsFromShelvesResponseDTO allProduct = new GetAllProductsFromShelvesResponseDTO(
					productId, unitInStock, name);
			allProducts.add(allProduct);
		}

		return allProducts;
	}

	public Product addProduct(Product product) {
		Optional<Category> oCategory = categoryRepository
				.findById(product.getCategory().getId());
		if (oCategory.isPresent()) {
			product.setCategory(oCategory.get());
		}

		return productRepository.save(product);
	}

	public Product updateProduct(Product product) {
//		Optional<Category> oCategory = categoryRepository
//				.findById(product.getCategory().getId());
//		if(oCategory.isPresent()) {
//			product.setCategory(oCategory.get());
//		}

		Optional<Product> oProduct = productRepository
				.findById(product.getId());
		if (oProduct.isPresent()) {
			product.setCategory(oProduct.get().getCategory());
		}
		return productRepository.save(product);
	}

	public void deleteProduct(int id) {
		productRepository.deleteById(id);
	}

	///////////////// BURADAN SONRASI EKLENDİ

	public Product getProduct(int productId) { // getFruit
		Optional<Product> oProduct = productRepository.findById(productId);
		Product product = null;
		if (oProduct.isPresent()) {
			product = oProduct.get();
		} else {
			// product bulunamadı. hata ver
			throw new ServiceException(ERROR_CODES.PRODUCT_NOT_FOUND);
		}
		return product;
	}

	// Yeni metod: Stok miktarını arttır
	public void increaseProductStock(int productId, int count) {
		Product product = getProduct(productId);
		int currentStock = product.getUnitInStock();

		// Güncel stok miktarından azaltılacak miktarı çıkar
		int newStock = currentStock + count;

		// Negatif stok miktarını önlemek için kontrol
		if (newStock < 0) {
			throw new ServiceException(ERROR_CODES.NOT_ENOUGH_SHELF); // hata
																		// kodundan
																		// emin
																		// değilim
		}

		// Yeni stok miktarını güncelle
		product.setUnitInStock(newStock);
		productRepository.save(product);
	}

	// depodan ürün gönderimi için aşağıdaki kısımları yazdım

	// öncelikle gelen id li ürünün raflarda olup olmadığını
	// kontrol eden metodu yazdım

	private ShelfProduct getProductFromShelf(int productId) {
		Optional<ShelfProduct> oProduct = shelfProductRepository
				.findFirstByProductId(productId);
		ShelfProduct product = null;
		if (oProduct.isPresent()) {
			product = oProduct.get();
		} else {
			// product bulunamadı. hata ver
			throw new ServiceException(ERROR_CODES.PRODUCT_NOT_FOUND);
		}
		return product;
	}

	@Transactional
	public void dispatchProduct(int productId, int count) {

		ShelfProduct product = getProductFromShelf(productId);
		// increaseProductStock(productId, count); -> decrese

		Optional<ShelfProduct> oShelf = shelfProductRepository
				.findByProductIdNotFull(productId);
		if (oShelf.isPresent()) {

			// yarı dolu raf bulundu.
			// gönderim öncelikli olarak bu raf içinden yapılacak.

			ShelfProduct shelf = oShelf.get();
			int dispatchCount = count;
			int productCount = shelfProductRepository
					.findProductCountByShelfIdAndProductId(
							shelf.getShelf().getId(), productId);

			if (dispatchCount > productCount) {

				dispatchCount = productCount;

			}

			shelf.setProductCount(productCount - dispatchCount);
			if (shelf.getProductCount() == 0) {

				// bu raftaki bu ürün bitti

				shelfProductRepository.deleteProductFromShelf(
						shelf.getShelf().getId(), productId);

			}

			shelfProductRepository.save(shelf);
			count -= dispatchCount;

		}

		// gönderim yapılacak product kaldıysa diğer tam dolu raflardan gönderim
		// devam edecek

		if (count > 0) {

			dispatchFromFullShelf(count, product);
		}

	}

	private void dispatchFromFullShelf(int count, ShelfProduct shelfProduct) {

		List<ShelfProduct> fullShelves = shelfProductRepository
				.findByProductIdAndProductCountGreaterThan(
						shelfProduct.getProduct().getId(), 0);

		int nextFullShelf = fullShelves.size() - 1;

		while (count > 0) {

			if (nextFullShelf < 0) {

				throw new ServiceException(ERROR_CODES.PRODUCT_NOT_FOUND);

			}

			ShelfProduct shelf = fullShelves.get(nextFullShelf);

			int dispatchAmount = count;
			int shelfProductCount = shelfProductRepository
					.findProductCountByShelfIdAndProductId(
							shelf.getShelf().getId(),
							shelfProduct.getProduct().getId());
			if (dispatchAmount > shelfProductCount) {
				dispatchAmount = shelfProductCount;
			}

			shelf.setProductCount(shelfProductCount - dispatchAmount);
			if (shelf.getProductCount() == 0) {
				// bu raftaki bu ürün bitti

				shelfProductRepository.deleteProductFromShelf(
						shelf.getShelf().getId(),
						shelfProduct.getProduct().getId());

			}

			shelfProductRepository.save(shelf);
			count -= dispatchAmount;
			nextFullShelf--;

		}

	}

}
