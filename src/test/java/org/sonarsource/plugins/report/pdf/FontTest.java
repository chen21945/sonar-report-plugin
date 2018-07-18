package org.sonarsource.plugins.report.pdf;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class FontTest {

    public void initFont(){
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", true);
        } catch (IOException e) {
            log.error("error load microsoft yahei font.", e);
        }
    }
}
