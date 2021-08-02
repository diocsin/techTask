package by.ilyushenko.tech.controller;

import by.ilyushenko.tech.configure.security.JwtTokenUtil;
import by.ilyushenko.tech.model.AuthenticationRequest;
import by.ilyushenko.tech.model.AuthenticationResponse;
import by.ilyushenko.tech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
            final String jwt = jwtTokenUtil.generateToken(userDetails);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (BadCredentialsException e) {
            System.err.println("Incorrect username or password");
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.FORBIDDEN);
        } catch (UsernameNotFoundException e) {
            System.err.println("User not found");
            return new ResponseEntity<>("User not found", HttpStatus.FORBIDDEN);
        }
    }
}
