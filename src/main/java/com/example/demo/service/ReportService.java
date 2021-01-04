package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.example.demo.dto.FileDTO;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportService {

	  @Autowired
      private EmployeeRepository employeeRepository;
	  
	    @Autowired
	    ResourceLoader resourceLoader;
	    
	    public FileDTO exportReport(String reportFormat) throws JRException, IOException {
	        String path = "C:\\Users\\trans\\Desktop\\Report";
	        
	        FileDTO fileDTO = new FileDTO();
	        
	        List<Employee> employees = employeeRepository.findAll();
	        
	
	        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employees);
	        
	        Map<String, Object> parameters = new HashMap<>();
	        Resource resAl = resourceLoader.getResource("classpath:logo_angular.png");
	        Resource resGob = resourceLoader.getResource("classpath:logo_spring.png");
	        Resource resSis = resourceLoader.getResource("classpath:logo_app.png");
	        
	        parameters.put("LogoRight", ImageIO.read(resGob.getInputStream()));
	        parameters.put("LogoLeft", ImageIO.read(resAl.getInputStream()));
	        parameters.put("LogoSistema", ImageIO.read(resSis.getInputStream()));
	        parameters.put("CollectionBeanParam", dataSource);
	        
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        
	        
	        //load file and compile it
	        File file = ResourceUtils.getFile("classpath:employees.jrxml");
	        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());   
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
	        
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        
	        if (reportFormat.equalsIgnoreCase("html")) {
	            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\employees.html");
	        }
	        if (reportFormat.equalsIgnoreCase("pdf")) {
//	            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\employees.pdf");
	      
	            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
	        }


	        fileDTO.setBase64(Base64.getEncoder().encodeToString(outputStream.toByteArray()));
	        fileDTO.setFilename("reportEmployees.pdf");
	        fileDTO.setFiletype("application/pdf");
            return fileDTO;

	    }
}
