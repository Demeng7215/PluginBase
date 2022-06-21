/*
 * MIT License
 *
 * Copyright (c) 2021 Revxrsal
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

package dev.demeng.pluginbase.commands.exception;

import dev.demeng.pluginbase.commands.command.CommandActor;
import dev.demeng.pluginbase.commands.command.ExecutableCommand;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.jetbrains.annotations.NotNull;

/**
 * Default implementation of {@link CommandExceptionHandler}, which sends basic messages describing
 * the exception.
 * <p>
 * See {@link CommandExceptionAdapter} for handling custom exceptions.
 */
public class DefaultExceptionHandler extends CommandExceptionAdapter {

  public static final DefaultExceptionHandler INSTANCE = new DefaultExceptionHandler();
  public static final NumberFormat FORMAT = NumberFormat.getInstance();

  @Override
  public void missingArgument(@NotNull CommandActor actor,
      @NotNull MissingArgumentException exception) {
    actor.errorLocalized("commands.missing-argument", exception.getParameter().getName());
  }

  @Override
  public void invalidEnumValue(@NotNull CommandActor actor,
      @NotNull EnumNotFoundException exception) {
    actor.errorLocalized("commands.invalid-enum", exception.getParameter().getName(), exception.getInput());
  }

  @Override
  public void invalidNumber(@NotNull CommandActor actor,
      @NotNull InvalidNumberException exception) {
//        actor.error("Expected a number, but found '" + exception.getInput() + "'.");
    actor.errorLocalized("commands.invalid-number", exception.getInput());
  }

  @Override
  public void invalidUUID(@NotNull CommandActor actor, @NotNull InvalidUUIDException exception) {
    actor.errorLocalized("commands.invalid-uuid", exception.getInput());
  }

  @Override
  public void invalidURL(@NotNull CommandActor actor, @NotNull InvalidURLException exception) {
    actor.errorLocalized("commands.invalid-url", exception.getInput());
  }

  @Override
  public void invalidBoolean(@NotNull CommandActor actor,
      @NotNull InvalidBooleanException exception) {
    actor.errorLocalized("commands.invalid-boolean", exception.getInput());
  }

  @Override
  public void noPermission(@NotNull CommandActor actor, @NotNull NoPermissionException exception) {
    actor.errorLocalized("commands.no-permission");
  }

  @Override
  public void argumentParse(@NotNull CommandActor actor,
      @NotNull ArgumentParseException exception) {
    actor.errorLocalized("commands.invalid-quoted-string");
    actor.error(exception.getSourceString());
    actor.error(exception.getAnnotatedPosition());
  }

  @Override
  public void commandInvocation(@NotNull CommandActor actor,
      @NotNull CommandInvocationException exception) {
    actor.errorLocalized("commands.error-occurred");
    exception.getCause().printStackTrace();
  }

  @Override
  public void tooManyArguments(@NotNull CommandActor actor,
      @NotNull TooManyArgumentsException exception) {
    ExecutableCommand command = exception.getCommand();
    String usage = (command.getPath().toRealString() + " " + command.getUsage()).trim();
    actor.errorLocalized("commands.too-many-arguments", usage);
  }

  @Override
  public void invalidCommand(@NotNull CommandActor actor,
      @NotNull InvalidCommandException exception) {
    actor.errorLocalized("commands.invalid-command", exception.getInput());
  }

  @Override
  public void invalidSubcommand(@NotNull CommandActor actor,
      @NotNull InvalidSubcommandException exception) {
    actor.errorLocalized("commands.invalid-subcommand", exception.getInput());
  }

  @Override
  public void noSubcommandSpecified(@NotNull CommandActor actor,
      @NotNull NoSubcommandSpecifiedException exception) {
    actor.errorLocalized("commands.no-subcommand-specified");
  }

  @Override
  public void cooldown(@NotNull CommandActor actor, @NotNull CooldownException exception) {
    actor.errorLocalized("commands.on-cooldown", formatTimeFancy(exception.getTimeLeftMillis()));
  }

  @Override
  public void invalidHelpPage(@NotNull CommandActor actor,
      @NotNull InvalidHelpPageException exception) {
    actor.errorLocalized("commands.invalid-help-page", exception.getPage(), exception.getPageCount());
  }

  @Override
  public void sendableException(@NotNull CommandActor actor, @NotNull SendableException exception) {
    exception.sendTo(actor);
  }

  @Override
  public void numberNotInRange(@NotNull CommandActor actor,
      @NotNull NumberNotInRangeException exception) {
    actor.errorLocalized("commands.number-not-in-range",
        exception.getParameter().getName(),
        FORMAT.format(exception.getMinimum()),
        FORMAT.format(exception.getMaximum()),
        FORMAT.format(exception.getInput())
    );
  }

  @Override
  public void onUnhandledException(@NotNull CommandActor actor, @NotNull Throwable throwable) {
    throwable.printStackTrace();
  }

  public static String formatTimeFancy(long time) {
    Duration d = Duration.ofMillis(time);
    long hours = d.toHours();
    long minutes = d.minusHours(hours).getSeconds() / 60;
    long seconds = d.minusMinutes(minutes).minusHours(hours).getSeconds();
    List<String> words = new ArrayList<>();
    if (hours != 0) {
      words.add(hours + plural(hours, " hour"));
    }
    if (minutes != 0) {
      words.add(minutes + plural(minutes, " minute"));
    }
    if (seconds != 0) {
      words.add(seconds + plural(seconds, " second"));
    }
    return toFancyString(words);
  }

  public static <T> String toFancyString(List<T> list) {
    StringJoiner builder = new StringJoiner(", ");
    if (list.isEmpty()) {
      return "";
    }
    if (list.size() == 1) {
      return list.get(0).toString();
    }
    for (int i = 0; i < list.size(); i++) {
      T el = list.get(i);
      if (i + 1 == list.size()) {
        return builder + " and " + el.toString();
      } else {
        builder.add(el.toString());
      }
    }
    return builder.toString();
  }

  public static String plural(Number count, String thing) {
    if (count.intValue() == 1) {
      return thing;
    }
    if (thing.endsWith("y")) {
      return thing.substring(0, thing.length() - 1) + "ies";
    }
    return thing + "s";
  }
}
