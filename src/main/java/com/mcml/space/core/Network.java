package com.mcml.space.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.mcml.space.config.Core;
import com.mcml.space.util.AzureAPI;

/**
 * @author Vlvxingze
 */
public class Network implements Runnable {
    public static void init(Plugin plugin) {
        if (!Core.AutoUpdate) return;
        
        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Network());
        AzureAPI.log("自动更新核心模块已启用");
    }

    public static void CheckAndDownloadPlugin() {
        if (Core.AutoUpdate == true) {
            try {
                // 整体获取
                File NetworkerFile = new File(EscapeLag.plugin.getDataFolder(), "networkerlog");
                DowloadFile("http://www.relatev.com/files/EscapeLag/NetWorker.yml", NetworkerFile);
                YamlConfiguration URLLog = YamlConfiguration.loadConfiguration(NetworkerFile);
                // 检查插件并下载新版本
                EscapeLag.plugin.getLogger().info("正在检查新版本插件，请稍等...");
                int NewVersion = URLLog.getInt("UpdateVersion");
                int NowVersion = Integer.valueOf(Core.internalVersion);
                if (NewVersion > NowVersion) {
                    EscapeLag.plugin.getLogger().info("插件检测到新版本 " + NewVersion + "，正在自动下载新版本插件...");
                    DowloadFile("http://www.relatev.com/files/EscapeLag/EscapeLag.jar", EscapeLag.getPluginFile());
                    EscapeLag.plugin.getLogger().info("插件更新版本下载完成！正在重启服务器！");
                    AzureAPI.restartServer("服务器内容更新!请过一会重新进服吧!");
                } else {
                    EscapeLag.plugin.getLogger().info("EscapeLag插件工作良好，暂无新版本检测更新。");
                }
                // 完成提示
                EscapeLag.plugin.getLogger().info("全部网络工作都读取完毕了...");
            } catch (IOException | NumberFormatException ex) {
            }
        }
    }

    public static void DownloadAntiAttack() {
        if (Bukkit.getPluginManager().getPlugin("AntiAttack") != null) {
            Bukkit.broadcastMessage("§a§l[EscapeLag]§c错误！您的服务器已经安装了AntiAttack反压测模块！无需再次安装！");
            return;
        }
        try {
            File AntiAttackFile = new File("/plugins", "AntiAttack.jar");
            DowloadFile("http://www.relatev.com/files/AntiAttack/AntiAttack.jar", AntiAttackFile);
            Bukkit.broadcastMessage("§a§l[EscapeLag]§b成功下载了AntiAttack反压测插件，重启即可生效！");
        } catch (IOException ex) {
        }
    }

    public static void DowloadFile(String urlStr, File savefile) throws IOException {
        if (savefile.exists() == true) {
            savefile.delete();
        }
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3 * 1000); // 3秒超时，不知道这样行不行
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        InputStream inputStream = conn.getInputStream();
        byte[] getData = readInputStream(inputStream);
        File file = savefile;
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if (fos != null) {
            fos.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    @Override
    public void run() {
        CheckAndDownloadPlugin();
    }
}
