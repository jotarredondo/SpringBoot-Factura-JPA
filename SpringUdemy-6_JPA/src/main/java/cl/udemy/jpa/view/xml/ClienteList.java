package cl.udemy.jpa.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import cl.udemy.jpa.modelo.Cliente;

@XmlRootElement(name="clienteList")
public class ClienteList {
	
	
	@XmlElement(name="cliente")
	public List<Cliente> clientes;
	
	

	public ClienteList() {
	}

	public ClienteList(List<Cliente> clientes) {

		this.clientes = clientes;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}
	
	

}
