package hello.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hello.entity.User;
import hello.repository.LogInRepository;

@Service
public class LogInService {

	@Autowired
	private LogInRepository logInRepository;
	
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		logInRepository.findAll().forEach(users::add);
		return users;
	}
	
	public Optional<User> getUserById(Integer id) {	
		return logInRepository.findById(id);
	}
}
