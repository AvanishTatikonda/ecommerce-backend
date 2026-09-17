package com.example.userservice.Controller;

import com.example.userservice.DTOS.*;
import com.example.userservice.Services.AuthSerivice;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import com.example.userservice.DTOS.SignUpResponseDto;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthSerivice authService;

    public AuthController(AuthSerivice authService){
         this.authService=authService;
    }
    @PostMapping("/sign_up")
    public ResponseEntity<SignUpResponseDto> signup(@RequestBody SignUpRequestDto request){
        SignUpResponseDto response=new SignUpResponseDto();
        try{
            if(authService.signUp(request.getEmail(),request.getPassword())){
                response.setRequestStatus(RequestStatus.SUCCESS);
            }
            else{
                response.setRequestStatus(RequestStatus.FAILURE);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            response.setRequestStatus(RequestStatus.FAILURE);
            return new ResponseEntity<>(response,HttpStatus.CONFLICT);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request){
        try{
            String token=authService.Login(request.getEmail(),request.getPassword());
            LoginResponseDto loginDto=new LoginResponseDto();
            loginDto.setRequestStatus(RequestStatus.SUCCESS);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Auth_Token", token);
            ResponseEntity<LoginResponseDto> response=new ResponseEntity<>(
                    loginDto,headers,HttpStatus.OK
            );
            return response;
        }
        catch (Exception e){
            e.printStackTrace();
            LoginResponseDto loginDto=new LoginResponseDto();
            loginDto.setRequestStatus(RequestStatus.FAILURE);
            ResponseEntity<LoginResponseDto> response=new ResponseEntity<>(
                    loginDto,HttpStatus.BAD_REQUEST
            );
            return response;
        }
    }
    @GetMapping("/validate")
    public boolean validate(@RequestParam("token") String token){
        System.out.println("Here I am");
        return authService.validate(token);

    }
}
