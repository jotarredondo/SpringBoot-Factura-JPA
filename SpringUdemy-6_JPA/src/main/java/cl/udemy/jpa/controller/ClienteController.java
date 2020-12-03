package cl.udemy.jpa.controller;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cl.udemy.jpa.modelo.Cliente;
import cl.udemy.jpa.service.IClienteService;
import cl.udemy.jpa.service.IUploadService;
import cl.udemy.jpa.utilPage.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadService uploadService;
	
	@Autowired
	private MessageSource messageSource;
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Secured({"ROLE_USER"})
	@GetMapping(value = "/upload/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = uploadService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash, Locale locale) {

		Cliente cliente = clienteService.fetchById(id);
		if (cliente == null) {
			flash.addFlashAttribute("danger", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", messageSource.getMessage("text.cliente.detalle.titulo", null, locale).concat(": ").concat(cliente.getNombre()));
		return "ver";

	}

	
	
	@RequestMapping(value = {"/listar","/"}, method = RequestMethod.GET)
	public String listar(Model modelo,Authentication authentication,
			HttpServletRequest request, Locale locale,
			@RequestParam(name = "page", defaultValue = "0") int page) {
		
		if(authentication != null) {
			logger.info("Hola usuarioautenticado, tu username es : ".concat(authentication.getName()));
			
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			logger.info("Utilizando forma estática Security Context Holder - Hola usuarioautenticado, tu username es : ".concat(auth.getName()));	
		}
		
		if(hasRole("ROLE_ADMIN")) {
			logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info("Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
			
		}
		
		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");
		
		if(securityContext.isUserInRole("ROLE_ADMIN")) {
			logger.info("(Usando SecurityContextHolder AwareRequestWrapper)Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info(" (Usando SecurityContextHolder AwareRequestWrapper)Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}
		
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("(Usando HttpServletRequest)Hola ".concat(auth.getName()).concat(" tienes acceso!"));
		} else {
			logger.info(" (Usando HttpServletRequest)Hola ".concat(auth.getName()).concat(" NO tienes acceso!"));
		}
		
		Pageable pageRequest = PageRequest.of(page, 5);
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);

		modelo.addAttribute("clientes", clientes);
		modelo.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
		modelo.addAttribute("page", pageRender);

		return "listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> modelo, Locale locale) {

		Cliente cliente = new Cliente();
		modelo.put("cliente", cliente);
		modelo.put("titulo", messageSource.getMessage("text.cliente.form.titulo.crear", null, locale));
		return "form";
	}
	 
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> modelo, RedirectAttributes flash, Locale locale) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("danger", messageSource.getMessage("text.cliente.flash.db.error", null, locale));
				return "redirect:/listar";
			}

		} else {
			flash.addFlashAttribute("danger", messageSource.getMessage("text.cliente.flash.id.error", null, locale));
			return "redirect:/listar";

		}
		modelo.put("cliente", cliente);
		modelo.put("titulo", messageSource.getMessage("text.cliente.form.titulo.editar", null, locale));
		return "form";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model modelo, RedirectAttributes flash,
			SessionStatus status, @RequestParam("file") MultipartFile foto, Locale locale) {

		if (result.hasErrors()) {

			modelo.addAttribute("titulo", "Formulario de clientes");
			return "form";
		}
		if (!foto.isEmpty()) {

			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {
				uploadService.delete(cliente.getFoto());

			}

			String uniqueFileName = null;
			try {
				uniqueFileName = uploadService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", messageSource.getMessage("text.cliente.flash.foto.subir.success", null, locale) + "'" + uniqueFileName + "'");
			cliente.setFoto(uniqueFileName);

		}
		String mensajeFlash = (cliente.getId() != null) ? messageSource.getMessage("text.cliente.flash.editar.success", null, locale) : messageSource.getMessage("text.cliente.flash.crear.success", null, locale);
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash, Locale locale) {

		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);

			clienteService.elimina(id);
			flash.addFlashAttribute("success", messageSource.getMessage("text.cliente.flash.eliminar.success", null, locale));

			if (uploadService.delete(cliente.getFoto())) {
				String mensajeFotoEliminar = String.format(messageSource.getMessage("text.cliente.flash.foto.eliminar.success", null, locale), cliente.getFoto());
				flash.addFlashAttribute("info", mensajeFotoEliminar);
			}
		}
		return "redirect:/listar";
	}
	
	private boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null) {
			return false;
		}
		Authentication auth = context.getAuthentication();
		if(auth == null) {
			return false;
		}
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
			return authorities.contains(new SimpleGrantedAuthority(role));
		
//		for(GrantedAuthority authority: authorities) {
//			if(role.equals(authority.getAuthority())) {
//				logger.info("Hola usuario : ".concat(auth.getName()).concat(" tu role es: ".concat(authority.getAuthority())));
//				return true;
//			}
//		}
//		
//		return false;
	}
}
