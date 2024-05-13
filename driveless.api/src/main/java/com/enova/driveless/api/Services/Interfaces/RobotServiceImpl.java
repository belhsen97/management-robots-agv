package com.enova.driveless.api.Services.Interfaces;




import com.enova.driveless.api.Repositorys.RobotRepository;
import com.enova.driveless.api.Services.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("robot-service")
@RequiredArgsConstructor
public class RobotServiceImpl implements RobotService {

    private final RobotRepository robotRepository;


}

