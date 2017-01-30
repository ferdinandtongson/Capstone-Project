package me.makeachoice.gymratpta.model.stubData;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

import me.makeachoice.gymratpta.model.item.ClientCardItem;

public class ClientStubData {


    public static ArrayList<ClientCardItem> createClientData(Context ctx){
        ArrayList<ClientCardItem> contactList = new ArrayList();

        ClientCardItem item01 = new ClientCardItem();
        item01.clientName = "Quess Starbringer";
        item01.clientInfo = "Active";
        item01.clientEmail = "xxxx@gmail.com";
        item01.clientPhone = "(xxx) xxx-xxxx";
        item01.profilePic = Uri.EMPTY;
        contactList.add(item01);

        ClientCardItem item02 = new ClientCardItem();
        item02.clientName = "Quess Starbringer";
        item02.clientInfo = "Retired";
        item02.clientEmail = "xxxx@gmail.com";
        item02.clientPhone = "(xxx) xxx-xxxx";
        item02.profilePic = Uri.EMPTY;
        contactList.add(item02);

        ClientCardItem item03 = new ClientCardItem();
        item03.clientName = "Quess Starbringer";
        item03.clientInfo = "Active";
        item03.clientEmail = "xxxx@gmail.com";
        item03.clientPhone = "(xxx) xxx-xxxx";
        item03.profilePic = Uri.EMPTY;
        contactList.add(item03);

        ClientCardItem item04 = new ClientCardItem();
        item04.clientName = "Quess Starbringer";
        item04.clientInfo = "Retired";
        item04.clientEmail = "xxxx@gmail.com";
        item04.clientPhone = "(xxx) xxx-xxxx";
        item04.profilePic = Uri.EMPTY;
        contactList.add(item04);

        ClientCardItem item05 = new ClientCardItem();
        item05.clientName = "Quess Starbringer";
        item05.clientInfo = "Active";
        item05.clientEmail = "xxxx@gmail.com";
        item05.clientPhone = "(xxx) xxx-xxxx";
        item05.profilePic = Uri.EMPTY;
        contactList.add(item05);

        ClientCardItem item06 = new ClientCardItem();
        item06.clientName = "Quess Starbringer";
        item06.clientInfo = "Retired";
        item06.clientEmail = "xxxx@gmail.com";
        item06.clientPhone = "(xxx) xxx-xxxx";
        item06.profilePic = Uri.EMPTY;
        contactList.add(item06);

        ClientCardItem item07 = new ClientCardItem();
        item07.clientName = "Quess Starbringer";
        item07.clientInfo = "Active";
        item07.clientEmail = "xxxx@gmail.com";
        item07.clientPhone = "(xxx) xxx-xxxx";
        item07.profilePic = Uri.EMPTY;
        contactList.add(item07);

        ClientCardItem item08 = new ClientCardItem();
        item08.clientName = "Quess Starbringer";
        item08.clientInfo = "Retired";
        item08.clientEmail = "xxxx@gmail.com";
        item08.clientPhone = "(xxx) xxx-xxxx";
        item08.profilePic = Uri.EMPTY;
        contactList.add(item08);

        ClientCardItem item09 = new ClientCardItem();
        item09.clientName = "Quess Starbringer";
        item09.clientInfo = "Active";
        item09.clientEmail = "xxxx@gmail.com";
        item09.clientPhone = "(xxx) xxx-xxxx";
        item09.profilePic = Uri.EMPTY;
        contactList.add(item09);

        ClientCardItem item10 = new ClientCardItem();
        item10.clientName = "Quess Starbringer";
        item10.clientInfo = "Retired";
        item10.clientEmail = "xxxx@gmail.com";
        item10.clientPhone = "(xxx) xxx-xxxx";
        item10.profilePic = Uri.EMPTY;
        contactList.add(item10);

        return contactList;
    }

}
