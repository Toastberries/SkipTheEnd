package xyz.toastberries.skiptheend;

public class TeleportContext {
    // probably doesn't need to be ThreadLocal, since server logic should be single thread, but just in case
    public static final ThreadLocal<Boolean> portalTravelingToEnd = ThreadLocal.withInitial(() -> false);
}
