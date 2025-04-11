package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Address;
import com.example.entity.User;
import com.example.repository.AddressRepository;
import com.example.repository.UserRepository;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "*")
public class AddressController {

    @Autowired private AddressRepository addressRepo;
    @Autowired private UserRepository userRepo;

    @PostMapping("/add")
    public ResponseEntity<?> addAddress(@RequestBody Address address, @RequestParam String username) {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            address.setUser(user);
            return ResponseEntity.ok(addressRepo.save(address));
        }
        return ResponseEntity.badRequest().body("User not found");
    }

    @GetMapping("/{username}")
    public List<Address> getAddresses(@PathVariable String username) {
        return addressRepo.findByUserUsername(username);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @RequestBody Address updatedAddress) {
        return addressRepo.findById(id).map(address -> {
            address.setStreet(updatedAddress.getStreet());
            address.setCity(updatedAddress.getCity());
            address.setState(updatedAddress.getState());
            address.setZipCode(updatedAddress.getZipCode());
            address.setCountry(updatedAddress.getCountry());
            return ResponseEntity.ok(addressRepo.save(address));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        addressRepo.deleteById(id);
        return ResponseEntity.ok("Address deleted");
    }
}
