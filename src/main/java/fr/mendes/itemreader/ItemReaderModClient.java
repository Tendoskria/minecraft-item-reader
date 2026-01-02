package fr.mendes.itemreader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class ItemReaderModClient implements ClientModInitializer {
    private static KeyBinding getItemTextKey;
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    @Override
    public void onInitializeClient() {
        // Register a keybinding (default: G key)
        getItemTextKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.itemreader.get_item_text",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.itemreader.general"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (getItemTextKey.wasPressed()) {
                if (client.player != null) {
                    ItemStack heldItem = client.player.getMainHandStack();

                    if (!heldItem.isEmpty()) {
                        try {
                            JsonObject jsonOutput = new JsonObject();

                            // Get custom name
                            if (heldItem.contains(DataComponentTypes.CUSTOM_NAME)) {
                                Text customName = heldItem.get(DataComponentTypes.CUSTOM_NAME);
                                if (customName != null) {
                                    String htmlName = trimHtml(textToHtml(customName));
                                    jsonOutput.addProperty("minecraft:custom_name", htmlName);
                                }
                            }

                            // Get enchantment
                            // TODO

                            // Get lore
                            if (heldItem.contains(DataComponentTypes.LORE)) {
                                var lore = heldItem.get(DataComponentTypes.LORE);
                                if (lore != null && !lore.lines().isEmpty()) {
                                    StringBuilder loreHtml = new StringBuilder("<p>");
                                    boolean first = true;
                                    for (Text loreLine : lore.lines()) {
                                        if (!first) {
                                            loreHtml.append("<br>");
                                        }
                                        loreHtml.append(textToHtml(loreLine));
                                        first = false;
                                    }
                                    loreHtml.append("</p>");
                                    jsonOutput.addProperty("minecraft:lore", trimHtml(loreHtml.toString()));
                                }
                            }

                            // Get item name (registry key)
                            var registryKey = heldItem.getRegistryEntry().getKey();
                            if (registryKey.isPresent()) {
                                jsonOutput.addProperty("minecraft:item_name", registryKey.get().getValue().toString());
                            }

                            String jsonString = GSON.toJson(jsonOutput);

                            copyToClipboard(jsonString);

                            client.player.sendMessage(Text.literal("§aJSON copied to clipboard!"), false);
                        } catch (Exception e) {
                            client.player.sendMessage(Text.literal("§cError: " + e.getMessage()), false);
                            e.printStackTrace();
                        }
                    } else {
                        client.player.sendMessage(Text.literal("§cNo item in hand!"), false);
                    }
                }
            }
        });
    }

    /**
     * Converts Minecraft Text component to HTML with proper styling
     */
    private String textToHtml(Text text) {
        StringBuilder html = new StringBuilder();
        appendTextWithStyle(text, html);
        return html.toString();
    }

    /**
     * Recursively appends text with its style converted to HTML
     */
    private void appendTextWithStyle(Text text, StringBuilder html) {
        String literalContent = "";
        try {
            // Try to get the literal text content directly
            var content = text.getContent();
            if (content.toString().startsWith("literal{")) {
                // Extract the literal text from the toString format
                String str = content.toString();
                int start = str.indexOf('{') + 1;
                int end = str.indexOf('}');
                if (start > 0 && end > start) {
                    literalContent = str.substring(start, end);
                }
            }
        } catch (Exception e) {
            // If we can't get literal content, skip it
        }

        Style style = text.getStyle();

        // Only process if we have literal content OR siblings
        boolean hasContent = !literalContent.isEmpty();
        boolean hasSiblings = !text.getSiblings().isEmpty();

        if (!hasContent && !hasSiblings) {
            return; // Nothing to process
        }

        // Open tags based on style (only if we have content to wrap)
        boolean hasSpan = false;
        StringBuilder spanStyle = new StringBuilder();

        // Color
        if (style.getColor() != null) {
            int color = style.getColor().getRgb();
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;
            spanStyle.append("color: rgb(").append(r).append(", ").append(g).append(", ").append(b).append(");");
            hasSpan = true;
        }

        if (hasSpan && (hasContent || hasSiblings)) {
            html.append("<span style=\"").append(spanStyle).append("\">");
        }

        // Bold
        if (style.isBold() && (hasContent || hasSiblings)) {
            html.append("<strong>");
        }

        // Italic
        if (style.isItalic() && (hasContent || hasSiblings)) {
            html.append("<em>");
        }

        // Underlined
        if (style.isUnderlined() && (hasContent || hasSiblings)) {
            html.append("<u>");
        }

        // Strikethrough
        if (style.isStrikethrough() && (hasContent || hasSiblings)) {
            html.append("<s>");
        }

        // Append the actual literal text content if not empty
        if (hasContent) {
            html.append(escapeHtml(literalContent));
        }

        // Process siblings (children text components)
        for (Text sibling : text.getSiblings()) {
            appendTextWithStyle(sibling, html);
        }

        // Close tags in reverse order
        if (style.isStrikethrough() && (hasContent || hasSiblings)) {
            html.append("</s>");
        }

        if (style.isUnderlined() && (hasContent || hasSiblings)) {
            html.append("</u>");
        }

        if (style.isItalic() && (hasContent || hasSiblings)) {
            html.append("</em>");
        }

        if (style.isBold() && (hasContent || hasSiblings)) {
            html.append("</strong>");
        }

        if (hasSpan && (hasContent || hasSiblings)) {
            html.append("</span>");
        }
    }

    /**
     * Trims HTML by removing leading/trailing spaces and <br> tags
     */
    private String trimHtml(String html) {
        if (html == null || html.isEmpty()) {
            return html;
        }

        String result = html;

        // Remove leading spaces and <br> tags
        while (result.startsWith(" ") || result.startsWith("<br>")) {
            if (result.startsWith(" ")) {
                result = result.substring(1);
            } else if (result.startsWith("<br>")) {
                result = result.substring(4);
            }
        }

        // Remove trailing spaces and <br> tags
        while (result.endsWith(" ") || result.endsWith("<br>")) {
            if (result.endsWith(" ")) {
                result = result.substring(0, result.length() - 1);
            } else if (result.endsWith("<br>")) {
                result = result.substring(0, result.length() - 4);
            }
        }

        return result;
    }

    /**
     * Escapes HTML special characters
     */
    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    /**
     * Copies text to system clipboard
     */
    private void copyToClipboard(String text) {
        try {
            StringSelection selection = new StringSelection(text);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
        } catch (Exception e) {
            System.err.println("Failed to copy to clipboard: " + e.getMessage());
        }
    }
}