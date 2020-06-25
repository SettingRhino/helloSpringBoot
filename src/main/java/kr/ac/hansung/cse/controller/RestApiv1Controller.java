package kr.ac.hansung.cse.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.ac.hansung.cse.model.Product;
import kr.ac.hansung.cse.repository.ProductRepository;

@RestController//@Controller+@ResponseBody
@RequestMapping("/api/v1")
public class RestApiv1Controller {
	@Autowired
	ProductRepository repository;

	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> customers = new ArrayList<>();
		try {
			repository.findAll().forEach(customers::add);

			if (customers.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(customers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") int id) {
		Optional<Product> product = repository.findById(id);

		if (product.isPresent()) {
			return new ResponseEntity<>(product.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@PostMapping(value = "/products")
	public ResponseEntity<Product> postProduct(@RequestBody Product product) {
		try {
			Product newproduct = repository.save(product);
			return new ResponseEntity<>(newproduct, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.EXPECTATION_FAILED);
		}
	}
	@GetMapping(value = "products/category/{category}")
	public ResponseEntity<List<Product>> findByCategory(@PathVariable String category) {
		try {
			List<Product> products = repository.findByCategory(category);

			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}
	@PutMapping("/products/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") int id, @RequestBody Product product) {
		Optional<Product> findproduct = repository.findById(id);

		if (findproduct.isPresent()) {
			Product updateproduct = findproduct.get();
			updateproduct.setName(product.getName());
			updateproduct.setCategory(product.getCategory());
			updateproduct.setPrice(product.getPrice());
			updateproduct.setManufacturer(product.getManufacturer());
			updateproduct.setUnitInStock(product.getUnitInStock());
			updateproduct.setDescription(product.getDescription());
			return new ResponseEntity<>(repository.save(updateproduct), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	@DeleteMapping("/products/{id}")
	public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") int id) {
		try {
			repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
		}
	}

}
