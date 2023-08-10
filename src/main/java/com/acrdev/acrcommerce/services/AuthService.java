package com.acrdev.acrcommerce.services;

import com.acrdev.acrcommerce.entities.User;
import com.acrdev.acrcommerce.services.exceptions.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    //regras de negócio para controle de acesso

    @Autowired
    private UserService userService;

    public void validateSelfOrAdmin(Long userId){
        User me = userService.authenticated();

        if(!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)){
            throw  new ForbiddenException("Access denide");
        }

    }

}
