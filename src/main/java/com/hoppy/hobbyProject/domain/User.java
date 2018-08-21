package com.hoppy.hobbyProject.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "enabled")
    private int enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(){
    }

    public User (User user){
        this.id = user.id;
        this.username = user.username;
        this.enabled = user.enabled;
    }

    public int getEnabled(){
        return enabled;
    }

    public Long getUserId(){
       return id;
    }

    public String getUsername(){
        return username;
    }

    public void setEnabled(int enabled){
        this.enabled = enabled;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public String[] getRolesNames() {
        ArrayList<String> rolesList = new ArrayList<>();
        roles.forEach(role -> {
            rolesList.add(role.getName());
        });
        String[] rolesNames = new String[rolesList.size()];
        rolesList.toArray(rolesNames);

        return rolesNames;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

