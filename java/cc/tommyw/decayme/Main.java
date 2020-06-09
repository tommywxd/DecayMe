package cc.tommyw.decayme;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public final class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new Events(this), this);
		this.getCommand("start").setExecutor(new Start(this));
	}

	@Override
	public void onDisable() {
			File started = new File("started");
			started.delete();
		}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		return false;
	}
	
}
