/*
 * MIT License
 *
 * Copyright (c) 2021-2022 Demeng Chen
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.demeng.pluginbase;

import dev.demeng.pluginbase.plugin.BasePlugin;
import java.util.Objects;
import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

/**
 * Settings for the plugin base. Most of these methods should be overriden to suite your needs.
 * Apply the settings using {@link BasePlugin}'s #setBaseSettings().
 */
public interface BaseSettings {

  /**
   * The prefix for chat and console messages. Colorized internally.
   *
   * @return The prefix
   */
  default String prefix() {
    return "&8[&b" + Common.getName() + "&8] &r";
  }

  /**
   * The color scheme of the plugin.
   *
   * @return The color scheme
   */
  default ColorScheme colorScheme() {
    return null;
  }

  // ---------------------------------------------------------------------------------
  // TIME FORMATS
  // ---------------------------------------------------------------------------------

  /**
   * The format for dates and times combined.
   *
   * @return The date and time format
   */
  default String dateTimeFormat() {
    return "MMMM dd yyyy HH:mm z";
  }

  /**
   * The format for dates.
   *
   * @return The date format
   */
  default String dateFormat() {
    return "MMMM dd yyyy";
  }

  // ---------------------------------------------------------------------------------
  // COMMAND MESSAGES
  // ---------------------------------------------------------------------------------

  /**
   * The message to send if the command executor provides an invalid command.
   *
   * <ul>
   *   <li>%input% - Invalid input</li>
   * </ul>
   *
   * @return The invalid command message
   */
  default String invalidCommand() {
    return "&cUnknown command: &f%input%";
  }

  /**
   * The message to send if the command executor provides an invalid subcommand.
   *
   * <ul>
   *   <li>%input% - Invalid input</li>
   * </ul>
   *
   * @return The invalid subcommand message
   */
  default String invalidSubcommand() {
    return "&cUnknown subcommand: &f%input%";
  }

  /**
   * The message to send if the command executor does not provide a subcommand.
   *
   * @return The no subcommand specified message
   */
  default String noSubcommandSpecified() {
    return "&cYou must specify a subcommand.";
  }

  /**
   * The message to send if the console attempts to execute a players-only command.
   *
   * @return The not player message
   */
  default String notPlayer() {
    return "&cYou must be a player to execute this command.";
  }

  /**
   * The message to send if a player attempts to execute a console-only command.
   *
   * @return The not console message
   */
  default String notConsole() {
    return "&cThis command must be executed in console.";
  }

  /**
   * The message to send if the command executor does not have the required permission.
   *
   * @return The insufficient permission message
   */
  default String insufficientPermission() {
    return "&cYou do not have permission to do this.";
  }

  /**
   * The message to send if the command executor does not provide all required arguments.
   *
   * <ul>
   *   <li>%usage% - Command usage</li>
   *   <li>%parameter% - Missing parameter</li>
   * </ul>
   *
   * @return The incorrect usage message
   */
  default String missingArgument() {
    return "&cMissing arguments! Did you mean: &f%usage%";
  }

  /**
   * The message to send if the command executor provides too many arguments.
   *
   * <ul>
   *   <li>%usage% - Command usage</li>
   * </ul>
   *
   * @return The too many arguments message
   */
  default String tooManyArguments() {
    return "&cToo many arguments! Did you mean: &f%usage%";
  }

  /**
   * The message to send if the command executor is on cooldown.
   *
   * @return The on cooldown message
   */
  default String cooldown() {
    return "&cYou must wait &f%remaining% &cbefore using this command again.";
  }

  /**
   * The message to send if the command executor provides an invalid number.
   *
   * <ul>
   *   <li>%input% - Invalid input</li>
   * </ul>
   *
   * @return The invalid number message
   */
  default String invalidNumber() {
    return "&cInvalid number: &f%input%";
  }

  /**
   * The message to send if the command executor provides an invalid player.
   *
   * <ul>
   *   <li>%input% - Invalid input</li>
   * </ul>
   *
   * @return The invalid player message
   */
  default String invalidPlayer() {
    return "&cInvalid player: &f%input%";
  }

  /**
   * The message to send if the command executor provides an invalid UUID.
   *
   * <ul>
   *   <li>%input% - Invalid input</li>
   * </ul>
   *
   * @return The invalid UUID message
   */
  default String invalidUuid() {
    return "&cInvalid UUID: &f%input%";
  }

  /**
   * The message to send if the command executor provides an invalid boolean.
   *
   * <ul>
   *   <li>%input% - Invalid input</li>
   * </ul>
   *
   * @return The invalid boolean message
   */
  default String invalidBoolean() {
    return "&cInvalid boolean. Expected &ftrue &cor &ffalse&c, but found &f'%input%'&c.";
  }

