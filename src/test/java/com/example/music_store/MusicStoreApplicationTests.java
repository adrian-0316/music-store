package com.example.music_store;

import com.example.music_store.dto.ProductRequest;
import com.example.music_store.dto.ProductResponse;
import com.example.music_store.entity.Product;
import com.example.music_store.repository.ProductRepository;
import com.example.music_store.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@InjectMocks
	private ProductService productService;

	private ProductRequest request;
	private Product product;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		request = ProductRequest.builder()
				.name("Guitar")
				.description("Acoustic guitar")
				.price(BigDecimal.valueOf(500))
				.imageUrl("guitar.jpg")
				.category("String")
				.build();

		product = Product.builder()
				.id(1L)
				.name("Guitar")
				.description("Acoustic guitar")
				.price(BigDecimal.valueOf(500))
				.imageUrl("guitar.jpg")
				.category("String")
				.build();
	}

	@Test
	void create_ShouldSaveAndReturnProductResponse() {
		when(productRepository.save(any(Product.class))).thenReturn(product);

		ProductResponse response = productService.create(request);

		assertNotNull(response);
		assertEquals("Guitar", response.getName());
		verify(productRepository, times(1)).save(any(Product.class));
	}

	@Test
	void findAll_ShouldReturnListOfProductResponses() {
		when(productRepository.findAll()).thenReturn(List.of(product));

		List<ProductResponse> responses = productService.findAll();

		assertEquals(1, responses.size());
		assertEquals("Guitar", responses.get(0).getName());
		verify(productRepository, times(1)).findAll();
	}

	@Test
	void findById_ShouldReturnProductResponse_WhenFound() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		ProductResponse response = productService.findById(1L);

		assertNotNull(response);
		assertEquals("Guitar", response.getName());
		verify(productRepository, times(1)).findById(1L);
	}

	@Test
	void findById_ShouldThrowException_WhenNotFound() {
		when(productRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> productService.findById(1L));
	}

	@Test
	void update_ShouldModifyAndReturnProductResponse() {
		when(productRepository.findById(1L)).thenReturn(Optional.of(product));

		ProductResponse response = productService.update(1L, request);

		assertNotNull(response);
		assertEquals("Guitar", response.getName());
		verify(productRepository, times(1)).findById(1L);
	}

	@Test
	void delete_ShouldCallRepositoryDeleteById() {
		productService.delete(1L);

		verify(productRepository, times(1)).deleteById(1L);
	}
}
