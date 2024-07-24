package com.enova.notification.api.Controllers;


import com.enova.notification.api.Services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationRestController {
    private final NotificationService notificationService;
    @GetMapping()
    public Object GetAll() {
        return null;
    }

    @PostMapping("/sign-in")
    public   ResponseEntity<?> aaaaaaaaaaaaa(@RequestBody Object request)  {
        return null;
    }

}