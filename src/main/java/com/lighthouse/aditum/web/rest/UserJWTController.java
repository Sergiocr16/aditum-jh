package com.lighthouse.aditum.web.rest;

import com.lighthouse.aditum.domain.Authority;
import com.lighthouse.aditum.domain.OfficerAccount;
import com.lighthouse.aditum.domain.User;
import com.lighthouse.aditum.security.AuthoritiesConstants;
import com.lighthouse.aditum.security.jwt.JWTConfigurer;
import com.lighthouse.aditum.security.jwt.TokenProvider;
import com.lighthouse.aditum.service.*;
import com.lighthouse.aditum.service.dto.*;
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

    private OfficerAccountService officerAccountService;

    private AdminInfoService managerService;

    private CompanyService companyService;

    private ResidentService residentService;

    private JuntaDirectivaAccountService juntaDirectivaAccountService;

    private MacroOfficerAccountService macroOfficerAccountService;

    private MacroCondominiumService macroCondominiumService;

    private MacroAdminAccountService macroAdminAccountService;

    public UserJWTController(MacroAdminAccountService macroAdminAccountService,TokenProvider tokenProvider, AuthenticationManager authenticationManager,UserService userService,OfficerAccountService officerAccountService,CompanyService companyService,AdminInfoService managerService,ResidentService residentService,JuntaDirectivaAccountService juntaDirectivaAccountService, MacroOfficerAccountService macroOfficerAccountService, MacroCondominiumService macroCondominiumService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.officerAccountService = officerAccountService;
        this.companyService = companyService;
        this.managerService = managerService;
        this.residentService = residentService;
        this.juntaDirectivaAccountService = juntaDirectivaAccountService;
        this.macroOfficerAccountService = macroOfficerAccountService;
        this.macroCondominiumService = macroCondominiumService;
        this.macroAdminAccountService = macroAdminAccountService;
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
                    case AuthoritiesConstants.MANAGER:
//                        AdminInfoDTO manager = managerService.findOneByUserId(user.getId());
//                        if(this.companyService.findOne(manager.getCompanyId()).getActive() == 1){
                            activeCompany = true;
//                        }
                        break;
                    case AuthoritiesConstants.OFFICER:
                        OfficerAccountDTO officer = officerAccountService.findOneByUserId(user.getId());
                        if(this.companyService.findOne(officer.getCompanyId()).getActive() == 1){
                            activeCompany = true;
                        }
                        break;
                    case AuthoritiesConstants.USER:
                        ResidentDTO resident = residentService.findOneByUserId(user.getId());
                        if(this.companyService.findOne(resident.getCompanyId()).getActive() == 1){
                            activeCompany = true;
                        }
                        break;
                    case AuthoritiesConstants.OWNER:
                        ResidentDTO owner = residentService.findOneByUserId(user.getId());
                        if(this.companyService.findOne(owner.getCompanyId()).getActive() == 1){
                            activeCompany = true;
                        }
                        break;
                    case AuthoritiesConstants.RH:
                            activeCompany = true;
                        break;
                    case AuthoritiesConstants.JD:
                        JuntaDirectivaAccountDTO juntaDirectivaAccountDTO = juntaDirectivaAccountService.findOneByUserId(user.getId());
                        if(this.companyService.findOne(juntaDirectivaAccountDTO.getCompanyId()).getActive() == 1){
                            activeCompany = true;
                        }
                        break;
                    case AuthoritiesConstants.OFFICERMACRO:
                        MacroOfficerAccountDTO macroOfficerAccountDTO = macroOfficerAccountService.findOneByUserId(user.getId());
                        if(this.macroCondominiumService.findOne(macroOfficerAccountDTO.getMacroCondominiumId()).isEnabled()){
                            activeCompany = true;
                        }
                        break;
                    case AuthoritiesConstants.MANAGERMACRO:
                        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountService.findOneByUserId(user.getId());
                        if(this.macroCondominiumService.findOne(macroAdminAccountDTO.getMacroCondominiumId()).isEnabled() && macroAdminAccountDTO.isEnabled()){
                            activeCompany = true;
                        }
                        break;
                    case AuthoritiesConstants.MANAGERAR:
//                        MacroAdminAccountDTO macroAdminAccountDTO = macroAdminAccountService.findOneByUserId(user.getId());
//                        if(this.macroCondominiumService.findOne(macroAdminAccountDTO.getMacroCondominiumId()).isEnabled() && macroAdminAccountDTO.isEnabled()){
                            activeCompany = true;
//                        }
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
