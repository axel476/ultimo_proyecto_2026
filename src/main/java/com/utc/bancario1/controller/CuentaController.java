package com.utc.bancario1.controller;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utc.bancario1.entity.Cuenta;
import com.utc.bancario1.repository.ClienteRepository;
import com.utc.bancario1.repository.CuentaRepository;

@Controller
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaController(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    // 1) LISTAR
    @GetMapping
    public String listar(Model model) {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        model.addAttribute("cuentas", cuentas);
        return "cuenta/lista";
    }

    // 2) FORM NUEVO
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("cuenta", new Cuenta());
        model.addAttribute("clientes", clienteRepository.findAll());
        return "cuenta/formulario";
    }

    // 3) FORM EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de cuenta inválido: " + id));

        model.addAttribute("cuenta", cuenta);
        model.addAttribute("clientes", clienteRepository.findAll());
        return "cuenta/formulario";
    }

    // 4) GUARDAR - CORREGIDO: Usar @ModelAttribute en lugar de @RequestParam
    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Cuenta cuenta,
            @RequestParam("clienteId") Long clienteId, // Cambiado de "cliente.id" a "clienteId"
            RedirectAttributes redirectAttributes
    ) {
        var cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        
        cuenta.setCliente(cliente);
        cuentaRepository.save(cuenta);

        String mensaje = (cuenta.getId() != null) 
                ? "Cuenta editada exitosamente" 
                : "Cuenta creada exitosamente";
        
        redirectAttributes.addFlashAttribute("mensaje", mensaje);
        return "redirect:/cuentas";
    }

    // 5) ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        cuentaRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("mensaje", "Cuenta eliminada exitosamente");
        return "redirect:/cuentas";
    }
}