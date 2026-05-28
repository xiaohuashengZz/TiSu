package com.tisu.service;

import com.tisu.model.dto.LoginDTO;
import com.tisu.model.dto.RegisterDTO;
import com.tisu.model.vo.LoginVO;

public interface AuthService {

    LoginVO register(RegisterDTO dto);

    LoginVO login(LoginDTO dto);
}
