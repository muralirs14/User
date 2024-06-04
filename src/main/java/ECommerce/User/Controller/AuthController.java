package ECommerce.User.Controller;

import ECommerce.User.Dto.LoginDto;
import ECommerce.User.Dto.SignUpDto;
import ECommerce.User.Entity.Role;
import ECommerce.User.Entity.UserLogin;
import ECommerce.User.Repository.RoleRepository;
import ECommerce.User.Repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserLoginRepository userLoginRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        // add check for username exists in a DB
        if(userLoginRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }
        // add check for email exists in DB
        if(userLoginRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already taken!",
                    HttpStatus.BAD_REQUEST);
        }
        // create user object
        UserLogin userLogin = new UserLogin();
        userLogin.setName(signUpDto.getName());
        userLogin.setUsername(signUpDto.getUsername());
        userLogin.setEmail(signUpDto.getEmail());
        userLogin.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        userLogin.setRoles(Collections.singleton(roles));
        userLoginRepository.save(userLogin);
        return new ResponseEntity<>("User registered successfully",HttpStatus.OK);
    }
    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);//once we login our user name and passwords will be rememberd for all the links secured
        return new ResponseEntity<>("User signed-in successfully!.",
                HttpStatus.OK);
    }



}
