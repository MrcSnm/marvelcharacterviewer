package com.hipreme.mobbuy.marvel;

import androidx.annotation.NonNull;

import com.hipreme.mobbuy.R;
import com.hipreme.mobbuy.utils.Resources;

import java.util.Date;
import static com.hipreme.mobbuy.utils.Digest.MD5;


public class MarvelAPI {

    public static @NonNull String getPublicPath()
    {
        return Resources.getString(R.string.marvel_base_url) + "/" +
                Resources.getString(R.string.marvel_api_version) +"/public/";
    }

    /**
     * @return [Timestamp, PubKey, Hash]
     */
    public static @NonNull String[] generateApikey()
    {
        Date timestamp = new Date();
        String _pv = "1ff31efe290a4c10095db9d72bd013e398f6ff33";

        return new String[]
            {
                String.valueOf(timestamp.getTime()),
                Resources.getString(R.string.marvel_public_key),
                MD5(timestamp.getTime() + _pv + Resources.getString(R.string.marvel_public_key))
            };
    }

    public static @NonNull String generateApiKeyString()
    {
        String[] s = generateApikey();
        return "?ts="+s[0] +"&apikey="+s[1]+"&hash="+s[2];
    }

    public static @NonNull String getFromPublicPath(String baseName)
    {
        String[] apiKey = generateApikey();

        return getPublicPath() + baseName +
                "?ts=" + apiKey[0] +
                "&apikey="+ apiKey[1] +
                "&hash="+ apiKey[2];
    }
    public static @NonNull String getFromPublicPath(int r_id)
    {
        return getFromPublicPath(Resources.getString(r_id));
    }

}
