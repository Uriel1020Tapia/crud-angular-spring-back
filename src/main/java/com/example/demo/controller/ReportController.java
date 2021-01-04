package com.example.demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.FileDTO;
import com.example.demo.service.ReportService;

import net.sf.jasperreports.engine.JRException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/")
public class ReportController {

	@Autowired
	ReportService reportService;

	@GetMapping("/report/{format}")
	public FileDTO generateReport(@PathVariable String format) throws JRException, IOException {
		return reportService.exportReport(format);
	}

}
