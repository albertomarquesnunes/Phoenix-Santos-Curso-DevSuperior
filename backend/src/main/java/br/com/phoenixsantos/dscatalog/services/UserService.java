package br.com.phoenixsantos.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.phoenixsantos.dscatalog.dto.RoleDTO;
import br.com.phoenixsantos.dscatalog.dto.UserDTO;
import br.com.phoenixsantos.dscatalog.dto.UserInsertDTO;
import br.com.phoenixsantos.dscatalog.entities.Role;
import br.com.phoenixsantos.dscatalog.entities.User;
import br.com.phoenixsantos.dscatalog.repositories.RoleRepository;
import br.com.phoenixsantos.dscatalog.repositories.UserRepository;
import br.com.phoenixsantos.dscatalog.services.exceptions.DatabaseException;
import br.com.phoenixsantos.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
		
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable){
		Page<User> list = repository.findAll(pageable);
		return  list.map(x -> new UserDTO(x));
		
	}


	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(()-> new ResourceNotFoundException("Não Encontrado"));
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto,entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserDTO dto) {
		try {
				User entity = repository.getOne(id);
				copyDtoToEntity(dto,entity);
				entity = repository.save(entity);
				return new UserDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not Found " + id);
		}
	
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e)
		{
			throw new ResourceNotFoundException("Id not Fount " + id);
		
		}
		catch(DataIntegrityViolationException e)
		{
			throw new DatabaseException("Integrity violation");
		}
	}
	

	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		
		entity.getRoles().clear();
		for(RoleDTO roleDto : dto.getRoles()) {
		Role role = roleRepository.getOne(roleDto.getId());
		entity.getRoles().add(role);
		}
	}

}