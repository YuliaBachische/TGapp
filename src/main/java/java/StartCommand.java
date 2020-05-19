package java;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class StartCommand extends AnonymizerCommand implements IBotCommand {

    final Logger log = LogManager.getLogger(getClass());

    private final AnonymousService mAnonymouses;

    // обязательно нужно вызвать конструктор суперкласса,
    // передав в него имя и описание команды
    public StartCommand(AnonymousService anonymouses) {
        super("start", "start using bot\n");
        mAnonymouses = anonymouses;
    }

    /**
     * реализованный метод класса BotCommand, в котором обрабатывается команда, введенная пользователем
     * @param absSender - отправляет ответ пользователю
     * @param user - пользователь, который выполнил команду
     * @param chat - чат бота и пользователя
     * @param strings - аргументы, переданные с командой
     */
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (mAnonymouses.addAnonymous(new Anonymous(user, chat))) {
            log.log(Level.INFO,"User {} is trying to execute '{}' the first time. Added to users' list.");
            sb.append("Hi, ").append(user.getUserName()).append("! You've been added to bot users' list!\n")
                    .append("Please execute command:\n'/set_name <displayed_name>'\nwhere &lt;displayed_name&gt; is the name you want to use to hide your real name.");
        } else {
            log.log(Level.INFO, "User {} has already executed '{}'. Is he trying to do it one more time?");
            sb.append("You've already started bot! You can send messages if you set your name (/set_name).");
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {

    }
}
