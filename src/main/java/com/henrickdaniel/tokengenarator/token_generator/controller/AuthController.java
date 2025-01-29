package com.henrickdaniel.tokengenarator.token_generator.controller;

import com.henrickdaniel.tokengenarator.token_generator.model.AuthResponseDTO;
import com.henrickdaniel.tokengenarator.token_generator.model.LoginDto;
import com.henrickdaniel.tokengenarator.token_generator.model.RegisterDTO;
import com.henrickdaniel.tokengenarator.token_generator.repository.RoleRepository;
import com.henrickdaniel.tokengenarator.token_generator.repository.UserRepository;
import com.henrickdaniel.tokengenarator.token_generator.security.JwtGenarator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import com.henrickdaniel.tokengenarator.token_generator.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenarator jwtGenarator;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO){

        if(userRepository.existsByName(registerDTO.getName())){
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }
        User user = new User();

        user.setName(registerDTO.getName());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("USER").get()));

        userRepository.save(user);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto loginDto) throws Exception{

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getName(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtGenarator.generateToken(authentication);

        return new ResponseEntity<>(AuthResponseDTO.builder()
                .acessToken(token)
                .tokenType("Bearer")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/say-hello")
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello World");
    }

}