  /**
   * The message to send if the command executor provides an invalid enum.
   *
   * <ul>
   *   <li>%parameter% - Invalid parameter</li>
   *   <li>%input% - Invalid input</li>
   * </ul>
   *
   * @return The invalid enum message
   */
  default String invalidEnum() {
    return "&cInvalid %parameter%: &f%input%";
  }

  /**
   * The message to send if the command executor provides an invalid URL.
   *
   * <ul>
   *   <li>%input% - Invalid input</li>
   * </ul>
   *
   * @return The invalid number message
   */
  default String invalidUrl() {
    return "&cInvalid URL: &f%input%";
  }

  /**
   * The message to send if the command executor provides an invalid world.
   *
   * <ul>
   *   <li>%input% - Invalid input</li>
   * </ul>
   *
   * @return The invalid world message
   */
  default String invalidWorld() {
    return "&cInvalid world: &f%input%";
  }

  /**
   * The message to send if the command executor provides an invalid selector.
   *
   * <ul>
   *   <li>%input% - Invalid input</li>
   * </ul>
   *
   * @return The invalid selector message
   */
  default String invalidSelector() {
    return "&cInvalid selector: &f%input%";
  }

  /**
   * The message to send if the command executor provides an invalid quoted string.
   *
   * <ul>
   *   <li>%source-string% - Source string arguments are being parsed from</li>
   *   <li>%annotated-position% - String pointing to the position where the error occurs</li>
   * </ul>
   *
   * @return The invalid quoted string message
   */
  default String invalidQuotedString() {
    return "&cInvalid quoted string.\n&c%source-string%\n&c%annotated-position%";
  }

  /**
   * The message to send if the command executor provides a number outside a range.
   *
   * <ul>
   *   <li>%parameter% - Invalid parameter</li>
   *   <li>%input% - Invalid input</li>
   *   <li>%min% - Minimum</li>
   *   <li>%max% - Maximum</li>
   * </ul>
   *
   * @return The number not in range message
   */
  default String numberNotInRange() {
    return "&c%parameter% must be between %min% and %max% (found &f%input%&c).";
  }

  /**
   * The message to send if the command executor provides an invalid help page.
   *
   * <ul>
   *   <li>%input% - Invalid input</li>
   *   <li>%max% - Maximum</li>
   * </ul>
   *
   * @return The invalid help page message
   */
  default String invalidHelpPage() {
    return "&cPage must be between 1 and %max% (found &f%input%&c).";
  }

  // ---------------------------------------------------------------------------------
  // WORDS AND PHRASES
  // ---------------------------------------------------------------------------------

  /**
   * The string to use if something does not exist, is not applicable, is null, etc.
   *
   * @return The not applicable phrase
   */
  default String notApplicable() {
    return "N/A";
  }

  // ---------------------------------------------------------------------------------
  // CHAT INPUT REQUESTS
  // ---------------------------------------------------------------------------------

  /**
   * The string used to cancel a chat input request.
   *
   * @return The input request cancel string
   */
  default String inputRequestEscapeSequence() {
    return "cancel";
  }

  /**
   * The default title sent to players when an input request is started and a custom title has not
   * been explicity set.
   *
   * @return The default input request title
   */
  default String inputRequestDefaultTitle() {
    return "&6Awaiting Input";
  }

  /**
   * The default subtitle sent to players when an input request is started and a custom subtitle has
   * not been explicity set.
   *
   * @return The default input request subtitle
   */
  default String inputRequestDefaultSubtitle() {
    return "&fSee chat for details.";
  }

  /**
   * The default message sent to player if the player enteres an invalid input in an input request.
   *
   * @return The default input request retry message
   */
  default String inputRequestDefaultRetryMessage() {
    return "&cInvalid input! Please try again.";
  }

  /**
   * The default message sent to player if the input request times out after 5 miuntes.
   *
   * @return The default input request timeout message
   */
  default String inputRequestDefaultTimeoutMessage() {
    return "&cYou did not respond in time!";
  }

  /**
   * A 3-color color scheme for messages or other text.
   */
  @Data
  class ColorScheme {

    /**
     * The primarily color, used as &p.
     */
    @NotNull private final String primary;

    /**
     * The secondary color, used as &s.
     */
    @NotNull private final String secondary;

    /**
     * The tertiary color, used as &t.
     */
    @NotNull private final String tertiary;

    /**
     * Gets the color scheme from a configuration section.
     *
     * @param section The configuration section containing the color scheme
     * @return The color scheme from the configuration section
     */
    @NotNull
    public static ColorScheme fromConfig(@NotNull final ConfigurationSection section) {
      return new ColorScheme(
          Objects.requireNonNull(section.getString("primary"), "Primary color is null"),
          Objects.requireNonNull(section.getString("secondary"), "Secondary color is null"),
          Objects.requireNonNull(section.getString("tertiary"), "Tertiary color is null"));
    }
  }
}
