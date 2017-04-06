package games.util;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftBat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Fireball;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_8_R3.EntityFireball;
import net.minecraft.server.v1_8_R3.MovingObjectPosition;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;

public class GameRocket extends EntityFireball{

	public GameRocket(World world) {
        super((net.minecraft.server.v1_8_R3.World) world);
    }

    public String t() {
        return "";
    }

    public void e() {

    }

    public void move(double d0, double d1, double d2) {

    }

    
    public void g(double x, double y, double z) {
        Vector vector = this.getBukkitEntity().getVelocity();
        super.g(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Fireball spawn(Location location) {
        World mcWorld = (World) ((CraftWorld) location.getWorld()).getHandle();
        final GameRocket customEntity = new GameRocket(mcWorld);
        customEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ((CraftLivingEntity) customEntity.getBukkitEntity()).setRemoveWhenFarAway(false);
        ((CraftWorld) mcWorld).addEntity(customEntity, SpawnReason.CUSTOM);
        return (CraftFireball) customEntity.getBukkitEntity();
    }

	@Override
	protected void a(MovingObjectPosition arg0) {
		// TODO Auto-generated method stub
		
	}
}
