package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.domain.Authority;
import com.lighthouse.aditum.domain.User;
import com.lighthouse.aditum.security.AuthoritiesConstants;
import com.lighthouse.aditum.security.jwt.JWTConfigurer;
import com.lighthouse.aditum.security.jwt.TokenProvider;
import com.lighthouse.aditum.service.*;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;
import com.lighthouse.aditum.service.dto.OfficerDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.web.rest.vm.LoginVM;

import java.util.Collections;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private UserService userService;

    private OfficerService officerService;

    private AdminInfoService managerService;

    private CompanyService companyService;

    private ResidentService residentService;

    public UserJWTController(TokenProvider tokenProvider, AuthenticationManager authenticationManager,UserService userService,OfficerService officerService,CompanyService companyService,AdminInfoService managerService,ResidentService residentService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.officerService = officerService;
        this.companyService = companyService;
        this.managerService = managerService;
        this.residentService = residentService;
    }

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
            String jwt = tokenProvider.createToken(authentication, rememberMe);
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
             boolean  activeCompany = false;
             User user = this.userService.getUserWithAuthoritiesByLogin(loginVM.getUsername()).get();
            for(Authority a : user.getAuthorities()) {
                switch (a.getName()){
                    case AuthoritiesConstants.ADMIN:
                        activeCompany = true;
                        break;
                    case AuthoritiesConstants.OFFICER:
                         OfficerDTO officer = officerService.findOneByUserId(user.getId());
                         if(this.companyService.findOne(officer.getCompanyId()).getActive() == 1){
                             activeCompany = true;
                         }
                        break;
                    case AuthoritiesConstants.MANAGER:
                        AdminInfoDTO manager = managerService.findOneByUserId(user.getId());
                        if(this.companyService.findOne(manager.getCompanyId()).getActive() == 1){
                            activeCompany = true;
                        }
                        break;
                    case AuthoritiesConstants.USER:
                        ResidentDTO resident = residentService.findOneByUserId(user.getId());
                        if(this.companyService.findOne(resident.getCompanyId()).getActive() == 1){
                            activeCompany = true;
                        }
                        break;
                }
            }
            if(activeCompany) {
                return ResponseEntity.ok(new JWTToken(jwt));
            }else{
                return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",null), HttpStatus.UNAUTHORIZED);
            }
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(Collections.singletonMap("AuthenticationException",exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
