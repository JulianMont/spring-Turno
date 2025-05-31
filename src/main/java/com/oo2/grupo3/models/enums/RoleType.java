package com.oo2.grupo3.models.enums;

public enum RoleType {

    ADMIN,
    USER;

    public String getPrefixedName() {
        return "ROLE_" + this.name();
    }
}
