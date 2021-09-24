package com.windows.Reinforcement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Reinforcement extends JavaPlugin implements Listener {
	
	String prefix = "§f§l[ §e§l소망온라인 §f§l] §f";
	String n = "§9§l블랙큐브";
	List<String> lore = new ArrayList<String>();
	List<String> lore2 = new ArrayList<String>();
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		Bukkit.getConsoleSender().sendMessage(prefix + "§aReinforcement 활성화");
	}
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(prefix + "§cReinforcement 비활성화");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	    Player player = (Player)sender;
		if (label.equalsIgnoreCase("강화")) {
			if (args.length == 0) {
				return false;
			}
			if (args.length == 1) {
				if (!player.isOp()) {
					player.sendMessage(prefix + "§c당신은 권한이 없습니다.");
					return false;
				}
				if (args[0].equalsIgnoreCase("블랙큐브")) {
					ItemStack cube = new ItemStack(Material.BEACON, 1);
					ItemMeta meta = cube.getItemMeta();
					meta.setDisplayName(n);
					meta.setLore(Arrays.asList(new String[] { "§e[-] §f아이템을 강화하는 데 쓰입니다.", "§e[-] §f강화를 원하신다면 이 큐브를 집어서 강화할 아이템을 클릭해주세요!", "§c[!] §e남은 강화가능횟수가 남아있는 아이템만 강화할 수 있습니다." }));
					cube.setItemMeta(meta);
					player.getInventory().addItem(new ItemStack[] { cube });
					return false;
				}
				return false;
			}
			return false;
		}
		return false;
	}
	
	public void lorecheck(InventoryClickEvent event) {
		lore.clear();
		for (int n = 0; n < event.getCurrentItem().getItemMeta().getLore().size(); n++) {
			String a = event.getCurrentItem().getItemMeta().getLore().get(n);
			lore.add(a);
		}
	}
	
	public void lorecheck2(InventoryClickEvent event) {
		lore2.clear();
		for (int n = 0; n < event.getCurrentItem().getItemMeta().getLore().size(); n++) {
			String a = event.getCurrentItem().getItemMeta().getLore().get(n);
			if (a.split(" ")[0].equals("§e[강화]")) {
				lore2.add(a);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent event) {
		Player player = (Player)event.getWhoClicked();
		if (event.getCursor().getType() == Material.BEACON && event.getCursor().hasItemMeta() && event.getCursor().getItemMeta().hasDisplayName() && event.getCursor().getItemMeta().getDisplayName().equalsIgnoreCase(n)) {
			int get = event.getCurrentItem().getTypeId();
	        if (get >= 298 && get <= 317) {
				if (event.getCursor().getAmount() != 1) {
					player.sendMessage(prefix + "하나만 집어서 클릭해주세요.");
					return;
				}
	        	if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore()) {
					for (int n = 0; n < event.getCurrentItem().getItemMeta().getLore().size(); n++) {
						String a = event.getCurrentItem().getItemMeta().getLore().get(n);
						lorecheck(event);
						if (a.split(" ")[0].equals("§9[-]§f")) {
							String b = a.split(" ")[4];
							int c = Integer.parseInt(b.replaceAll("회", ""));
							if (c >= 1) {
								lore.set(n, "§9[-]§f 남은 강화가능횟수 : " + (c-=1) + "회");
								Random random = new Random();
							    int random2 = random.nextInt(100);
							    if (random2 <= 5) {
							    	lore.add("§e[강화] +3 체력");
							    } else if (random2 <= 10) {
							    	lore.add("§e[강화] +3 방어력");
							    } else if (random2 <= 15) {
							    	lore.add("§e[강화] +2 체력");
							    } else if (random2 <= 20) {
							    	lore.add("§e[강화] +2 방어력");
							    } else if (random2 <= 75) {
							    	lore.add("§e[강화] +1 체력");
							    } else if (random2 <= 100) {
							    	lore.add("§e[강화] +1 방어력");
							    }
							    event.setCursor(null);
							    event.setCancelled(true);
								ItemStack item = event.getCurrentItem();
						        ItemMeta meta = item.getItemMeta();
						        meta.setLore(lore);
						        item.setItemMeta(meta);
								if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName() != null) {
									player.sendMessage("");
									player.sendMessage(prefix + "§r" + event.getCurrentItem().getItemMeta().getDisplayName() + " §f에 강화 기운이 스며들었습니다.");
									Bukkit.broadcastMessage("");
									Bukkit.broadcastMessage(prefix + "§e" + player.getName() + " §f님이 §f" + event.getCurrentItem().getItemMeta().getDisplayName() + " 을(를) 블랙큐브로 강화하셨습니다.");
									lorecheck2(event);
									Bukkit.broadcastMessage(prefix + "§c▒ 현재 강화옵션");
									for (int t = 0; t < lore2.size(); t++) {
										Bukkit.broadcastMessage(prefix + lore2.get(t));
									}
									return;
								}
							} else {
								player.sendMessage(prefix + "남은 강화가능횟수가 없는 아이템입니다.");
								return;
							}
						}
					}
	        	}
	        } else if ((get == 267) || 
	                (get == 268) || 
	                (get == 272) || 
	                (get == 276) || 
	                (get == 283)) {
				if (event.getCursor().getAmount() != 1) {
					player.sendMessage(prefix + "하나만 집어서 클릭해주세요.");
					return;
				}
	        	if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasLore()) {
					for (int n = 0; n < event.getCurrentItem().getItemMeta().getLore().size(); n++) {
						String a = event.getCurrentItem().getItemMeta().getLore().get(n);
						lorecheck(event);
						if (a.split(" ")[0].equals("§9[-]§f")) {
							String b = a.split(" ")[4];
							int c = Integer.parseInt(b.replaceAll("회", ""));
							if (c >= 1) {
								lore.set(n, "§9[-]§f 남은 강화가능횟수 : " + (c-=1) + "회");
								Random random = new Random();
							    int random2 = random.nextInt(100);
							    if (random2 <= 5) {
							    	lore.add("§e[강화] +3 공격력");
							    } else if (random2 <= 20) {
							    	lore.add("§e[강화] +2 공격력");
							    } else if (random2 <= 100) {
							    	lore.add("§e[강화] +1 공격력");
							    }
							    event.setCursor(null);
							    event.setCancelled(true);
								ItemStack item = event.getCurrentItem();
						        ItemMeta meta = item.getItemMeta();
						        meta.setLore(lore);
						        item.setItemMeta(meta);
								if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().getDisplayName() != null) {
									player.sendMessage("");
									player.sendMessage(prefix + "§r" + event.getCurrentItem().getItemMeta().getDisplayName() + " §f에 강화 기운이 스며들었습니다.");
									Bukkit.broadcastMessage("");
									Bukkit.broadcastMessage(prefix + "§e" + player.getName() + " §f님이 §f" + event.getCurrentItem().getItemMeta().getDisplayName() + " 을(를) 블랙큐브로 강화하셨습니다.");
									lorecheck2(event);
									Bukkit.broadcastMessage(prefix + "§c▒ 현재 강화옵션");
									for (int t = 0; t < lore2.size(); t++) {
										Bukkit.broadcastMessage(prefix + lore2.get(t));
									}
									if (!player.hasPermission("tutorial")) {
										player.sendMessage("");
										player.sendMessage(prefix + "잘하셨습니다!");
										player.sendMessage(prefix + "다음단계로 이동됩니다.");
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eci " + player.getName());
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sudo " + player.getName() + " c:/#이동워프즈# tutorial1");
										return;
									}
									return;
								}
							} else {
								player.sendMessage(prefix + "남은 강화가능횟수가 없는 아이템입니다.");
								return;
							}
						}
					}
	        	}
	        } else {
	        	if (event.getCurrentItem().getType() != Material.AIR) {
	        		player.sendMessage(prefix + "강화가 불가능한 아이템입니다.");
	        		return;
	        	}
	        }
		}
		//player.sendMessage(event.getCurrentItem().getType() + ""); // 놓은곳에 있던 물건
		//player.sendMessage("------");
		//player.sendMessage(event.getCursor().getType() + ""); // 집었던물건
	}
	

}
