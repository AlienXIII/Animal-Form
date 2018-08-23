package com.hoppy.hobbyProject.config;

import com.hoppy.hobbyProject.Repo.UserRepository;
import com.hoppy.hobbyProject.domain.User;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//interfejs który spring sam sobie ogarnia
//Ty jedynie musisz mu zaimplementować lodByUsername bo to jego jedyna metoda

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository; //żeby usera wyssać :D

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);//User w twoim domain
        UserBuilder userBuilder; //narzędzie to budowania usera, ale tego ze springSecurity
        if(user != null){
            userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
            //budujemy usera Springsecurity na podstawie tego z bazy
            userBuilder.password(user.getPassword()); //pass z bazy do usera w ss
            userBuilder.roles(user.getRolesNames()); //nazwy ról np USER ADMIN MANAGER itp
            if(!userBuilder.build().isEnabled() || !user.getEnabled()){
                throw new DisabledException("User is disabled...");
            }
        }else {
            throw new UsernameNotFoundException("User not found"); //wypierdol błąd jeśli nima usera
        }

        return userBuilder.build();
    }
}
