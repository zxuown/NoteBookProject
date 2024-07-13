    package com.example.notebook.controller;


    import com.example.notebook.dto.AuthRequest;
    import com.example.notebook.dto.JwtUtil;
    import com.example.notebook.entity.User;
    import com.example.notebook.service.AuthService;
    import com.example.notebook.service.UserService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.web.bind.annotation.*;

    @RestController
    public class AuthController {

        private final UserService userService;
        private final AuthService authService;
        private final JwtUtil jwtUtil;

        @Autowired
        public AuthController(UserService userService, AuthService authService, JwtUtil jwtUtil) {
            this.userService = userService;
            this.authService = authService;
            this.jwtUtil = jwtUtil;
        }

        @PostMapping("/auth/login")
        public String submitLogin(@RequestBody AuthRequest request) {
            if (new BCryptPasswordEncoder().matches(request.getPassword(),
                    userService.loadUserByUsername(request.getEmail()).getPassword())){
                return authService.authenticate(request.getEmail());
            }
            return null;
        }

        @GetMapping("/auth/user/token/{userToken}")
        public User submitLogin(@PathVariable String userToken) {
            return userService.loadUserByUsername(jwtUtil.getEmailFromToken(userToken));
        }

        @PostMapping("/auth/register")
        public String registerUser(@RequestBody AuthRequest request) {
            if (userService.emailExists(request.getEmail())) {
                return null;
            }

            userService.create(request.getEmail(), request.getPassword());
            return  authService.authenticate(request.getEmail());
        }


    }
