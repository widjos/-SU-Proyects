package com.universales.practica2.impl;

import java.util.List;
import java.util.Optional;

import com.library.dt.TestDto.CompaniaDto;
import com.universales.practica2.entity.Compania;
import com.universales.practica2.entity.Seguro;
import com.universales.practica2.repository.CompaniaRepository;
import com.universales.practica2.repository.SeguroRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compania")
@CrossOrigin
public class CompaniaService {

    @Autowired
    CompaniaRepository companiaRepository;
    
    @Autowired
    SeguroRepository seguroRepository;
    
    @GetMapping("/buscar")
    public List<Compania> buscar() {
        return companiaRepository.findAll();
    }

    @PostMapping("/guardar")
    public Compania guardar(@RequestBody CompaniaDto newCompania) {
        Compania compania = this.nuevoCompania(newCompania);
        List<Seguro> seguros = compania.getSeguros();
        compania.setSeguros(null);
        companiaRepository.save(compania);
        for (Seguro seguro : seguros) {
        	seguro.getCompanias().add(compania);   
        }
        seguroRepository.saveAll(seguros);
        compania.setSeguros(seguros);
        return compania;
    }

    @PutMapping("/actualizar")
    public Compania actualizar(@RequestBody CompaniaDto newCompania) {
        return this.guardar(newCompania);
    }

    @DeleteMapping(path = "/eliminar/{id}")
    public void eliminar(@PathVariable("id") String id) {
        Optional<Compania> compania = companiaRepository.findById(id);
        if (compania.isPresent()) {
            companiaRepository.delete(compania.get());
        }
    }

    @GetMapping("/buscar/{id}")
    public List<Compania> buscar(@PathVariable("id") String id) {
        return companiaRepository.findByNombreCompania(id);
    }

    @GetMapping("/buscar/seguros")
    public List<Compania> buscarSeguros() {
        return companiaRepository.findDistinctBySegurosNotNull();
    }

    @GetMapping("/buscar/seguros/{id}")
    public List<Compania> buscarSeguros(@PathVariable("id") String id) {
        return companiaRepository.findDistinctByNombreCompaniaContainingAndSegurosNotNull(id);
    }

    @GetMapping("/buscar/numeroVia/{numeroVia}")
    public List<Compania> buscarNumeroVia(@PathVariable("numeroVia") int numeroVia) {
        return companiaRepository.findByNumeroViaIs(numeroVia);
    }

    public Compania nuevoCompania(CompaniaDto newCompania) {
        Compania compania = new Compania();
        compania.setNombreCompania(newCompania.getNombreCompania());
        compania.setNumeroVia(newCompania.getNumeroVia());
        compania.setClaseVia(newCompania.getClaseVia());
        compania.setCodPostal(newCompania.getCodPostal());
        compania.setNombreVia(newCompania.getNombreVia());
        compania.setNotas(newCompania.getNotas());
        compania.setNumeroVia(newCompania.getNumeroVia());
        compania.setTelefonoContratacion(newCompania.getTelefonoContratacion());
        compania.setTelefonoSiniestros(newCompania.getTelefonoSiniestros());
        return compania;
    }
}
