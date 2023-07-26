package com.acrdev.acrcommerce.services;

import com.acrdev.acrcommerce.dto.UserDTO;
import com.acrdev.acrcommerce.entities.Role;
import com.acrdev.acrcommerce.entities.User;
import com.acrdev.acrcommerce.projections.UserDetailsProjection;
import com.acrdev.acrcommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

       List<UserDetailsProjection> result = repository.searchUserAndRolesByEmail(username);
        if(result.size() == 0){
            throw new UsernameNotFoundException("E-mail not found");
        }
        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());

        for(UserDetailsProjection projection : result){
            user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
        }
        return user;
    }

    //método auxiliar
    //retorna o usuário que está logado - query no repository
    protected User authenticated(){
       try {
           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
           Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
           String username = jwtPrincipal.getClaim("username");
           User user = repository.findByEmail(username).get(); //pegar o obj dentro do Optional
           return user;
       }
       catch (Exception e){
           throw new UsernameNotFoundException("E-mail not found");
       }
    }

    //pegar usuário Logado
    @Transactional(readOnly = true)
    public UserDTO getMe(){
        User user = authenticated(); //método acima
        return new UserDTO(user);
    }

}
