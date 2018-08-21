package org.sonarsource.plugins.report;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class TokenTest
{

    public static void main(String[] args) throws UnsupportedEncodingException {

        final Base64.Encoder encoder = Base64.getEncoder();
        String b = encoder .encodeToString("22858fd738f365bd57df4a6fc3602dbcaa887eb9:".getBytes("UTF-8"));

        System.out.println(b);
    }
}
