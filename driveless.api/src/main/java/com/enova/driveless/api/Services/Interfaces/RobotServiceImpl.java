package com.enova.driveless.api.Services.Interfaces;



import com.enova.driveless.api.Exceptions.RessourceNotFoundException;
import com.enova.driveless.api.Mappers.RobotMapper;
import com.enova.driveless.api.Models.Entitys.Robot;
import com.enova.driveless.api.Repositorys.RobotRepository;
import com.enova.driveless.api.Services.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("robot-service")
@RequiredArgsConstructor
public class RobotServiceImpl implements RobotService {

    private final RobotRepository robotRepository;


}

