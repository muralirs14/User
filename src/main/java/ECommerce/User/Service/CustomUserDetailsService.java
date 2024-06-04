package ECommerce.User.Service;

import ECommerce.User.Entity.Role;
import ECommerce.User.Entity.UserLogin;
import ECommerce.User.Repository.UserLoginRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserLoginRepository userLoginRepository;
    public CustomUserDetailsService(UserLoginRepository userLoginRepository) {
        this.userLoginRepository = userLoginRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws
            UsernameNotFoundException {
        UserLogin userLogin = userLoginRepository.findByUsernameOrEmail(usernameOrEmail,
                        usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));
        return new
                org.springframework.security.core.userdetails.User(userLogin.getEmail(),
                userLogin.getPassword(), mapRolesToAuthorities(userLogin.getRoles()));
    }
    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new
                SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}