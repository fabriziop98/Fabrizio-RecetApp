package com.fabrizio.recetapp.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fabrizio.recetapp.app.models.entity.Ingrediente;
import com.fabrizio.recetapp.app.models.entity.Receta;
import com.fabrizio.recetapp.app.models.service.IIngredienteService;
import com.fabrizio.recetapp.app.models.service.IUploadFileService;
import com.fabrizio.recetapp.app.models.service.IngredienteServiceImpl;

@Controller
@RequestMapping("/ingrediente")
public class IngredienteController {
	
	@Autowired
	private IIngredienteService ingredienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	@Secured("ROLE_ADMIN")
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
	
	@GetMapping("/listar")
	public String listado(Model model, Authentication authentication, HttpServletRequest request, Locale locale) {

		SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request,
				"ROLE_");
		List<Ingrediente> ingredientes = ingredienteService.findAll();

		model.addAttribute("titulo", "Listado de ingredientes");
		model.addAttribute("ingredientes", ingredientes);

		return "ingredientes";
	}
	

	@GetMapping("/")
	public String displayCrearIngrediente(Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		return "crearingrediente";
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/form")
	public String crear(@Valid Ingrediente ingrediente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

//		para la validacion de la descripcion nula al momento de crear una factura.
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Crear Ingrediente");
			return "crearingrediente";
		}
		
		if (!foto.isEmpty()) {
			if (ingrediente.getId() != null && ingrediente.getId() > 0 && ingrediente.getFoto() != null
					&& ingrediente.getFoto().length() > 0) {
				uploadFileService.delete(ingrediente.getFoto());
			}
//			String uniqueFilename = uploadFileService.copy(foto);
			String uniqueFilename = null;
			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}
			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
			ingrediente.setFoto(uniqueFilename);
			
		}
		flash.addFlashAttribute("success", "Ingrediente '"+ingrediente.getNombre()+"' creado con éxito");
		ingredienteService.save(ingrediente);
		
		status.setComplete();
		return "redirect:/ingrediente/listar";
	}
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar")
	public String eliminar(RedirectAttributes flash) {

		ingredienteService.deleteAll();
		flash.addFlashAttribute("success", "Todas los ingredientes fueron eliminados con éxito");

		return "redirect:/ingrediente/listar";
	}
	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Ingrediente ingrediente = ingredienteService.findOne(id);
			ingredienteService.delete(id);
			flash.addFlashAttribute("success", "Ingrediente "+ingrediente.getNombre() +" Eliminado con éxito!");

			
				if (uploadFileService.delete(ingrediente.getFoto())) {
					flash.addFlashAttribute("info", "Foto " + ingrediente.getFoto() + " eliminada con éxito!");
				}
			}
		

		return "redirect:/ingrediente/listar";
	}

}
