package tyg.tradinggame.tradinggame.security.service;

import tyg.tradinggame.tradinggame.security.dao.request.SignUpRequest;
import tyg.tradinggame.tradinggame.security.dao.request.SigninRequest;
import tyg.tradinggame.tradinggame.security.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);

    void deleteUser(Long id);
}
