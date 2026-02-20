package com.utc.bancario1.controller;
import com.utc.bancario1.entity.Cliente;
import com.utc.bancario1.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")

public class ClienteController {
	@Autowired
    private ClienteRepository clienteRepository;

    // 1. LISTAR todos los clientes
    @GetMapping
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteRepository.findAll();
        model.addAttribute("clientes", clientes);
        return "clientes/lista"; // Esto buscará templates/clientes/lista.html
    }

    // 2. Mostrar formulario para NUEVO cliente
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/formulario";
    }

    // 3. GUARDAR cliente (nuevo o editado)
    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente, RedirectAttributes redirectAttributes) {
        clienteRepository.save(cliente);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
        return "redirect:/clientes"; // Redirige a la lista
    }

    // 4. Mostrar formulario para EDITAR cliente
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("ID de cliente inválido: " + id));
        model.addAttribute("cliente", cliente);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente editado exitosamente");
        return "clientes/formulario"; // Reusa el mismo formulario
    }

    // 5. ELIMINAR cliente
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clienteRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
        return "redirect:/clientes";
    }
    
    
}
