package com.fabrizio.recetapp.app.controllers;

import com.fabrizio.recetapp.app.models.entity.Ingrediente;
import com.fabrizio.recetapp.app.models.entity.Receta;
import com.fabrizio.recetapp.app.models.service.IIngredienteService;
import com.fabrizio.recetapp.app.models.service.IRecetaService;
import com.fabrizio.recetapp.app.models.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/receta")
@SessionAttributes("receta")
public class RecetaController {

	@Autowired
	private IIngredienteService ingredienteService;

	@Autowired
	private IRecetaService recetaService;

	@Autowired
	private IUploadFileService uploadFileService;

	@GetMapping("/listar")
	public String listado(Model model, Authentication authentication, HttpServletRequest request, Locale locale) {

		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");
		List<Receta> recetas = recetaService.findAll();

		model.addAttribute("titulo", "Listado de recetas");
		model.addAttribute("recetas", recetas);

		return "recetas";
	}

	@Secured("ROLE_USER")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
//		Receta receta = recetaService.fetchByIdWithFacturas(id);//clienteService.findOne(id);
		Receta receta = recetaService.findOne(id);
		if (receta == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		model.put("receta", receta);
		model.put("titulo", "Detalle receta: " + receta.getNombre());
		return "verreceta";
	}

	@GetMapping("/")
	public String displayCrearReceta(Model model) {
		model.addAttribute("receta", new Receta());
		return "creareceta";
	}

	@GetMapping(value = "/cargar-ingredientes/{term}", produces = { "application/json" })
	public @ResponseBody List<Ingrediente> cargarIngrediente(@PathVariable String term) {
		return recetaService.findIngredienteByNombre(term);
	}

	@PostMapping("/form")
	public String guardar(@Valid Receta receta, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId, @RequestParam("file") MultipartFile foto,
			RedirectAttributes flash, SessionStatus status) {

//		para la validacion de la descripcion nula al momento de crear una factura.
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Receta");
			return "creareceta";
		}

//		para la validacion de las lineas nula al momento de crear una factura.
		if (itemId == null || itemId.length == 0) {
			model.addAttribute("titulo", "Crear Receta");
//			el error se conecta al th:errorclass
			model.addAttribute("error", "Error: La receta no puede no tener ingredientes!");
			return "creareceta";
		}

		if (foto.isEmpty()) {
			model.addAttribute("titulo", "Crear Receta");
			model.addAttribute("error", "Error: La receta no puede no tener foto!");
			return "creareceta";
		}

		for (int i = 0; i < itemId.length; i++) {
			Ingrediente ingrediente = recetaService.findIngredienteById(itemId[i]);
			receta.addIngrediente(ingrediente);
		}

		if (!foto.isEmpty()) {
			if (receta.getId() != null && receta.getId() > 0 && receta.getFoto() != null
					&& receta.getFoto().length() > 0) {
				uploadFileService.delete(receta.getFoto());
			}
//		String uniqueFilename = uploadFileService.copy(foto);
			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
			receta.setFoto(uniqueFilename);

		}

		recetaService.save(receta);

//		para finalizar la sesion de factura en el session
		status.setComplete();
		flash.addFlashAttribute("success", "Receta creada con éxito!");

		return "redirect:/receta/listar";
	}

	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
//		Resource recurso = uploadFileService.load(filename);
		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Receta receta = recetaService.findOne(id);
			recetaService.delete(id);
			flash.addFlashAttribute("success", "Receta " + receta.getNombre() + " Eliminada con éxito!");

			if (uploadFileService.delete(receta.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + receta.getFoto() + " eliminada con éxito!");
			}
		}

		return "redirect:/receta/listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar")
	public String eliminar(RedirectAttributes flash) {

		recetaService.deleteAll();
		flash.addFlashAttribute("success", "Todas las recetas fueron eliminadas con éxito");

		return "redirect:/receta/listar";
	}


}
