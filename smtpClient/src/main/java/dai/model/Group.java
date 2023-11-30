package dai.model;

import java.util.ArrayList;
import java.util.List;


public class Group {
    private String sender;
    private List<String> recipients;
    private Message message;

    // Constructeur
    public Group(String sender, ArrayList<String> recipients, Message message) {
        this.sender = sender;
        this.recipients = recipients;
        this.message = message;
    }

    // Getters et Setters

    public Message getMessage() {
        return message;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }
    public static List<Group> createGroups(int minNumberOfEmailsPerGroup, int maxNumberOfEmailsPerGroup, int numberOfGroups, List<String> victims ,List<Message> messages){
        List<Group> groups = new ArrayList<>();

        try{
            // throw exeption if there are not enough victims to form groups
            if (victims.size() < numberOfGroups * minNumberOfEmailsPerGroup) {
                throw new Exception("Error, not enough victims to form groups");
            }
            // random between min and max number of emails per group
            int groupSize = (int) (Math.random() * (maxNumberOfEmailsPerGroup - minNumberOfEmailsPerGroup)) + minNumberOfEmailsPerGroup;

            for (int i = 0; i < numberOfGroups; i++) {

                int startIndex = i * groupSize;

                int endIndex = i* groupSize + (groupSize);

                if (victims.size() < endIndex) {
                    throw new Exception("Error, not enough victims to form a group");
                }

                ArrayList<String> selectedVictims = new ArrayList<>(victims.subList(startIndex, endIndex));

                String sender = selectedVictims.remove(0);
                // get random message

                Message message = messages.get((int) (Math.random() * messages.size()));

                groups.add(new Group(sender, selectedVictims, message));
            }

            return groups;
        }catch(Exception e){
            System.out.println(e+"\n");
        }
        return null;
    }
}