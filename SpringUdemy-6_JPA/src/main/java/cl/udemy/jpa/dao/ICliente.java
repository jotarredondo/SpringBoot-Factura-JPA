package cl.udemy.jpa.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import cl.udemy.jpa.modelo.Cliente;

@Repository
public interface ICliente extends PagingAndSortingRepository<Cliente, Long> {
	
	@Query("SELECT c from Cliente c left JOIN fetch c.facturas f WHERE c.id =?1")
	public Cliente fetchByIdWithFacrua(Long id);
	
}
