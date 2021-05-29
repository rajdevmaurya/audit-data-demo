package com.demo.audit.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.audit.dto.ProductRequest;
import com.demo.audit.entity.Lock;
import com.demo.audit.entity.Product;
import com.demo.audit.repository.LockRepository;
import com.demo.audit.repository.ProductRepository;

@Service
public class ProductService {
	

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private LockRepository lockRepository;

	public String saveProduct(ProductRequest<Product> request) {
		Product product = request.getProduct();
		product.setCreatedBy(request.getCreatedBy());
		repository.save(product);
		return "Producthas been saved successfully";

	}

	public String updateProduct(Long id, double price, ProductRequest<Product> request) {
		Product product = repository.findById(id).get();
		if (product != null) {
			product.setPrice(price);
			product.setModifiedBy(request.getModifiedBy());
			repository.saveAndFlush(product);
		} else {
			throw new RuntimeException("Product not found with id : " + id);
		}
		return "Product has been Updated successfully ";
	}

	
	public String getProductService(String msz) throws InterruptedException {
		String lockValue = null;
		List<Lock> s = lockRepository.findAll();
		if (s.size() > 0) {
			lockValue = s.get(0).getName();
		}

		if (msz.equals("MANUAL_TRIGGER")) {
			System.err.println("MANUAL_TRIGGER request coming===========>");
			if (lockValue == null) {
				System.err.println("MANUAL_TRIGGER calling.===================>>" + msz);
				Lock lock = new Lock();
				lock.setName("MANUAL_TRIGGER");
				lock.setCreateDt(LocalDateTime.now());
				lockRepository.save(lock);
				heyBatchRunIt();
				lockRepository.delete(lock);
				System.err.println("MANUAL_TRIGGER calling.....ended===================>>" + msz);
			} else {

				// if(s.) {
				System.err.println("Job skip for " + msz);
				// }
			}

		} else {
			System.out.println("Auto request coming===========>");
			if (msz.equals("AUTO_SCHEDULE")) {
				if (lockValue == null) {
					System.out.println("AUTO_SCHEDULE calling..............>>" + msz);
					Lock ll = new Lock();
					ll.setName("AUTO_SCHEDULE");
					ll.setCreateDt(LocalDateTime.now());
					lockRepository.save(ll);
					heyBatchRunIt();
					lockRepository.delete(ll);
					System.out.println("AUTO_SCHEDULE calling.....ended.........>>" + msz);
				}
			} else {
				System.out.println(" AUTO_SCHEDULE Job skip for======== " + msz);
			}
		}
		return "Hi Product";
	}

	public void heyBatchRunIt() throws InterruptedException {
		System.out.println("Batch running");
		Thread.sleep(30000);
	}
}
