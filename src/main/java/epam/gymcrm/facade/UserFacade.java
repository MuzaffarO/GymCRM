package epam.gymcrm.facade;

import epam.gymcrm.dto.auth.JwtResponse;
import epam.gymcrm.dto.auth.LoginRequest;
import epam.gymcrm.dto.auth.PasswordChangeRequest;
import epam.gymcrm.dto.trainee.request.TraineeRegisterDto;
import epam.gymcrm.dto.trainer.request.TrainerRegisterDto;
import epam.gymcrm.dto.user.response.CredentialsInfoResponseDto;
import epam.gymcrm.security.JwtUtil;
import epam.gymcrm.security.TokenBlacklistService;
import epam.gymcrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    public CredentialsInfoResponseDto registerTrainer(TrainerRegisterDto dto) {
        return userService.registerTrainer(dto);
    }

    public CredentialsInfoResponseDto registerTrainee(TraineeRegisterDto dto) {
        return userService.registerTrainee(dto);
    }

    public JwtResponse login(LoginRequest request) {
        userService.login(request);
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);
        return new JwtResponse(jwt);
    }

    public void changeLogin(PasswordChangeRequest request) {
        userService.changeLogin(request);
    }

    public String logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return "No token provided.";
        }
        String token = authHeader.substring(7);
        long expiryMillis = jwtUtil.getRemainingExpirationMillis(token);
        tokenBlacklistService.blacklistToken(token, expiryMillis);
        return "Logged out and token blacklisted.";
    }
}
