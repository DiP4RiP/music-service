package ru.dip4rip.musicservice.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "full_name", length = 100)
  private String fullName;

  @Column(name = "address", length = 200)
  private String address;

  @Column(name = "phone", length = 20)
  private String phone;

  @Column(name = "login", length = 50)
  private String login;

  @Column(name = "password", length = 255)
  private String password;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Playlist> playlists;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role = Role.USER;

}