package cn.ksmcbrigade.scc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.UUID;


@Mod(SwordOfTheCosmosChanged.MODID)
public class SwordOfTheCosmosChanged {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "scc";

    public static ArrayList<UUID> uuids = new ArrayList<>();

    public SwordOfTheCosmosChanged() {
        MinecraftForge.EVENT_BUS.register(this);
    }
}
