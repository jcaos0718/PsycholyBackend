package com.psicolApp.apirest.etities;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Table(name="psychologist", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class Psychologist implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Basic
    @Column(nullable = false)
    String username;

    private String password;

    private String lastname;
    private  String firstname;
    private  String country;


    @OneToMany(mappedBy = "psychologist", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Patient> patients;
    

    @OneToMany(mappedBy = "psychologist", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Diagnosis> diagnoses;

    @Enumerated(EnumType.STRING) 
    Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      return List.of(new SimpleGrantedAuthority((role.name())));
    }
    @Override
    public boolean isAccountNonExpired() {
       return true;
    }
    @Override
    public boolean isAccountNonLocked() {
       return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

  
}