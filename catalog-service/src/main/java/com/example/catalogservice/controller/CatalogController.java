package com.example.catalogservice.controller;


import com.example.catalogservice.entity.CatalogEntity;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
@Slf4j
public class CatalogController {
    Environment env;
    CatalogService catalogService;

    public CatalogController(Environment env,CatalogService catalogService) {
        this.catalogService=catalogService;
        this.env = env;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server Port ={}",request.getServerPort());
        return String.format("Hi, there. This is a message from First service %s .",env.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        Iterable<CatalogEntity> catalogList = catalogService.getAllCatalogs();
//        log.info("helloword");

        List<ResponseCatalog> result = new ArrayList<>();
        catalogList.forEach(v -> {
            result.add(new ModelMapper().map(v,ResponseCatalog.class));
        });
//        log.info(catalogLis);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
