package com.promc.announce;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.Arrays;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    private static final List<String> legacyColors = Arrays.asList("&0", "&1", "&2", "&3", "&4", "&5", "&6", "&7", "&8", "&9", "&a", "&b", "&c", "&d", "&e", "§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e");
    private static final List<String> specialChars = Arrays.asList("&l", "&n", "&o", "&k", "&m", "§l", "§n", "§o", "§k", "§m");
    private static final Pattern patternNormal = Pattern.compile("\\{#([0-9A-Fa-f]{6})\\}");
    private static final Pattern patternGrad = Pattern.compile("\\{#([0-9A-Fa-f]{6})>\\}(.*?)\\{#([0-9A-Fa-f]{6})<\\}");
    private static final Pattern patternOneFromTwo = Pattern.compile("\\{#([0-9A-Fa-f]{6})<>\\}");
    private static Matcher matcher;

    public enum ColorizeType {
        GRADIENT,
        RGB,
        CLASSIC
    }

    /**
     * 在这里我们可以选择颜色化的类型，
     * 如果只想使用RGB，可以将ColorizeType.RGB传入参数
     *
     * @param input 消息
     * @param type  ColorizeType枚举，用于更好地选择类型
     * @return 上色后的输出，或者为空类型
     */
    public static String colorizeType(ColorizeType type, String input) {
        return switch (type) {
            case GRADIENT -> colorizeGradient(input);
            case RGB -> colorizeRGB(input);
            case CLASSIC -> colorizeClassic(input);
        };
    }

    /**
     * 这个方法清除了整个字符串
     *
     * @return 没有模式、旧颜色和特殊字符的字符串
     */
    public static String clear(String input) {
        input = removePatterns(input);
        input = removeLegacyColors(input);
        input = removeSpecialChars(input);
        return input;
    }

    /**
     * 在这个方法中，我们移除了类似 "&n"、"&r" 等特殊字符
     * 示例：
     * "&kForestTech ❤" -> "ForestTech ❤"
     *
     * @param input 消息
     * @return 输出
     */
    public static String removeSpecialChars(String input) {
        for (String chars : specialChars) {
            if (!input.contains(chars)) {
                continue;
            }
            input = input.replaceAll(chars, "");
        }

        return input;
    }

    /**
     * 在这个方法中，我们移除了类似 "&2"、"&c" 等旧颜色代码
     * 示例：
     * "&2ForestTech ❤" -> "ForestTech ❤"
     *
     * @param input 消息
     * @return 输出
     */
    public static String removeLegacyColors(String input) {
        for (String color : legacyColors) {
            if (!input.contains(color)) {
                continue;
            }
            input = input.replaceAll(color, "");
        }
        return input;
    }

    /**
     * 如果我们想要从消息中移除模式，我们可以使用这个方法
     * 示例：
     * "{#00e64e}ForestTech ❤" -> "ForestTech ❤"
     *
     * @param input 带有模式的消息
     * @return 没有模式的字符串
     */
    public static String removePatterns(String input) {
        input = input.replaceAll("\\{#([0-9A-Fa-f]{6})>\\}", "");
        input = input.replaceAll("\\{#([0-9A-Fa-f]{6})<\\}", "");
        input = input.replaceAll("\\{#([0-9A-Fa-f]{6})\\}", "");
        return input;
    }

    /**
     * 通用的方法，用于给消息添加颜色
     *
     * @param input 消息
     * @return 上色后的消息
     */
    public static String colorize(String input) {
        input = colorizeGradient(input);
        input = colorizeRGB(input);
        return input;
    }

    /**
     * 用于将普通的颜色代码转换为Spigot的颜色
     */
    public static String colorizeClassic(String input) {
        input = ChatColor.translateAlternateColorCodes('&', input);
        return input;
    }

    /**
     * 在这里我们可以调用渐变上色的方法
     *
     * Group 1 = 第一个渐变
     * Group 3 = 第二个渐变
     * Group 2 = 内容
     *
     * @param input 消息
     * @return 带有渐变的上色输出
     */
    public static String colorizeGradient(String input) {
        matcher = patternOneFromTwo.matcher(input);

        StringBuffer output = new StringBuffer();

        while (matcher.find()) {
            String text = matcher.group(1);
            matcher.appendReplacement(output, "{#" + text + "<}{#" + text + ">}");
        }
        matcher.appendTail(output);

        input = output.toString();

        matcher = patternGrad.matcher(input);
        while (matcher.find()) {
            input = input.replace(matcher.group(), color(matcher.group(2), new Color(Integer.parseInt(matcher.group(1), 16)), new Color(Integer.parseInt(matcher.group(3), 16))));
        }
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    /**
     * 这个方法只会进行普通的RGB上色，不包含渐变
     *
     * @param input 消息
     * @return 带有RGB颜色的输出
     */
    public static String colorizeRGB(String input) {
        matcher = patternNormal.matcher(input);
        String color;
        while (matcher.find()) {
            color = matcher.group(1);
            if (color == null) {
                color = matcher.group(2);
            }
            input = input.replace(matcher.group(), getColor(color) + "");
        }
        return input;
    }

    /**
     * 用于渐变上色的颜色方法，例如，这会取第一个渐变和第二个渐变，并在消息中进行渐变
     *
     * @param input  整个消息
     * @param first  第一个渐变
     * @param second 第二个渐变
     */
    public static String color(String input, Color first, Color second) {
        ChatColor[] colors = createGradient(first, second, removeSpecialChars(input).length());
        return apply(input, colors);
    }

    private static String apply(String input, ChatColor[] colors) {
        StringBuilder specialColors = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        String[] characters = input.split("");
        int outIndex = 0;

        for (int i = 0; i < characters.length; i++) {
            if (!characters[i].equals("&") && !characters[i].equals("§")) {
                stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
                continue;
            }
            if (i + 1 >= characters.length) {
                stringBuilder.append(colors[outIndex++]).append(specialColors).append(characters[i]);
                continue;
            }
            if (characters[i + 1].equals("r")) {
                specialColors.setLength(0);
            } else {
                specialColors.append(characters[i]);
                specialColors.append(characters[i + 1]);
            }
            i++;
        }
        return stringBuilder.toString();
    }

    /**
     * @return 字符串的颜色
     */
    private static ChatColor[] createGradient(Color first, Color second, int amount) {
        ChatColor[] colors = new ChatColor[amount];
        int amountR = Math.abs(first.getRed() - second.getRed()) / (amount - 1);
        int amountG = Math.abs(first.getGreen() - second.getGreen()) / (amount - 1);
        int amountB = Math.abs(first.getBlue() - second.getBlue()) / (amount - 1);
        int[] colorDir = new int[]{first.getRed() < second.getRed() ? +1 : -1, first.getGreen() < second.getGreen() ? +1 : -1, first.getBlue() < second.getBlue() ? +1 : -1};

        for (int i = 0; i < amount; i++) {
            Color color = new Color(first.getRed() + ((amountR * i) * colorDir[0]), first.getGreen() + ((amountG * i) * colorDir[1]), first.getBlue() + ((amountB * i) * colorDir[2]));
            colors[i] = ChatColor.of(color);
        }
        return colors;
    }

    public static ChatColor getColor(String matcher) {
        return ChatColor.of(new Color(Integer.parseInt(matcher, 16)));
    }

}
