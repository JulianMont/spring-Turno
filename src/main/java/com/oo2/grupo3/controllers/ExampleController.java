package com.oo2.grupo3.controllers;

import com.oo2.grupo3.helpers.ViewRouteHelper;
import com.oo2.grupo3.models.dtos.requests.ExampleRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ExampleResponseDTO;
import com.oo2.grupo3.services.interfaces.IExampleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/example")
public class ExampleController {

    private final IExampleService exampleService;

    public ExampleController(IExampleService exampleService) {
        this.exampleService = exampleService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String listNotDeleted(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ExampleResponseDTO> examples = exampleService.findAllNotDeleted(pageable);
        model.addAttribute("examples", examples);
        return ViewRouteHelper.EXAMPLE_LIST;
    }

    @GetMapping("/admin/list")
    @PreAuthorize("hasRole('ADMIN')")
    public String listAll(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<ExampleResponseDTO> examples = exampleService.findAll(pageable);
        model.addAttribute("examples", examples);
        return ViewRouteHelper.EXAMPLE_ADMIN_LIST;
    }

    @GetMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("exampleRequestDTO", new ExampleRequestDTO());
        return ViewRouteHelper.EXAMPLE_FORM;
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String save(@Valid @ModelAttribute ExampleRequestDTO exampleRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ViewRouteHelper.EXAMPLE_FORM;
        }
        exampleService.save(exampleRequestDTO);
        return "redirect:/example/admin/list";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Integer id, Model model) {
        ExampleResponseDTO dto = exampleService.findById(id);

        ExampleRequestDTO requestDTO = new ExampleRequestDTO();
        requestDTO.setId(dto.getId());
        requestDTO.setName(dto.getName());
        requestDTO.setPrice(dto.getPrice());

        model.addAttribute("exampleRequestDTO", requestDTO);
        return ViewRouteHelper.EXAMPLE_FORM;
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute ExampleRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ViewRouteHelper.EXAMPLE_FORM;
        }
        exampleService.update(id, dto);
        return "redirect:/example/admin/list";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String softDelete(@PathVariable Integer id) {
        exampleService.deleteById(id);
        return "redirect:/example/admin/list";
    }

    @PostMapping("/restore/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String restore(@PathVariable Integer id) {
        exampleService.restoreById(id);
        return "redirect:/example/admin/list";
    }
}