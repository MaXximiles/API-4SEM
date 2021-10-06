package fatec.grupodois.endurance.enumeration;

import static fatec.grupodois.endurance.constant.Authority.*;


public enum Role {
    ROLE_GUEST(GUEST_AUTHORITIES),
    ROLE_ORACLE(ORACLE_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String... authorities) { this.authorities = authorities; }

    public String[] getAuthorities() { return authorities; }

}
