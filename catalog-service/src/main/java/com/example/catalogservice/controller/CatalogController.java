package com.example.catalogservice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.catalogservice.jpa.CatalogEntity;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.ResponseCatalog;

@RestController
@RequestMapping("/catalog-service")
public class CatalogController {
	
	Environment env;
	CatalogService catalogService;
	
	@Autowired
	public CatalogController(Environment env, CatalogService catalogService) {
		super();
		this.env = env;
		this.catalogService = catalogService;
	}
	
	@GetMapping("/health_check")
	public String status(HttpServletRequest request) {
		return String.format("It's Working in Catalog Service %s", env.getProperty("local.server.port"));
	}
	
	/* 모든 카타로그에 대한 정보 출력 */
	@GetMapping("/catalogs")
	public ResponseEntity<List<ResponseCatalog>> getCatalogs(){
		Iterable<CatalogEntity> catalogList = catalogService.getAllCatalogs();
		
		List<ResponseCatalog> result = new ArrayList<>();
		
		catalogList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseCatalog.class));
		});
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
