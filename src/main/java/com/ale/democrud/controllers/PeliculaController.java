package com.ale.democrud.controllers;


import com.ale.democrud.models.Director;
import com.ale.democrud.models.Pelicula;
import com.ale.democrud.services.DirectorService;
import com.ale.democrud.services.PeliculaService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;

    @Autowired
    private DirectorService directorService;

    @PostConstruct
    public void initData() {
        if (directorService.listarTodos().isEmpty()) {
            Director d1 = directorService.guardar(new Director("Guillermo del Toro", "Mexicana"));
            Director d2 = directorService.guardar(new Director("Christopher Nolan", "Británica"));
            Director d3 = directorService.guardar(new Director("Alfonso Cuarón", "Mexicana"));

            peliculaService.guardar(new Pelicula("El Laberinto del Fauno", "Fantasía", 2006, d1));
            peliculaService.guardar(new Pelicula("Interstellar", "Ciencia Ficción", 2014, d2));
            peliculaService.guardar(new Pelicula("Roma", "Drama", 2018, d3));
        }
    }

    // 1. LEER
    @GetMapping("/")
    public String listar(Model model) {
        model.addAttribute("peliculas", peliculaService.listarTodas());
        model.addAttribute("directores", directorService.listarTodos());
        model.addAttribute("nuevoDirector", new Director());
        return "index";
    }

    // 2. CREAR
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        model.addAttribute("directores", directorService.listarTodos());
        return "form";
    }

    // Guardar Película 
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pelicula pelicula) {
        peliculaService.guardar(pelicula);
        return "redirect:/";
    }

    // Guardar nuevo Director 
    @PostMapping("/directores/guardar")
    public String guardarDirector(@ModelAttribute Director director) {
        directorService.guardar(director);
        return "redirect:/";
    }

    // 3. Actualizar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Pelicula pelicula = peliculaService.obtenerPorId(id).orElse(null);
        model.addAttribute("pelicula", pelicula);
        model.addAttribute("directores", directorService.listarTodos());
        return "form";
    }

    // 4. ELIMINAR 
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        peliculaService.eliminar(id);
        return "redirect:/";
    }
}