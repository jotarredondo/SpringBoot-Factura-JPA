package cl.udemy.jpa.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.udemy.jpa.modelo.Cliente;
import cl.udemy.jpa.modelo.Factura;
import cl.udemy.jpa.modelo.ItemFactura;
import cl.udemy.jpa.modelo.Producto;
import cl.udemy.jpa.service.IClienteService;

@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {
	
	@Autowired
	private IClienteService clienteServicio;
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Model model,
			RedirectAttributes flash) {
		
		Factura factura = clienteServicio.fetchFacturaById(id);
		
//		Factura factura = clienteServicio.findFacturaById(id);
		
		if(factura == null){
			flash.addFlashAttribute("danger", "La factura no existe en la base de datos! ");
			return "redirect:/listar";
		}
		model.addAttribute("factura", factura);
		model.addAttribute("titulo", "Factura :  ".concat(factura.getDescripcion())); 
		
		return "factura/ver";
	}
	
	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(name="clienteId")Long clienteId,
			Map<String, Object> model,
			RedirectAttributes flash) {
		
		Cliente cliente = clienteServicio.findOne(clienteId);
		if(cliente == null) {
			flash.addAttribute("error", "El cliente no existe en la base de datos");
			return "redicrect : /listar";
		}
		
		Factura factura = new Factura();
		factura.setCliente(cliente);
		
		model.put("factura", factura);
		model.put("titulo", " Crear Factura");
		
		return "factura/form";
	}
	
	@GetMapping(value="/cargar-productos/{term}", produces= {"application/json"})
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term){
		
		return clienteServicio.findByNombre(term);
		
	}

	@PostMapping("/form")
	public String guardar(@Valid Factura factura, 
			BindingResult result,
			Model modelo, 
			@RequestParam(name="item_id[]", required=false)Long[] itemId,
			@RequestParam(name="cantidad[]", required=false)Integer[] cantidad,
			RedirectAttributes flash,
			SessionStatus status) {
		
		if(result.hasErrors()) {
			
			modelo.addAttribute("titulo", "Crear Factura");
			return "factura/form";
		}
		
		if(itemId == null || itemId.length == 0) {
			modelo.addAttribute("titulo", "Crear Factura");
			modelo.addAttribute("danger", "Error:  La factura debe tener lineas");
			return "factura/form";
		}
		
		for (int i = 0; i < itemId.length; i++) {
			Producto producto = clienteServicio.findProductoById(itemId[i]);
			ItemFactura linea =new ItemFactura();
			linea.setCantidad(cantidad[i]);
			linea.setProducto(producto);
			factura.addItemFactura(linea);
			
			log.info("ID: " + itemId[i].toString() + ", cantidad: " + cantidad[i].toString());
			
		}
		
		clienteServicio.saveFactura(factura);
		status.setComplete();
		
		flash.addFlashAttribute("success", "Factura creada con éxito !");
		
		return "redirect:/ver/" + factura.getCliente().getId();
	}
	
	
	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value="id")Long id,RedirectAttributes flash) {
		
		Factura factura = clienteServicio.findFacturaById(id);
		
		if(factura !=null) {
			clienteServicio.deleteFactura(id);
			flash.addFlashAttribute("success", "Factura eliminada con exito!");
			return "redirect:/ver/" + factura.getCliente().getId();
			
		}
		
		flash.addFlashAttribute("danger", "La factura no existe en la BBDD, se puede eliminar");
		return "redirect:/listar";
	}
	
	
}
