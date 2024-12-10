package tn.enova.Securitys;

import tn.enova.Models.Entitys.Token;
import tn.enova.Repositorys.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Service
@RequiredArgsConstructor
public class LogoutService  implements LogoutHandler {

private final UserRepository userRepository;

  @Override
  public void logout(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication
  ) {
    final String authHeader = request.getHeader("Authorization");

    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    final String jwt = authHeader.substring(7);
    Token storedToken = userRepository.findTokenByTokenValue(jwt).orElse(null);
    if (storedToken != null) {
      storedToken.setExpired(true);
      storedToken.setRevoked(true);
      userRepository.updateToken(storedToken.getToken()  ,storedToken);
      SecurityContextHolder.clearContext();
    }
  }
}
