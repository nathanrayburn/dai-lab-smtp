package dai.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Group {
    private String sender;
    private List<String> recipients;

    private Map<String, Object> message;

    // Constructeur
    public Group(String sender, List<String> recipients, Map<String, Object> message) {
        this.sender = sender;
        this.recipients = recipients;
        this.message = message;
    }

    // Getters et Setters
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
    public static List<Group> createGroups(int minNumberOfEmailsPerGroup, int maxNumberOfEmailsPerGroup, int numberOfGroups, List<String> victims ,List<Map<String, Object>> messages){
        List<Group> groups = new ArrayList<>();

        try{
            if(numberOfGroups > maxNumberOfEmailsPerGroup || numberOfGroups < minNumberOfEmailsPerGroup){
                throw new Exception("Error, number of groups must be between "+minNumberOfEmailsPerGroup+" and "+maxNumberOfEmailsPerGroup);
            }

            int groupSize = maxNumberOfEmailsPerGroup;

            for (int i = 0; i < numberOfGroups; i++) {

                int startIndex = i * groupSize;

                int endIndex = i* groupSize + (groupSize);

                if (victims.size() < endIndex) {
                    throw new Exception("Error, not enough victims to form a group");
                }
                var message = messages.get((int) (Math.random() * messages.size()));

                List<String> selectedVictims = victims.subList(startIndex, endIndex);

                String sender = selectedVictims.remove(0);

                groups.add(new Group(sender, selectedVictims, message));
            }

            return groups;
        }catch(Exception e){
            System.out.println(e+"\n");
        }
        return null;
    }
}

