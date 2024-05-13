package com.enova.driveless.api.Controllers;

import com.enova.driveless.api.Enums.StatusRobotAuth;
import com.enova.driveless.api.Models.Requests.AuthenticationRequest;
import com.enova.driveless.api.Models.Responses.AuthenticationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authentication")
public class AuthenticationController {

//https://www.emqx.io/docs/en/latest/access-control/authz/http.html
    @PostMapping("/sign-in")
    public   ResponseEntity<AuthenticationResponse> authenticateRobot(@RequestBody AuthenticationRequest request)  {
        System.out.println(request);
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .result(StatusRobotAuth.allow)
                        .is_superuser(true)
                        .build()
        );
    }
}