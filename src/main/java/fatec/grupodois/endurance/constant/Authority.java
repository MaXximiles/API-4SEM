package fatec.grupodois.endurance.constant;

public class Authority {

    public static final String[] GUEST_AUTHORITIES = { "user:read" };
    public static final String[] ORACLE_AUTHORITIES = { "user:read",
                                                        "evento:create",
                                                        "user:update"
                                                      };
    public static final String[] ADMIN_AUTHORITIES = {
                                                        "user:read",
                                                        "user:create",
                                                        "user:delete",
                                                        "user:update",
                                                        "evento:create",
                                                        "evento:delete",
                                                        "evento:update"
                                                     };

}
