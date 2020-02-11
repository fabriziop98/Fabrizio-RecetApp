package com.fabrizio.recetapp.app.controllers;

import com.fabrizio.recetapp.app.models.entity.Ingrediente;
import com.fabrizio.recetapp.app.models.entity.Receta;
import com.fabrizio.recetapp.app.models.service.IIngredienteService;
import com.fabrizio.recetapp.app.models.service.IUploadFileService;
import com.fabrizio.recetapp.app.models.service.RecetaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


@Controller
@SessionAttributes("usuario")
public class PrincipalController {

    @Autowired
    private IIngredienteService ingredienteService;

    @Autowired
    private IUploadFileService uploadFileService;

    @Autowired
    private RecetaServiceImpl recetaService;

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

    @GetMapping(value = "/", produces = {"application/json"})
    public String listar(Model model, Authentication authentication,
                         HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");


        return "listar";
    }

    @GetMapping(value = "/listar/{term}", produces = "application/json")
    public @ResponseBody
    List<Ingrediente> busqueda(@PathVariable String term) {
        return ingredienteService.findByNombre(term);
    }
    
    @GetMapping(value = "/encontrarRecetas/")
    public String ListaIngredientesNull(RedirectAttributes flash) {
    	flash.addAttribute("error", "¡No haz ingresado ningun ingrediente! Esta vez prueba ingresando uno");
    	return "redirect:/";
    }

    @GetMapping(value = "/encontrarRecetas/{listaIngredientes}")
    public String busquedaRecetaPorIngrediente(@PathVariable List<String> listaIngredientes, Model model) {
        List<Ingrediente> ingredientesEncontrados = new ArrayList<>();
        System.out.print(listaIngredientes);
        for (String ingrediente : listaIngredientes) {
            ingredientesEncontrados.add(ingredienteService.findByNombreContaining(ingrediente));
        }

        model.addAttribute("recetas", recetaService.findByIngrediente(ingredientesEncontrados));

        return "recetas";
    }


    /*
    Este mapping te devuelve un Json para que veas más rápido, antes de hacer la vista, que devuelve las recetas que contienen
    todos los ingredientes que pusiste
     */
    @GetMapping(value = "/encontrarRecetasJson/{listaIngredientes}", produces = {"application/json"})
    public @ResponseBody
    List<Receta> busquedaRecetaPorIngredientes(@PathVariable List<String> listaIngredientes) {
        List<Ingrediente> ingredientesEncontrados = new ArrayList<>();
        System.out.print(listaIngredientes);
        for (String ingrediente : listaIngredientes) {
            ingredientesEncontrados.add(ingredienteService.findByNombreContaining(ingrediente));
        }

        return recetaService.findByIngrediente2(ingredientesEncontrados); //DEVUELVE LAS RECETAS QUE CONTIENEN TODOS LOS INGREDIENTES
        //return recerecetaService.findByIngrediente2(ingredientesEncontrados); //ESTE TE DEVUELVE LAS RECETAS QUE CONTIENEN AL MENOS UNO DE LOS INGREDIENTES
    }


}
