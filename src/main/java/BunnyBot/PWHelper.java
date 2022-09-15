package BunnyBot;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * input password very fast
 *
 * @author Shang Zemo on 2022/8/3
 */
public class PWHelper {

    PWHelper(String paraPath) {
    }

    static void play(String pw) {
        try {
            TimeUnit.SECONDS.sleep(2);
            Robot bot = new Robot();
            for (byte b : pw.getBytes(StandardCharsets.UTF_8)) {
                System.out.println(b);
                // ~!@#$%^&*()_+
                boolean isUp = false;
                for (byte a:"~!@#$%^&*()_+|}{\":?".getBytes(StandardCharsets.UTF_8)){
                    if (a==b){
                        isUp = true;
                        break;
                    }
                }
                if (isUp) {
                    byte rawB = '0';
                    switch (b) {
                        case '~':
                            rawB = '`';
                            break;
                        case '!':
                            rawB = '1';
                            break;
                        case '@':
                            rawB = '2';
                            break;
                        case '#':
                            rawB = '3';
                            break;
                        case '$':
                            rawB = '4';
                            break;
                        case '%':
                            rawB = '5';
                            break;
                        case '^':
                            rawB = '6';
                            break;
                        case '&':
                            rawB = '7';
                            break;
                        case '*':
                            rawB = '8';
                            break;
                        case '(':
                            rawB = '9';
                            break;
                        case ')':
                            rawB = '0';
                            break;
                        case '_':
                            rawB = '-';
                            break;
                        case '+':
                            rawB = '=';
                            break;
                        case '|':
                            rawB = '\\';
                            break;
                        case '}':
                            rawB = ']';
                            break;
                        case '{':
                            rawB = '[';
                            break;
                        case '"':
                            rawB = '\'';
                            break;
                        case ':':
                            rawB = ';';
                            break;
                        case '?':
                            rawB = '/';
                            break;
                        case '>':
                            rawB = '.';
                            break;
                        case '<':
                            rawB = ',';
                            break;
                    }
                    bot.keyPress(KeyEvent.VK_SHIFT);
                    bot.keyPress(KeyEvent.getExtendedKeyCodeForChar(rawB));
                    bot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(rawB));
                    bot.keyRelease(KeyEvent.VK_SHIFT);
                    continue;
                }
                // A-z
                if (b < 91 && 64 < b) {
                    bot.keyPress(KeyEvent.VK_SHIFT);
                }
                bot.keyPress(KeyEvent.getExtendedKeyCodeForChar(b));
                bot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(b));
                if (b < 91 && 64 < b) {
                    bot.keyRelease(KeyEvent.VK_SHIFT);
                }
            }
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
