package com.przemarcz.auth;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
class ResourceController {

    @GetMapping("/me")
    public Principal getPrincipal(Principal principal) {
        return principal;
    }

}
