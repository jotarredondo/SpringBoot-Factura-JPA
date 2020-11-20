package cl.udemy.jpa.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import cl.udemy.jpa.modelo.Factura;

@Repository
public interface IFacturaDao extends CrudRepository<Factura, Long>{

	@Query("SELECT f FROM Factura f JOIN fetch f.cliente c JOIN fetch f.items l JOIN fetch l.producto WHERE f.id =?1")
	public Factura fetchPorIdWithClienteAndItemFactura(Long id);
}
