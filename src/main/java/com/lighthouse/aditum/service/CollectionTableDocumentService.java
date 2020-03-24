package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Company;
import com.lighthouse.aditum.service.dto.HouseYearCollectionDTO;
import com.lighthouse.aditum.service.mapper.CompanyMapper;
import com.lowagie.text.DocumentException;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class CollectionTableDocumentService {
    private static final String COMPANY = "company";
    private static final String BASE_URL = "baseUrl";
    private static final String COLLECTION = "collection";
    private static final String YEAR = "year";
    private static final String CURRENT_DATE = "currentDate";
    private static final String LOGO = "logo";
    private static final String LOGO_ADMIN = "logoAdmin";

    private final Logger log = LoggerFactory.getLogger(CollectionTableDocumentService.class);
    private final JHipsterProperties jHipsterProperties;
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;
    private final SpringTemplateEngine templateEngine;

    public CollectionTableDocumentService(SpringTemplateEngine templateEngine, JHipsterProperties jHipsterProperties,CompanyService companyService, CompanyMapper companyMapper){
        this.companyMapper = companyMapper;
        this.companyService = companyService;
        this.jHipsterProperties = jHipsterProperties;
        this.templateEngine = templateEngine;
    }
    public File obtainFileToPrint(Long companyId, String year,List<HouseYearCollectionDTO> collectionDTO) {
        Company company = companyMapper.companyDTOToCompany(companyService.findOne(companyId));
        String fileName = "Tabla Cobranza "+year+".pdf";
        try {
            Context contextTemplate = new Context();
            contextTemplate.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
            contextTemplate.setVariable(COMPANY,company);
            contextTemplate.setVariable(COLLECTION,collectionDTO);
            contextTemplate.setVariable(YEAR,year);
            ZonedDateTime date = ZonedDateTime.now();
            contextTemplate.setVariable(LOGO,company.getLogoUrl());
            contextTemplate.setVariable(LOGO_ADMIN,company.getAdminLogoUrl());
            String timeNowFormatted = DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mma").format(date);
            contextTemplate.setVariable(CURRENT_DATE,timeNowFormatted);
            String contentTemplate = templateEngine.process("collectionTableTemplate", contextTemplate);
            OutputStream outputStream = new FileOutputStream(fileName);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(contentTemplate);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            File file = new File(fileName);
            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
