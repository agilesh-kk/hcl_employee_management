
package service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import entity.User;
import entity.Role;
import repository.UserRepository;
import repository.RoleRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AdminService(
            UserRepository userRepository,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public String promoteUserToAdmin(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() ->
                        new IllegalStateException("ROLE_ADMIN not found"));

        user.setRole(adminRole);

        userRepository.save(user);

        return "User promoted to ADMIN successfully";
    }
}
