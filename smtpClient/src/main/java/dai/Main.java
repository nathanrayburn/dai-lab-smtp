package dai;

import dai.*;
import dai.config.Configuration;
import dai.model.Group;
import dai.network.SMTPClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Configuration config = new Configuration("src/main/java/dai/config/config.json");
            SMTPClient client = new SMTPClient(config.getSmtpHost(), config.getSmtpPort());

            List<Group> groups = Group.createGroups(config.getMinNumberOfEmailsPerGroup(), config.getMaxNumberOfEmailsPerGroup(), config.getNumberOfGroups(), config.getVictims());

            client.connect();


            client.close();
        }catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
