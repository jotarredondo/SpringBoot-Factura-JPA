package cl.udemy.jpa.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import cl.udemy.jpa.modelo.Producto;

@Repository
public interface IProductoDao extends CrudRepository<Producto, Long> {
	
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> findByNombre(String term);

}
