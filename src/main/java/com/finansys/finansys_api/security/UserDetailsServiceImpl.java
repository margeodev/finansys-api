package com.finansys.finansys_api.security;

import com.finansys.finansys_api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    return userRepository.findByEmailAndIsActiveTrue(email)
            .map(UserAuthenticated::new)
            .orElseThrow(
                    () -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
  }

}
