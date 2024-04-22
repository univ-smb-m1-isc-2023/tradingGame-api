package tyg.tradinggame.tradinggame.security.controller;

import org.springframework.web.bind.annotation.*;
import tyg.tradinggame.tradinggame.security.dao.request.SignUpRequest;
import tyg.tradinggame.tradinggame.security.dao.request.SigninRequest;
import tyg.tradinggame.tradinggame.security.dao.response.JwtAuthenticationResponse;
import tyg.tradinggame.tradinggame.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signup(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authenticationService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SigninRequest request) {
        return ResponseEntity.ok(authenticationService.signin(request));
    }

}

