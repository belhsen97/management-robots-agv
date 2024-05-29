package com.enova.driveless.api.Controllers;

import com.enova.driveless.api.Enums.StatusRobotAuth;
import com.enova.driveless.api.Models.Entitys.Robot;
import com.enova.driveless.api.Models.Requests.AuthenticationRequest;
import com.enova.driveless.api.Models.Responses.AuthenticationResponse;
import com.enova.driveless.api.Services.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final RobotService robotService;
//https://www.emqx.io/docs/en/latest/access-control/authz/http.html
    @PostMapping("/sign-in")
    public   ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)  {
        System.out.println(request.toString());
        if (request.getUsername() == "admin" && request.getPassword() == "admin" ){
            return ResponseEntity.ok(AuthenticationResponse.builder().result(StatusRobotAuth.allow).is_superuser(true).build());
        }
        if (request.getUsername() == "user" && request.getPassword() == "user" ){
            return ResponseEntity.ok(AuthenticationResponse.builder().result(StatusRobotAuth.allow).is_superuser(false).build());
        }
        Optional<Robot> r =  robotService.selectByUsernameAndPassword(request.getUsername(),request.getPassword());
        if ( r.isPresent() ){
            return ResponseEntity.ok(
                    AuthenticationResponse.builder()
                            .result(StatusRobotAuth.allow)
                            .is_superuser(false)
                            .build()
            );
        }
        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .result(StatusRobotAuth.ignore)
                        .is_superuser(false)
                        .build()
        );
    }
}