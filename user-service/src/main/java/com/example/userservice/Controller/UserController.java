package com.example.userservice.Controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;

@RestController
@RequestMapping("/user-service")
public class UserController {

	private Environment env;
	private UserService userService;
	
	@Autowired
	public UserController(Environment env, UserService userService) {
		this.env = env;
		this.userService = userService;
	}
	
	
	@Autowired
	private Greeting greeting;
	
	
	@GetMapping("/health_check")
	public String status(HttpServletRequest request) {
		return String.format("It's Working in User Service %s", env.getProperty("local.server.port"));
	}
	
	@GetMapping("/welcome")
	public String welcome() {
		return greeting.getMessage();
	}
	
	@GetMapping("/welcome2")
	public String welcome2() {
		return env.getProperty("greeting.message");
	}
	
	/* 회원가입 */
	@PostMapping("/users")
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserDto userDto = mapper.map(user, UserDto.class);
		userService.createUser(userDto);
		
		ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
	}
	
	/* 모든 유저에 대한 정보 출력 */
	@GetMapping("/users")
	public ResponseEntity<List<ResponseUser>> getUsers(){
		Iterable<UserEntity> userList = userService.getUserByAll();
		
		List<ResponseUser> result = new ArrayList<>();
		
		userList.forEach(v -> {
			result.add(new ModelMapper().map(v, ResponseUser.class));
		});
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	/* 유저ID로 유저에 대한 정보 출력*/
	@GetMapping("/users/{users}")
	public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId){
		
		UserDto userDto = userService.getUserByUserId(userId);
		
		ResponseUser result = new ModelMapper().map(userDto, ResponseUser.class);
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
