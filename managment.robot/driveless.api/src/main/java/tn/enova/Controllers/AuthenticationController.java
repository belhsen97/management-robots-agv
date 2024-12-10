package tn.enova.Controllers;

import tn.enova.Enums.StatusRobotAuth;
import tn.enova.Models.Commons.Robot;
import tn.enova.Models.Requests.AuthenticationRequest;
import tn.enova.Models.Responses.AuthenticationResponse;
import tn.enova.Services.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    //http://192.168.1.8:8089/management-robot/driveless-service/authentication/sign-in
    //http://192.168.1.8:8088/management-robot-avg/driveless/authentication/sign-in
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