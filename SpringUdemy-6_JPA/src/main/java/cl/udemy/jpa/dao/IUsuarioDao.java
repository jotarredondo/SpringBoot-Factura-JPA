package cl.udemy.jpa.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import cl.udemy.jpa.modelo.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	
	@Query("select u from Usuario u where u.username=?1")
	public Usuario findByUsername(String username);

}
