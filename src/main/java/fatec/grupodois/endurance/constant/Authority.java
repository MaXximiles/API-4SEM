package fatec.grupodois.endurance.constant;

public class Authority {

    public static final String[] GUEST_AUTHORITIES = { "user:read",
                                                        "evento: read"
                                                     };
    public static final String[] ORACLE_AUTHORITIES = { "user:read",
                                                        "evento:create",
                                                        "evento: read",
                                                        "user:update"
                                                      };
    public static final String[] ADMIN_AUTHORITIES = {
                                                        "user:read",
                                                        "user:create",
                                                        "user:delete",
                                                        "user:update",
                                                        "evento: read",
                                                        "evento:create",
                                                        "evento:delete",
                                                        "evento:update"
                                                     };

}
