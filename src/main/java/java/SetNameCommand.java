package java;

import org.apache.log4j.Level;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public final class SetNameCommand extends AnonymizerCommand implements IBotCommand {

    private final AnonymousService mAnonymouses;

    public SetNameCommand(AnonymousService anonymouses) {
        super("set_name", "set or change name that will be displayed with your messages\n");
        mAnonymouses = anonymouses;
    }

    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        if (!mAnonymouses.hasAnonymous(user)) {
            log.log(Level.INFO, "User " + user.getId() + " is trying to execute " + getCommandIdentifier() +" without starting the bot!");
            message.setText("Firstly you should start the bot! Execute '/start' command!");
            execute(absSender, message, user);
            return;
        }

        String displayedName = getName(strings);

        if (displayedName == null) {
            log.log(Level.INFO, "User " + user.getId() + " is trying to set empty name.");
            message.setText("You should use non-empty name!");
            execute(absSender, message, user);
            return;
        }

        StringBuilder sb = new StringBuilder();

        if (mAnonymouses.setUserDisplayedName(user, displayedName)) {

            if (mAnonymouses.getDisplayedName(user) == null) {
                log.log(Level.INFO, "User " + user.getId() + " set a name" + displayedName);
                sb.append("Your displayed name: '").append(displayedName)
                        .append("'. Now you can send messages to bot!");
            } else {
                log.log(Level.INFO, "User " + user.getId() + " has changed name to " + displayedName);
                sb.append("Your new displayed name: '").append(displayedName).append("'.");
            }
        } else {
            log.log(Level.INFO, "User " + user.getId() + " is trying to set taken name " + displayedName);
            sb.append("Name ").append(displayedName).append(" is already in use! Choose another name!");
        }

        message.setText(sb.toString());
        execute(absSender, message, user);
    }

    private String getName(String[] strings) {

        if (strings == null || strings.length == 0) {
            return null;
        }

        String name = String.join(" ", strings);
        return name.replaceAll(" ", "").isEmpty() ? null : name;
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {

    }
}