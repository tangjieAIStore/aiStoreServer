package cn.aistore.ai.common;

public class SystemThreadLocal {

    private static final ThreadLocal<Integer> UserId = new ThreadLocal<>();

    private static final ThreadLocal<Long> TenantId = new ThreadLocal<>();

    public static void setUserId(Integer value) {
        UserId.set(value);
    }

    public static Integer getUserId() {
        if (UserId.get() == null){
            return 0;
        }
        return UserId.get();
    }

    public static void removeUserId() {

        UserId.remove();
    }

    // TenanId 增删改
    public static void setTenantId(Long value) {
        TenantId.set(value);
    }

    public static Long getTenantId() {
        if (TenantId.get() == null) {
            return 0L;
        }
        return TenantId.get();
    }

    public static void removeTenantId() {
        TenantId.remove();
    }
}
