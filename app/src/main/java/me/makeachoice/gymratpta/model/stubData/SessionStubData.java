package me.makeachoice.gymratpta.model.stubData;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.ClientCardItem;

public class SessionStubData {


    public static ArrayList<ClientCardItem> createData(Context ctx){
        ArrayList<ClientCardItem> contactList = new ArrayList();

        ClientCardItem item01 = new ClientCardItem();
        item01.clientName = "Quess Starbringer";
        item01.clientInfo = "08:00 - 09:00";
        item01.clientEmail = "xxxx@gmail.com";
        item01.clientPhone = "(xxx) xxx-xxxx";
        item01.profilePic = Uri.EMPTY;
        item01.isActive = true;
        contactList.add(item01);

        ClientCardItem item02 = new ClientCardItem();
        item02.clientName = "Quess Starbringer";
        item02.clientInfo = "09:00 - 10:00";
        item02.clientEmail = "xxxx@gmail.com";
        item02.clientPhone = "(xxx) xxx-xxxx";
        item02.profilePic = Uri.EMPTY;
        item02.isActive = true;
        contactList.add(item02);

        ClientCardItem item03 = new ClientCardItem();
        item03.clientName = "Quess Starbringer";
        item03.clientInfo = "10:00 - 11:00";
        item03.clientEmail = "xxxx@gmail.com";
        item03.clientPhone = "(xxx) xxx-xxxx";
        item03.profilePic = Uri.EMPTY;
        item03.isActive = true;
        contactList.add(item03);

        ClientCardItem item04 = new ClientCardItem();
        item04.clientName = "Quess Starbringer";
        item04.clientInfo = "11:00 - 12:00";
        item04.clientEmail = "xxxx@gmail.com";
        item04.clientPhone = "(xxx) xxx-xxxx";
        item04.profilePic = Uri.EMPTY;
        item04.isActive = true;
        contactList.add(item04);

        ClientCardItem item05 = new ClientCardItem();
        item05.clientName = "Quess Starbringer";
        item05.clientInfo = "12:00 - 13:00";
        item05.clientEmail = "xxxx@gmail.com";
        item05.clientPhone = "(xxx) xxx-xxxx";
        item05.profilePic = Uri.EMPTY;
        item05.isActive = true;
        contactList.add(item05);

        ClientCardItem item06 = new ClientCardItem();
        item06.clientName = "Quess Starbringer";
        item06.clientInfo = "13:00 - 14:00";
        item06.clientEmail = "xxxx@gmail.com";
        item06.clientPhone = "(xxx) xxx-xxxx";
        item06.profilePic = Uri.EMPTY;
        item06.isActive = true;
        contactList.add(item06);

        ClientCardItem item07 = new ClientCardItem();
        item07.clientName = "Quess Starbringer";
        item07.clientInfo = "14:00 - 15:00";
        item07.clientEmail = "xxxx@gmail.com";
        item07.clientPhone = "(xxx) xxx-xxxx";
        item07.profilePic = Uri.EMPTY;
        item07.isActive = true;
        contactList.add(item07);

        ClientCardItem item08 = new ClientCardItem();
        item08.clientName = "Quess Starbringer";
        item08.clientInfo = "015:00 - 16:00";
        item08.clientEmail = "xxxx@gmail.com";
        item08.clientPhone = "(xxx) xxx-xxxx";
        item08.profilePic = Uri.EMPTY;
        item08.isActive = true;
        contactList.add(item08);

        ClientCardItem item09 = new ClientCardItem();
        item09.clientName = "Quess Starbringer";
        item09.clientInfo = "16:00 - 17:00";
        item09.clientEmail = "xxxx@gmail.com";
        item09.clientPhone = "(xxx) xxx-xxxx";
        item09.profilePic = Uri.EMPTY;
        item09.isActive = true;
        contactList.add(item09);

        ClientCardItem item10 = new ClientCardItem();
        item10.clientName = "Quess Starbringer";
        item10.clientInfo = "17:00 - 18:00";
        item10.clientEmail = "xxxx@gmail.com";
        item10.clientPhone = "(xxx) xxx-xxxx";
        item10.profilePic = Uri.EMPTY;
        item10.isActive = true;
        contactList.add(item10);

        return contactList;
    }

}
