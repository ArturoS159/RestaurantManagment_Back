package com.przemarcz.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
class ResourceController {

    @GetMapping("/me")
    public Principal getPrincipal(Principal principal) {
        return principal;
    }

}
