package io.github.brenovit.store.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.brenovit.store.models.EPermission;
import io.github.brenovit.store.models.Product;
import io.github.brenovit.store.models.User;
import io.github.brenovit.store.repository.ProductRepository;

@Service
public class ProductService extends InternalService {	
	
	@Autowired
	private ProductRepository repository;
	
	public List<Product> findAll(){
		User user = getLoggedUser();
		if(user.hasPermission(EPermission.ADMIN)) {
			return repository.findAll();			
		}
		return repository.findByUserId(getLoggedUser().getId());	
	}
		
	public Optional<Product> findById(Long id){
		Optional<Product> product = repository.findByIdAndUserId(id, getLoggedUser().getId());
		if(product.isPresent() && product.get().getUser().getId() != getLoggedUser().getId()) {
			return Optional.empty();
		}
		return product;
	}
	
	public Product save (Product product) {
		product.setUser(getLoggedUser());		
		return repository.save(product);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}	
}
