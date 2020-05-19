package java;

import org.apache.log4j.Level;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class MyNameCommand extends AnonymizerCommand implements IBotCommand {

    private final AnonymousService mAnonymouses;

    public MyNameCommand(AnonymousService anonymouses) {
        super("my_name", "show your current name that will be displayed with your messages\n");
        mAnonymouses = anonymouses;
    }

    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        log.log(Level.INFO, user.getId() + " is performing " +  getCommandIdentifier());

        StringBuilder sb = new StringBuilder();

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!mAnonymouses.hasAnonymous(user)) {

            sb.append("You are not in bot users' list! Send /start command!");
            log.log(Level.INFO, "User " + user.getId() + " is trying to execute " + getCommandIdentifier() + " without starting the bot.");

        } else if(mAnonymouses.getDisplayedName(user) == null) {

            sb.append("Currently you don't have a name.\nSet it using command:\n'/set_name &lt;displayed_name&gt;'");
            log.log(Level.INFO, "User " + user.getId() + " is trying to execute " + getCommandIdentifier() + " without having a name.");

        } else {

            log.info("User " + user.getId() + " is executing " + getCommandIdentifier() +  " Name is " + mAnonymouses.getDisplayedName(user));
            sb.append("Your current name: ").append(mAnonymouses.getDisplayedName(user));
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }


    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {

    }
}
