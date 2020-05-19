package java;

import org.apache.log4j.Level;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class StopCommand extends AnonymizerCommand implements IBotCommand {

    private final AnonymousService mAnonymouses;

    public StopCommand(AnonymousService anonymouses) {
        super("stop", "remove yourself from bot users' list\n");
        mAnonymouses = anonymouses;
    }

    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.log(Level.INFO, + user.getId() + " is performing " + getCommandIdentifier());

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (mAnonymouses.removeAnonymous(user)) {
            log.info("User " + user.getId() + " has been removed from users list!" );
            sb.append("You've been removed from bot's users list! Bye!");
        } else {
            log.log(Level.INFO, "User" + user.getId() + " is trying to execute " + getCommandIdentifier() +  " without having executed 'start' before!");
            sb.append("You were not in bot users' list. Bye!");
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {

    }
}