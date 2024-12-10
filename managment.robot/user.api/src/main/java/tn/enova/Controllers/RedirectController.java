package tn.enova.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.enova.Services.AuthenticationService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/management-robot")
@AllArgsConstructor
public class RedirectController {
    private final AuthenticationService service;
    // GET method
    @GetMapping("/**")
    //@PreAuthorize("hasRole('Chief_Service')")
    //@PreAuthorize("hasRole('Patient')")
    public HashMap<String, String> handleGetRequest() {
        return new HashMap<String, String>(){{put("username",service.getUsername());}};
    }
    // POST method
    @PostMapping("/**")
    public HashMap<String, String> handlePostRequest() {
        return new HashMap<String, String>(){{put("username",service.getUsername());}};
    }
    // PUT method
    @PutMapping("/**")
    public HashMap<String, String> handlePutRequest() {
        return new HashMap<String, String>(){{put("username",service.getUsername());}};
    }
    // DELETE method
    @DeleteMapping("/**")
    public HashMap<String, String> handleDeleteRequest() {
        return new HashMap<String, String>(){{put("username",service.getUsername());}};
    }
}