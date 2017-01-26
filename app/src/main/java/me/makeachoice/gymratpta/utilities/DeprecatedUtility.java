package me.makeachoice.gymratpta.utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by Usuario on 12/13/2016.
 */

public class DeprecatedUtility {


    @TargetApi(23)
    public static int getColor(Context context, int colorId){
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return context.getColor(colorId);
        } else {
            return context.getResources().getColor(colorId);
        }
    }

    @TargetApi(21)
    public static Drawable getDrawable(Context context, int drawableId){
        final int version = Build.VERSION.SDK_INT;
        if(version >= 21){
            return context.getDrawable(drawableId);
        } else{
            return context.getResources().getDrawable(drawableId);
        }
    }


    public static String stripPhoneNumber(String number){
        String stripped = "";
        char[] charNumber = number.toCharArray();
        int count = charNumber.length;
        for(int i = 0; i < count; i++){
            String num = String.valueOf(charNumber[i]);

            if(num.matches("\\d+(?:\\.\\d+)?"))
            {
                stripped = stripped + num;
            }
        }

        return stripped;

    }

    public static String largeNote = "aslf;kjasldfkjalfjasl;fja;lsdjfl;asdfjlkkafjlasjflsdjf" +
            "asdlfkj asdfl;k asdflj asdf;j a;jdf alsdkj a;sld af;jf asld ;j l; jfasldk  " +
            "aslk  ;jdfs j ladkj ja lsdl aldlk jalsdk jlkj asldfj  jaldjf lkaldfj ljasdlj " +
            "asdf;jlsadk  jlasdf  l;adl  jljf jalsd j llasd jl kl  jlksadljsdl l;jasdl " +
            "asdlfkj asdfl;k asdflj asdf;j a;jdf alsdkj a;sld af;jf asld ;j l; jfasldk  " +
            "aslk  ;jdfs j ladkj ja lsdl aldlk jalsdk jlkj asldfj  jaldjf lkaldfj ljasdlj " +
            "asdf;jlsadk  jlasdf  l;adl  jljf jalsd j llasd jl kl  jlksadljsdl l;jasdl " +
            "asdlfkj asdfl;k asdflj asdf;j a;jdf alsdkj a;sld af;jf asld ;j l; jfasldk  " +
            "aslk  ;jdfs j ladkj ja lsdl aldlk jalsdk jlkj asldfj  jaldjf lkaldfj ljasdlj " +
            "asdf;jlsadk  jlasdf  l;adl  jljf jalsd j llasd jl kl  jlksadljsdl l;jasdl " +
            "asdlkj ;asdfj a;dslk l djl alds lkj; jfjd kja; slk lkd ljfkl jadl;k jljf";

}
