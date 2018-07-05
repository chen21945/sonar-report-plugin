package org.sonarsource.plugins.report.constant;

/**
 * 导出报表文件类型
 */
public enum  Categorys {

    PDF_REPORT("PDF Report"),



    ;

    private String code;

    public String getCode() {
        return code;
    }

    Categorys(String code){
        this.code = code;
    }

}
